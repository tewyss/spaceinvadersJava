package spaceinvaders;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameOverMenu extends BaseMenu {
    public GameOverMenu(Font font, int currentScore) {

        addTitle(bundle.getString("game_over"), font, Color.RED);
        addScore(bundle.getString("your_score"), currentScore, font);
        askPlayerNameAndSaveScore(currentScore);

        createMenuItem(bundle.getString("retry"), font);
        createMenuItem(bundle.getString("exit_to_main"), font);
        createMenuItem(bundle.getString("exit_game"), font);

        updateMenuSelection();
    }
}
