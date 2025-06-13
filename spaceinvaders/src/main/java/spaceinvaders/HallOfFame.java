package spaceinvaders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.converter.IntegerStringConverter;
import lombok.extern.log4j.Log4j2;
import spaceinvaders.storage.PlayerScore;
import spaceinvaders.storage.HallOfFameManager;

@Log4j2
public class HallOfFame extends BaseMenu {
	private final TableView<PlayerScore> table    = new TableView<>();
	private final HallOfFameManager      manager  = new HallOfFameManager();
	private final Main                   mainApp;
	private static final DateTimeFormatter DTF     =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public HallOfFame(Font customFont, Main mainApp) {
		this.mainApp = mainApp;

		addTitle(bundle.getString("hall_of_fame_title"), customFont, Color.GOLD);

		var nameCol = new TableColumn<PlayerScore,String>(bundle.getString("player_column"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		nameCol.setOnEditCommit(evt -> {
			PlayerScore ps       = evt.getRowValue();
			String     oldName   = evt.getOldValue();
			String     newName   = evt.getNewValue() != null ? evt.getNewValue().trim() : "";
			if (newName.isEmpty() || !newName.matches("[A-Za-z0-9]{1,15}")) {
				Alert alert = new Alert(AlertType.WARNING,
						"Jméno nesmí být prázdné a musí obsahovat max. 15 alfanumerických znaků");
				alert.setHeaderText(null);
				alert.showAndWait();
				ps.setPlayerName(oldName);
				table.refresh();
				return;
			}
			ps.setPlayerName(newName);
			try {
				manager.updatePlayerName(ps.getId(), newName);
			} catch (Exception e) {
				log.error("Chyba při změně jména hráče", e);
				ps.setPlayerName(oldName);
				table.refresh();
				Alert err = new Alert(AlertType.ERROR,
						"Nepodařilo se uložit nové jméno na serveru");
				err.setHeaderText(null);
				err.showAndWait();
			}
		});

		var scoreCol = new TableColumn<PlayerScore,Integer>(bundle.getString("score_column"));
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
		scoreCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		scoreCol.setOnEditCommit(evt -> {
			PlayerScore ps        = evt.getRowValue();
			Integer     oldScore  = evt.getOldValue();
			Integer     newScore  = evt.getNewValue();
			if (newScore == null || newScore < 0) {
				Alert alert = new Alert(AlertType.WARNING,
						"Skóre musí být celé nezáporné číslo");
				alert.setHeaderText(null);
				alert.showAndWait();
				ps.setScore(oldScore);
				table.refresh();
				return;
			}
			ps.setScore(newScore);
			try {
				manager.updateScore(ps.getId(), newScore);
			} catch (Exception e) {
				log.error("Chyba při aktualizaci skóre", e);
				ps.setScore(oldScore);
				table.refresh();
				Alert err = new Alert(AlertType.ERROR,
						"Nepodařilo se uložit nové skóre na serveru");
				err.setHeaderText(null);
				err.showAndWait();
			}
		});

		var dateCol = new TableColumn<PlayerScore,String>(bundle.getString("date_column"));
		dateCol.setCellValueFactory(cell -> {
			LocalDateTime ts = cell.getValue().getRecordedAt();
			String fmt = ts != null ? ts.format(DTF) : "";
			return new ReadOnlyStringWrapper(fmt);
		});

		table.getColumns().setAll(nameCol, scoreCol, dateCol);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setPrefSize(600, 400);
		table.setEditable(true);

		table.setRowFactory(tv -> {
			TableRow<PlayerScore> row = new TableRow<>();
			var menu = new ContextMenu();
			var del  = new MenuItem(bundle.getString("delete_record"));
			del.setOnAction(e -> deleteScore(row.getItem()));
			menu.getItems().add(del);
			row.contextMenuProperty().bind(
					Bindings.when(row.emptyProperty())
							.then((ContextMenu)null)
							.otherwise(menu)
			);
			return row;
		});

		loadScores();

		menuLayout.getChildren().addAll(table);

		Button backBtn = new Button(bundle.getString("return_esc"));
		backBtn.setFont(customFont);
		backBtn.setOnAction(e -> mainApp.changeToMain());
		menuLayout.getChildren().add(backBtn);

		table.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
			if (evt.getCode() == KeyCode.ESCAPE) {
				backBtn.fire();
				evt.consume();
			}
		});
	}

	private void loadScores() {
		try {
			List<PlayerScore> list = manager.loadScores();
			table.setItems(FXCollections.observableArrayList(list));
		} catch (Exception e) {
			log.error("Chyba při načítání skóre z REST API", e);
		}
	}

	private void deleteScore(PlayerScore ps) {
		if (ps == null) return;
		var alert = new Alert(AlertType.CONFIRMATION,
				bundle.getString("confirm_delete_score"),
				ButtonType.YES, ButtonType.NO);
		alert.setHeaderText(null);
		alert.showAndWait()
				.filter(b -> b == ButtonType.YES)
				.ifPresent(b -> {
					try {
						manager.deleteScore(ps.getId());
						loadScores();
					} catch (Exception ex) {
						log.error("Chyba při mazání skóre", ex);
					}
				});
	}
}
