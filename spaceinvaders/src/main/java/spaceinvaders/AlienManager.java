package spaceinvaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlienManager {
	private List<Alien> aliens;
	private boolean movingRight = true;
	private Pane gameLayout;
	private Random random = new Random();
	private static final double ALIEN_MOVE_SPEED = 15;
	private static final double ALIEN_MOVE_DOWN_DISTANCE = 10;
	private static final double ALIEN_LEFT_BOUNDARY = 0;
	private static final double ALIEN_RIGHT_BOUNDARY = 740;
	private Game game;
	private List<Bullet> alienBullets;


	public AlienManager(Pane gameLayout, Game game) {
		this.aliens = new ArrayList<>();
		this.gameLayout = gameLayout;
		this.game = game;
		this.alienBullets = game.getAlienBullets();
	}


	public void createAliens() {
		int rows = 4;
		int cols = 8;
		double alienWidth = 60;
		double alienHeight = 60;
		double spacing = 25;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				double centerX = 85 + col * (alienWidth + spacing);
				double centerY = 85 + row * (alienHeight + spacing);
				Alien alien = new Alien("/images/alien.gif", centerX - (alienWidth / 2), centerY - (alienWidth / 2),
						alienWidth, alienHeight);
				aliens.add(alien);
				gameLayout.getChildren().add(alien.getImageView());
			}
		}
	}

	public void updateAliens(double deltaTime) {
		boolean boundaryReached = checkBoundaries(deltaTime);

		if (boundaryReached) {
			moveAliensDown();
		} else {
			updateAlienPositions(deltaTime);
		}
	}

	private boolean checkBoundaries(double deltaTime) {
		double currentAlienSpeed = ALIEN_MOVE_SPEED + (ALIEN_MOVE_SPEED * (1.0 - (aliens.size() / 32.0)) * 15);
		if (aliens.size() == 1) {
			currentAlienSpeed = 750;
		}

		for (Alien alien : aliens) {
			double deltaX = currentAlienSpeed * deltaTime;
			double newX = alien.getX() + (movingRight ? deltaX : -deltaX);

			if (newX >= ALIEN_RIGHT_BOUNDARY || newX <= ALIEN_LEFT_BOUNDARY) {
				return true;
			}
		}
		return false;
	}

	private void moveAliensDown() {
		movingRight = !movingRight;
		for (Alien alien : aliens) {
			alien.setY(alien.getY() + ALIEN_MOVE_DOWN_DISTANCE);

			if (alien.getY() >= 460) {
				game.endGame(false);
				break;
			}
		}
	}

	private void updateAlienPositions(double deltaTime) {
		double currentAlienSpeed = ALIEN_MOVE_SPEED + (ALIEN_MOVE_SPEED * (1.0 - (aliens.size() / 32.0)) * 15);
		if (aliens.size() == 1) {
			currentAlienSpeed = 750;
		}

		for (Alien alien : aliens) {
			double deltaX = currentAlienSpeed * deltaTime;
			double newX = alien.getX() + (movingRight ? deltaX : -deltaX);
			alien.setX(newX);
		}
	}

	public void removeAlien(Alien alien) {
		aliens.remove(alien);
		gameLayout.getChildren().remove(alien.getImageView());
		log.info("ALien byl znicen!");
	}

	public List<Alien> getAliens() {
		return aliens;
	}

	public boolean areAllAliensDead() {
		return aliens.isEmpty();
	}

	public void checkAlienCollisions(List<Bullet> bullets) {
		List<Bullet> bulletsCopy = new ArrayList<>(bullets);

		for (Bullet bullet : bulletsCopy) {
			Bounds bulletBounds = bullet.getImageView().getBoundsInParent();
			Bounds bulletHitbox = new BoundingBox(bulletBounds.getMinX() + 5, bulletBounds.getMinY() + 5,
					bulletBounds.getWidth() - 10, 50);

			List<Alien> aliensCopy = new ArrayList<>(aliens);
			for (Alien alien : aliensCopy) {
				Bounds alienBounds = alien.getImageView().getBoundsInParent();
				Bounds alienHitbox = new BoundingBox(alienBounds.getMinX() + 10, alienBounds.getMinY(), 40, 40);

				if (bulletHitbox.intersects(alienHitbox)) {
					gameLayout.getChildren().remove(bullet.getImageView());
					gameLayout.getChildren().remove(alien.getImageView());
					bullets.remove(bullet);
					removeAlien(alien);
					game.updateScore(100);
					game.playExplosion(alien.getX(), alien.getY());
					break;
				}
			}
		}
	}

	public void alienFireBullet() {
		if (aliens.isEmpty())
			return;

		Alien shooter = aliens.get(random.nextInt(aliens.size()));
		Bullet alienBullet = new Bullet("/images/alien_projectile.gif",
				shooter.getImageView().getLayoutX() + shooter.getImageView().getFitWidth() / 2 - 15,
				shooter.getImageView().getLayoutY() + shooter.getImageView().getFitHeight(), 30, 50);
		alienBullets.add(alienBullet);
		gameLayout.getChildren().add(alienBullet.getImageView());
	}

	public void updateAlienBullets(double deltaTime) {
		Iterator<Bullet> alienBulletIterator = alienBullets.iterator();
		while (alienBulletIterator.hasNext()) {
			Bullet alienBullet = alienBulletIterator.next();
			alienBullet.moveDown(deltaTime);

			if (alienBullet.getY() > 600) {
				gameLayout.getChildren().remove(alienBullet.getImageView());
				alienBulletIterator.remove();
			}
		}
	}

}
