package spaceinvaders;

public class Alien extends GameObject implements MoveOnXY {

    public Alien(String imagePath, double x, double y, double height, double width) {
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
    public void setX(double x) {
        this.x = x;
        imageView.setLayoutX(x);
    }

    @Override
    public void setY(double y) {
        this.y = y;
        imageView.setLayoutY(y);
    }
}