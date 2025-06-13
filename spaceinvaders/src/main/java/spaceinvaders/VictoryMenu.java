package spaceinvaders;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class VictoryMenu extends BaseMenu {

    public VictoryMenu(Font font, int currentScore) {
        addTitle(bundle.getString("victory_title"), font, Color.rgb(255, 236, 0));
        addScore(bundle.getString("your_score"), currentScore, font);
        askPlayerNameAndSaveScore(currentScore);

        createMenuItem(bundle.getString("exit_to_main"), font);
        createMenuItem(bundle.getString("exit_game"), font);

        updateMenuSelection();
    }
}
