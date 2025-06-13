package spaceinvaders;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class OptionsMenu extends BaseMenu {
    private Text volumeText;
    private double volumeLevel;

    public OptionsMenu(Font font) {
        volumeLevel = Main.getSavedVolume();

        addTitle("options", font, Color.BLUE); // klíč do .properties

        volumeText = new Text(bundle.getString("volume") + ": " + (int) (volumeLevel * 100) + "%");
        volumeText.setFont(font);
        volumeText.setFill(Color.GREY);
        menuLayout.getChildren().add(volumeText);

        createMenuItem(bundle.getString("return_esc"), font);
        updateVolumeText();
    }

    public void increaseVolume() {
        if (volumeLevel < 1.0) {
            volumeLevel = Math.min(1.0, Math.round((volumeLevel + 0.05) * 100.0) / 100.0);
        }
        updateVolumeText();
    }

    public void decreaseVolume() {
        if (volumeLevel > 0.0) {
            volumeLevel = Math.max(0.0, Math.round((volumeLevel - 0.05) * 100.0) / 100.0);
        }
        updateVolumeText();
    }

    private void updateVolumeText() {
        volumeText.setText(bundle.getString("volume") + ": " + (int) (volumeLevel * 100) + "%");
    }

    public double getVolumeLevel() {
        return volumeLevel;
    }
}
