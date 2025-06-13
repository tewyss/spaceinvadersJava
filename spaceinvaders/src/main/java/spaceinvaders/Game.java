package spaceinvaders;

import java.util.*;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class Game {
	private Pane gameLayout;
	private long lastTime;
	private Set<String> pressedKeys = new HashSet<>();
	private String pathToExplodeSound = "/sounds/explode.mp3";
	private Text scoreValue;
	private int score = 0;
	private boolean isPaused = false;
	private boolean isResumed = false;
	private long pausedTime = 0;

	private List<ImageView> lifeIcons;
	private int lives = 3;

	private List<Alien> aliens;
	private List<Bullet> bullets;
	private List<Bullet> alienBullets;

	private AudioClip explodeSound;

	private static final long ALIEN_SHOOT_INTERVAL = 2000000000;
	private long lastAlienShotTime = 0;
	private Main mainApp;
	private AnimationTimer animationTimer;
	private AlienManager alienManager;
	private ShipManager shipManager;
	private boolean isGameOver = false;
	protected ResourceBundle bundle;

	public Game(Main mainApp) {
		this.mainApp = mainApp;
		isGameOver = false;
		gameLayout = new Pane();
		bullets = new ArrayList<>();
		shipManager = new ShipManager(gameLayout, bullets);
		alienBullets = new ArrayList<>();
		alienManager = new AlienManager(gameLayout, this);
		lastTime = System.nanoTime();
		aliens = new ArrayList<>();
		bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

		lifeIcons = new ArrayList<>();
		gameLayout.setStyle("-fx-background-color: black;");

		Font myFont = Font.loadFont(getClass().getResourceAsStream("/fonts/myFont.ttf"), 15);
		explodeSound = new AudioClip(getClass().getResource(pathToExplodeSound).toExternalForm());

		alienManager.createAliens();
		createScore(myFont);
		createLives(myFont);
		createEndLine();

		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
			}
		};
		animationTimer.start();
	}

	public void update() {
		if (isPaused) {
			pausedTime = System.nanoTime();
			return;
		}

		double deltaTime = calculateDeltaTime();

		if (pausedTime != 0) {
			long pauseDuration = System.nanoTime() - pausedTime;
			lastTime += pauseDuration;
			pausedTime = 0;
		}
		if (isResumed) {
			lastAlienShotTime = System.nanoTime();
			isResumed = false;
			return;
		}

		long currentAlienShootInterval = ALIEN_SHOOT_INTERVAL
				- (long) ((ALIEN_SHOOT_INTERVAL * 0.8) * (1.0 - (alienManager.getAliens().size() / 32.0)));

		if (System.nanoTime() - lastAlienShotTime >= currentAlienShootInterval) {
			alienManager.alienFireBullet();
			lastAlienShotTime = System.nanoTime();
		}

		if (alienManager.areAllAliensDead()) {
			endGame(true);
			return;
		}

		updateShipMovement(deltaTime);
		alienManager.updateAliens(deltaTime);
		shipManager.updateBullets(deltaTime);
		alienManager.updateAlienBullets(deltaTime);
		checkCollision();
	}

	public void setKeyPressed(boolean press, String key) {
		if (press) {
			pressedKeys.add(key);
		} else {
			pressedKeys.remove(key);
		}
	}

	public void checkCollision() {
		if (isPaused)
			return;

		alienManager.checkAlienCollisions(bullets);
		if (shipManager.checkShipCollision(alienBullets)) {

			updateLives(1);
			resetPlayerPosition();
		}
	}

	private void updateShipMovement(double deltaTime) {
		boolean moveRight = pressedKeys.contains("RIGHT");
		boolean moveLeft = pressedKeys.contains("LEFT");
		boolean fire = pressedKeys.contains("SPACE") || pressedKeys.contains("UP");
		shipManager.updateMovement(deltaTime, moveRight, moveLeft, fire);

	}

	public void playExplosion(double x, double y) {
		if (isPaused)
			return;

		Image image = new Image(getClass().getResourceAsStream("/images/explosion.gif"));
		ImageView explosion = new ImageView(image);

		explosion.setX(x);
		explosion.setY(y);

		gameLayout.getChildren().add(explosion);
		explodeSound.setVolume(Main.getSavedVolume());
		explodeSound.play();

		PauseTransition pause = new PauseTransition(Duration.millis(700));
		pause.setOnFinished(event -> gameLayout.getChildren().remove(explosion));
		pause.play();
	}

	private void createScore(Font customFont) {
		Text scoreLabel = new Text(bundle.getString("high_score") + ": ");
		scoreLabel.setFont(customFont);
		scoreLabel.setFill(Color.WHITE);

		scoreValue = new Text(String.valueOf(score));
		scoreValue.setFont(customFont);
		scoreValue.setFill(Color.rgb(30, 255, 0));

		TextFlow scoreDisplay = new TextFlow(scoreLabel, scoreValue);
		scoreDisplay.setLayoutX(20);
		scoreDisplay.setLayoutY(15);

		gameLayout.getChildren().add(scoreDisplay);
	}

	public void updateScore(int points) {
		score += points;
		scoreValue.setText(String.valueOf(score));
	}

	private void createLives(Font customFont) {
		Text livesLabel = new Text(bundle.getString("lives") + ": ");
		livesLabel.setFont(customFont);
		livesLabel.setFill(Color.WHITE);
		livesLabel.setLayoutX(575);
		livesLabel.setLayoutY(30);
		gameLayout.getChildren().add(livesLabel);

		for (int i = 0; i < lives; i++) {
			ImageView life = new ImageView(new Image(getClass().getResourceAsStream("/images/heart.png")));
			life.setFitWidth(30);
			life.setFitHeight(30);
			life.setX(675.0 + i * 35);
			life.setY(5);
			gameLayout.getChildren().add(life);
			lifeIcons.add(life);
		}
	}

	private void resetPlayerPosition() {
		shipManager.resetShipPosition();
		shipManager.clearBullets();
		alienBullets.forEach(bullet -> gameLayout.getChildren().remove(bullet.getImageView()));
	}

	public void updateLives(int livesLost) {
		lives -= livesLost;
		if (lives >= 0 && !lifeIcons.isEmpty()) {
			ImageView lifeToRemove = lifeIcons.get(lives);
			gameLayout.getChildren().remove(lifeToRemove);
			lifeIcons.remove(lifeToRemove);
		}

		if (lives <= 0) {
			endGame(false);
		}
	}

	public void createEndLine() {
		ImageView endLine = new ImageView(new Image(getClass().getResourceAsStream("/images/endline.png")));
		endLine.setFitWidth(800);
		endLine.setFitHeight(5);
		endLine.setLayoutX(0);
		endLine.setLayoutY(500);
		gameLayout.getChildren().add(endLine);
	}

	public void pause() {
		isPaused = true;
	}

	public void resume() {
		isPaused = false;
		lastTime = System.nanoTime();
		isResumed = true;
	}

	public double calculateDeltaTime() {
		long currentTime = System.nanoTime();
		double deltaTime = (double) (currentTime - lastTime) / 1000000000;
		lastTime = currentTime;
		return deltaTime;
	}

	public void clearPressedKeys() {
		pressedKeys.clear();
	}

	public Pane getGameLayout() {
		return gameLayout;
	}

	public void endGame(boolean flag) {
		if (isGameOver) {
			return;
		}
		isGameOver = true;
		if (animationTimer != null) {
			animationTimer.stop();
		}
		resetGame();
		Font myFont = Font.loadFont(getClass().getResourceAsStream("/fonts/myFont.ttf"), 20);
		if (flag) {
			mainApp.showVictoryOverMenu(myFont);
		} else {
			mainApp.showGameOverMenu(myFont);
		}

		score = 0;

	}

	public void stopGame() {
		if (animationTimer != null) {
			animationTimer.stop();
		}
		gameLayout.getChildren().clear();
	}

	public int getCurrentScore() {
		return score;
	}

	public List<Bullet> getAlienBullets() {
		return alienBullets;
	}

	public void resetGame() {
		isGameOver = false;
		aliens.clear();
		bullets.clear();
		alienBullets.clear();
		lives = 3;
		gameLayout.getChildren().clear();
	}
}
