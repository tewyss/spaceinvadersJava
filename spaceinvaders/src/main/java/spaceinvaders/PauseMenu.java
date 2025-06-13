package spaceinvaders;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PauseMenu extends BaseMenu {

    public PauseMenu(Font font) {

        addTitle(bundle.getString("pause_title"), font, Color.rgb(200, 35, 200));

        createMenuItem(bundle.getString("resume"), font);
        createMenuItem(bundle.getString("options"), font);
        createMenuItem(bundle.getString("exit_to_main"), font);
        createMenuItem(bundle.getString("exit_game"), font);

        updateMenuSelection();
    }
}
