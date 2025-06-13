package spaceinvaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipManager {
	private Ship ship;
	private Pane gameLayout;
	private List<Bullet> bullets;
	private AudioClip shootingSound;
	private static final long SHOOT_COOLDOWN = 650000000;
	private long lastShotTime = 0;
	private String currentShip = "";
	private boolean isShooting = false;

	private static final String PATH_TO_IDLE_SHIP = "/images/ship_idle.gif";
	private static final String PATH_TO_TILT_RIGHT_SHIP = "/images/ship_tilt_right.gif";
	private static final String PATH_TO_TILT_LEFT_SHIP = "/images/ship_tilt_left.gif";
	private static final String PATH_TO_SHOOT_SOUND = "/sounds/pewlaser.mp3";

	private static final double SHIP_MOVE_SPEED = 250;
	private static final double SHIP_LEFT_BOUNDARY = 0;
	private static final double SHIP_RIGHT_BOUNDARY = 725;

	public ShipManager(Pane gameLayout, List<Bullet> bullets) {
		this.gameLayout = gameLayout;
		this.bullets = bullets;
		this.shootingSound = new AudioClip(getClass().getResource(PATH_TO_SHOOT_SOUND).toExternalForm());
		createShip();
	}

	public void createShip() {
		ship = new Ship(PATH_TO_IDLE_SHIP, 380, 515, 75, 75);
		gameLayout.getChildren().add(ship.getImageView());
	}

	public void updateMovement(double deltaTime, boolean moveRight, boolean moveLeft, boolean fire) {
		log.trace("Hráč se pohnul na pozici X={}, Y={}", getShip().getX(), getShip().getY());

		if (moveRight) {
			moveShip(SHIP_MOVE_SPEED * deltaTime);
			if (!currentShip.equals(PATH_TO_TILT_RIGHT_SHIP)) {
				ship.setImage(PATH_TO_TILT_RIGHT_SHIP);
				currentShip = PATH_TO_TILT_RIGHT_SHIP;
			}
		} else if (moveLeft) {
			moveShip(-SHIP_MOVE_SPEED * deltaTime);
			if (!currentShip.equals(PATH_TO_TILT_LEFT_SHIP)) {
				ship.setImage(PATH_TO_TILT_LEFT_SHIP);
				currentShip = PATH_TO_TILT_LEFT_SHIP;
			}
		}

		if (fire) {
			fireBullet();
		}

		if (!moveRight && !moveLeft && !isShooting && !currentShip.equals(PATH_TO_IDLE_SHIP)) {
			ship.setImage(PATH_TO_IDLE_SHIP);
			currentShip = PATH_TO_IDLE_SHIP;
		}
	}

	private void moveShip(double deltaX) {
		double newX = ship.getImageView().getLayoutX() + deltaX;

		if (newX >= SHIP_LEFT_BOUNDARY && newX <= SHIP_RIGHT_BOUNDARY) {
			ship.setX(newX);
		}
	}

	public void fireBullet() {
		long currentTime = System.nanoTime();

		if (currentTime - lastShotTime >= SHOOT_COOLDOWN) {
			Bullet bullet = new Bullet("/images/projectile.gif",
					ship.getImageView().getLayoutX() + ship.getImageView().getFitWidth() / 2 - 15,
					ship.getImageView().getLayoutY(), 30, 50);
			bullets.add(bullet);
			gameLayout.getChildren().add(bullet.getImageView());

			lastShotTime = currentTime;

			isShooting = true;
			ship.setImage("/images/ship_shoot.gif");
			PauseTransition pause = new PauseTransition(Duration.millis(500));
			pause.setOnFinished(event -> {
				ship.setImage(PATH_TO_IDLE_SHIP);
				isShooting = false;
			});
			pause.play();

			shootingSound.setVolume(Main.getSavedVolume());
			shootingSound.play();
		}
	}

	public void resetShipPosition() {
		ship.setX(380);
		log.info("Lodicka dostala hit!");
	}

	public Ship getShip() {
		return ship;
	}

	public void updateBullets(double deltaTime) {
		Iterator<Bullet> bulletIterator = bullets.iterator();
		while (bulletIterator.hasNext()) {
			Bullet bullet = bulletIterator.next();
			bullet.moveUp(deltaTime);

			if (bullet.getY() < 0) {
				gameLayout.getChildren().remove(bullet.getImageView());
				bulletIterator.remove();
			}
		}
	}

	public boolean checkShipCollision(List<Bullet> alienBullets) {
		List<Bullet> alienBulletsCopy = new ArrayList<>(alienBullets);

		for (Bullet alienBullet : alienBulletsCopy) {
			Bounds alienBulletBounds = alienBullet.getImageView().getBoundsInParent();
			Bounds shipBounds = ship.getImageView().getBoundsInParent();
			Bounds shipHitbox = new BoundingBox(shipBounds.getMinX() + 10, shipBounds.getMinY(),
					shipBounds.getWidth() - 20, shipBounds.getHeight());

			if (alienBulletBounds.intersects(shipHitbox)) {
				gameLayout.getChildren().remove(alienBullet.getImageView());
				alienBullets.remove(alienBullet);
				return true;
			}
		}

		return false;
	}

	public void clearBullets() {
		bullets.forEach(bullet -> gameLayout.getChildren().remove(bullet.getImageView()));
		bullets.clear();
	}

}
