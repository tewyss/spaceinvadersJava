package spaceinvaders;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuController {
	private Main main;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exitButton;

    @FXML
    private Button hallOfFameButton;

    @FXML
    private Button optionsButton;

    @FXML
    private Button startButton;
    
    @FXML
    private Text titleText;

    @FXML
    void exitGame(ActionEvent event) {
    	try {
			main.exitGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void openHallOfFame(ActionEvent event) {
    	try {
			main.showHallOfFameMenu();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void openOptions(ActionEvent event) {
    	try {
			main.openOptionsMenu(main.getFont(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void startGame(ActionEvent event) {
    	try {
			main.startGame(main.getFont());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void initialize() {
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'mainMenuWindow.fxml'.";
        assert hallOfFameButton != null : "fx:id=\"hallOfFameButton\" was not injected: check your FXML file 'mainMenuWindow.fxml'.";
        assert optionsButton != null : "fx:id=\"optionsButton\" was not injected: check your FXML file 'mainMenuWindow.fxml'.";
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'mainMenuWindow.fxml'.";

    }
    void setMain(Main main) {
    	
    	try {
			this.main = main;
			Font newFont = Font.font(main.getFont().getFamily(), 35);
			exitButton.setFont(main.getFont());
			hallOfFameButton.setFont(main.getFont());
			optionsButton.setFont(main.getFont());
			startButton.setFont(main.getFont());
			titleText.setFont(newFont);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }

}
