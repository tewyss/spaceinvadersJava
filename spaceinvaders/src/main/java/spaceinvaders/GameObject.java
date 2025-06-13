package spaceinvaders;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

@Getter
public abstract class GameObject {
	protected ImageView imageView;
	protected double x;
	protected double y;
	protected final double height;
	protected final double width;

	protected GameObject(double x, double y, double height, double width) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	protected void initializeImage(String imagePath) {
		Image image = new Image(getClass().getResourceAsStream(imagePath));
		imageView = new ImageView(image);
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		imageView.setLayoutX(x);
		imageView.setLayoutY(y);
		imageView.setPreserveRatio(true);
	}

	public abstract void setX(double x);

	public abstract double getX();

	public abstract void setY(double y);

	public abstract double getY();
}
