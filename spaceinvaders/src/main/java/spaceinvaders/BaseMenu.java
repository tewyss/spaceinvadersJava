package spaceinvaders;

import java.util.*;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import spaceinvaders.storage.HallOfFameManager;

@Log4j2
public abstract class BaseMenu {
	@Getter
	protected VBox menuLayout;
	protected List<Text> menuItems;
	protected int indexOfSelection = 0;
	protected ResourceBundle bundle;

	protected BaseMenu() {
		bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

		menuLayout = new VBox(30);
		menuItems = new ArrayList<>();
		menuLayout.setAlignment(Pos.CENTER);
		menuLayout.setStyle("-fx-background-color: black;");
	}

	protected void addTitle(String titleText, Font customFont, Color color) {
		Text title = new Text(titleText);
		title.setFont(Font.font(customFont.getFamily(), 40));
		title.setFill(color);
		menuLayout.getChildren().add(title);
	}

	protected void addScore(String labelText, int score, Font customFont) {
		Text scoreText = new Text(labelText + ": " + score);
		scoreText.setFont(Font.font(customFont.getFamily(), 30));
		scoreText.setFill(Color.WHITE);
		menuLayout.getChildren().add(scoreText);
	}

	protected void askPlayerNameAndSaveScore(int score) {
		if (score == 0) return;

		Platform.runLater(() -> {
			boolean asking = true;
			while (asking) {
				TextInputDialog dialog = new TextInputDialog("Player");
				dialog.setTitle(bundle.getString("enter_name_title"));
				dialog.setHeaderText(bundle.getString("enter_name_header"));
				dialog.setContentText(bundle.getString("enter_name_prompt"));

				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					String playerName = result.get().trim();
					if (playerName.matches("[A-Za-z0-9]{1,15}")) {
						saveScore(playerName, score);
						asking = false;
					} else {
						Alert alert = new Alert(Alert.AlertType.WARNING);
						alert.setTitle(bundle.getString("invalid_input_title"));
						alert.setHeaderText(null);
						alert.setContentText(bundle.getString("invalid_input_text"));
						alert.showAndWait();
					}
				} else {
					log.info(bundle.getString("user_cancel"));
					asking = false;
				}
			}
		});
	}

	protected void saveScore(String playerName, int score) {
		try {
			HallOfFameManager manager = new HallOfFameManager();
			manager.saveSingleScore(playerName, score);
			log.info(bundle.getString("score_saved") + " " + playerName + ", " + score);
		} catch (Exception e) {
			log.error("Chyba při ukládání skóre přes REST API", e);
			Platform.runLater(() -> {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(bundle.getString("error_title"));
				alert.setHeaderText(null);
				alert.setContentText(bundle.getString("error_saving_score"));
				alert.showAndWait();
			});
		}
	}

	protected void createMenuItem(String textContent, Font customFont) {
		Text menuItem = new Text(textContent);
		menuItem.setFont(customFont);
		menuItem.setFill(Color.WHITE);
		menuLayout.getChildren().add(menuItem);
		menuItems.add(menuItem);
	}

	protected void updateMenuSelection() {
		for (int i = 0; i < menuItems.size(); i++) {
			Text menuItem = menuItems.get(i);
			menuItem.setFill(i == indexOfSelection ? Color.rgb(30, 255, 0) : Color.WHITE);
		}
	}

	public void moveSelectionUp() {
		indexOfSelection = (indexOfSelection - 1 + menuItems.size()) % menuItems.size();
		updateMenuSelection();
	}

	public void moveSelectionDown() {
		indexOfSelection = (indexOfSelection + 1) % menuItems.size();
		updateMenuSelection();
	}

	public String getSelectedItem() {
		return menuItems.get(indexOfSelection).getText();
	}

	public VBox getLayout() {
		return menuLayout;
	}
}
