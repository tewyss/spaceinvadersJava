package spaceinvaders;

public class Bullet extends GameObject {
    private static final double BULLET_SPEED = 300;

    public Bullet(String imagePath, double x, double y, double width, double height) {
        super(x, y, height, width);
        initializeImage(imagePath);
    }

    public void moveUp(double deltaTime) {
        setY(getY() - BULLET_SPEED * deltaTime);
    }

    public void moveDown(double deltaTime) {
        setY(getY() + BULLET_SPEED * deltaTime);
    }

    @Override
    public void setX(double x) {
        this.x = x;
        imageView.setLayoutX(x);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
        imageView.setLayoutY(y);
    }

    @Override
    public double getY() {
        return y;
    }
}