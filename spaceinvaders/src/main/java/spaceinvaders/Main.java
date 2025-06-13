package spaceinvaders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
	private boolean isGamePaused = false;
	private Game game;
	private Scene gameScene;
	private static double savedVolume = 0.5;
	private Stage primaryStage;
	Font myFont;
	protected ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

	@Override
	public void start(Stage primaryStage) {
		myFont = Font.loadFont(getClass().getResourceAsStream("/fonts/myFont.ttf"), 20);

		this.primaryStage = primaryStage;
		changeToMain();

	}

	public void startGame(Font myFont) {
		if (game != null) {
			game.stopGame();
		}
		game = new Game(this);
		gameScene = new Scene(game.getGameLayout(), 800, 600);

		gameScene.setFill(Color.BLACK);
		primaryStage.setScene(gameScene);

		gameScene.setOnKeyPressed(event -> {
			String key = event.getCode().toString();
			if (key.equals("LEFT") || key.equals("RIGHT") || key.equals("SPACE") || key.equals("UP")) {
				game.setKeyPressed(true, key);
			} else if (key.equals("ESCAPE")) {
				if (!isGamePaused) {
					game.pause();
					pauseGame(myFont);
				} else {
					resumeGame();
				}
			}
		});

		gameScene.setOnKeyReleased(event -> {
			String key = event.getCode().toString();
			if (key.equals("LEFT") || key.equals("RIGHT") || key.equals("SPACE") || key.equals("UP")) {
				game.setKeyPressed(false, key);
			}
		});
	}

	private void pauseGame(Font myFont) {
		if (!isGamePaused) {
			isGamePaused = true;
			PauseMenu pauseMenu = new PauseMenu(myFont);
			Scene pauseScene = new Scene(pauseMenu.getLayout(), 800, 600);
			pauseScene.setFill(Color.BLACK);
			primaryStage.setScene(pauseScene);
			game.clearPressedKeys();

			// Handling key events for pause menu
			pauseScene.setOnKeyPressed(event -> {
				switch (event.getCode()) {
				case UP:
					pauseMenu.moveSelectionUp();
					break;
				case DOWN:
					pauseMenu.moveSelectionDown();
					break;
				case ENTER:
					handlePauseMenuSelection(pauseMenu.getSelectedItem(), myFont);
					break;
				default:
					break;
				}
			});
		}
	}

	private void handlePauseMenuSelection(String selectedItem, Font myFont) {
		String resume = bundle.getString("resume");
		String options = bundle.getString("options");
		String exitToMain = bundle.getString("exit_to_main");
		String exitGame = bundle.getString("exit_game");
		if (selectedItem.equals(resume)) {
			resumeGame();
		} else if (selectedItem.equals(options)) {
			openOptionsMenu(myFont, true);
		} else if (selectedItem.equals(exitToMain)) {
			isGamePaused = false;
			this.changeToMain();
		} else if (selectedItem.equals(exitGame)) {
			System.exit(0);
		}
	}

	public void openOptionsMenu(Font myFont, boolean fromPauseMenu) {
		OptionsMenu optionsMenu = new OptionsMenu(myFont);
		Scene optionsScene = new Scene(optionsMenu.getLayout(), 800, 600);
		optionsScene.setFill(Color.BLACK);

		primaryStage.setScene(optionsScene);

		optionsScene.setOnKeyPressed(event -> {
			switch (event.getCode()) {
			case LEFT:
				optionsMenu.decreaseVolume();
				break;
			case RIGHT:
				optionsMenu.increaseVolume();
				break;
			case ESCAPE:
				Main.setSavedVolume(optionsMenu.getVolumeLevel());
				if (fromPauseMenu) {
					resumeGame();
				} else {
					this.changeToMain();
				}
				break;
			default:
				break;
			}
		});
	}

	private void handleGameOverMenuSelection(String selectedItem, Font myFont) {
		String retry = bundle.getString("retry");
		String exitToMain = bundle.getString("exit_to_main");
		String exitGame = bundle.getString("exit_game");

		if (selectedItem.equals(retry)) {
			game = new Game(this);
			game.resetGame();
			startGame(myFont);
		} else if (selectedItem.equals(exitToMain)) {
			game.stopGame();
			this.changeToMain();
		} else if (selectedItem.equals(exitGame)) {
			System.exit(0);
		}
	}


	public void showGameOverMenu(Font myFont) {
		int score = game.getCurrentScore();
		GameOverMenu gameOverMenu = new GameOverMenu(myFont, score);
		Scene gameOverScene = new Scene(gameOverMenu.getLayout(), 800, 600);
		gameOverScene.setFill(Color.BLACK);

		primaryStage.setScene(gameOverScene);

		gameOverScene.setOnKeyPressed(event -> {
			switch (event.getCode()) {
			case UP:
				gameOverMenu.moveSelectionUp();
				break;
			case DOWN:
				gameOverMenu.moveSelectionDown();
				break;
			case ENTER:
				handleGameOverMenuSelection(gameOverMenu.getSelectedItem(), myFont);
				break;
			default:
				break;
			}
		});
	}

	private void handleVictoryMenuSelection(String selectedItem) {
		String exitToMain = bundle.getString("exit_to_main");
		String exitGame = bundle.getString("exit_game");

		if (selectedItem.equals(exitToMain)) {
			game.stopGame();
			this.changeToMain();
		} else if (selectedItem.equals(exitGame)) {
			System.exit(0);
		}
	}

	public void showVictoryOverMenu(Font myFont) {
		int score = game.getCurrentScore();
		VictoryMenu victoryMenu = new VictoryMenu(myFont, score);
		Scene victoryScene = new Scene(victoryMenu.getLayout(), 800, 600);
		victoryScene.setFill(Color.BLACK);

		primaryStage.setScene(victoryScene);

		victoryScene.setOnKeyPressed(event -> {
			switch (event.getCode()) {
			case UP:
				victoryMenu.moveSelectionUp();
				break;
			case DOWN:
				victoryMenu.moveSelectionDown();
				break;
			case ENTER:
				handleVictoryMenuSelection(victoryMenu.getSelectedItem());
				break;
			default:
				break;
			}
		});
	}

	public void showHallOfFameMenu() {
		HallOfFame hallOfFameMenu = new HallOfFame(myFont, this);
		Scene hallOfFameScene = new Scene(hallOfFameMenu.getLayout(), 800, 600);
		hallOfFameScene.setFill(Color.BLACK);

		primaryStage.setScene(hallOfFameScene);

		hallOfFameScene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				this.changeToMain();
			}
		});
	}

	private void resumeGame() {
		isGamePaused = false;
		primaryStage.setScene(gameScene);
		game.resume();
	}

	public static double getSavedVolume() {
		return savedVolume;
	}

	public static void setSavedVolume(double volume) {
		savedVolume = volume;
	}

	Font getFont() {
		return myFont;
	}

	void exitGame() {
		System.exit(0);
	}

	void changeToMain() {
		try {
			FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/mainMenuWindow.fxml"));
			Parent root = menuLoader.load();
			MenuController menuController = menuLoader.getController();
			menuController.setMain(this);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.resizableProperty().set(false);
			primaryStage.setTitle(bundle.getString("window_title"));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}