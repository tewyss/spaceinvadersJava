package spaceinvaders;

import javafx.scene.image.Image;
import lombok.Getter;

@Getter
public class Ship extends GameObject implements MoveOnX {

	public Ship(String imagePath, double x, double y, double height, double width) {
		super(x, y, height, width);
		initializeImage(imagePath);
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setY(double y) {
		// no-op
	}

	@Override
	public void setX(double x) {
		this.x = x;
		imageView.setLayoutX(x);
	}

	public void setImage(String imagePath) {
		imageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
	}
}
