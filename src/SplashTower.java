import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SplashTower extends Tower {
	
	public static final String imagePath = "file:sprites/splashtower.png";
	
	public SplashTower() {
		cost = 300;
		damage = 20;
		fireRate = 1;
		attackRadius = 7.0;
		splashDamage = true;
		testMonster(gameArea);
	}
	public SplashTower(Integer x, Integer y, Pane gameArea) {
		super(x, y, gameArea);
		image = new Image(imagePath);
		imageView = new ImageView(image);
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
		
		cost = 300;
		damage = 50;
		fireRate = 2;
		attackRadius = 7.0;
		splashDamage = true;
		
		attackRadiusVisual = new Circle();
		attackRadiusVisual.setRadius(attackRadius * 20);
		attackRadiusVisual.setCenterX((double)x + 25);
		attackRadiusVisual.setCenterY((double)y + 25);
		attackRadiusVisual.setFill(new Color(1,1,1,.3));
		
		testMonster(gameArea);
	}
	
	public void testMonster(Pane gameArea) {
		final Runnable shootProjectile = new Runnable() {
			@Override public void run() {
				ListIterator<Enemy> i = TowerDefenseAssignment4.GameController.getEnemyList().listIterator(); 
				
				while (i.hasNext()) {
					Enemy e = i.next();
					
					if (e.imageView.getBoundsInParent().intersects(attackRadiusVisual.getBoundsInParent())) {
						Bounds enemyBounds = e.imageView.localToParent(e.imageView.getBoundsInParent());
						double enemyX = enemyBounds.getMinX();
						double enemyY = enemyBounds.getMinY();
						/*p = new Projectile((int)imageView.getX(), (int)imageView.getY(), 
								(int)enemyX, (int)enemyY, 
								TowerDefenseAssignment4.projectileImage);*/
						
						// Change values of the game based on the state of the enemy (Dead = money to user, alive = take damage)
						e.receiveDamage(damage);
						System.out.println("Enemy" + (i.nextIndex()-1) + " took " + damage + " damage! Enemy has " + e.health + " life left.");
						if (e.health <= 0) { // When it is the last health point of the enemy, it dies
							TowerDefenseAssignment4.GameController.getEnemyList().remove(i.nextIndex() - 1);
							Platform.runLater(new Runnable() {
								@Override public void run() {
									TowerDefenseAssignment4.GameController.addMoney(50);
								}
							});							if (TowerDefenseAssignment4.GameController.getEnemyList().size() == 0) { // When it is the last enemy of the round
								TowerDefenseAssignment4.GameController.inRound = false;
								if (TowerDefenseAssignment4.GameController.getRound() == 5) { // when round 5 finishes, you win
									Platform.runLater(new Runnable() {
										@Override public void run() {
											TowerDefenseAssignment4.GameController.displayWinMessage();
										}
									});
								}
							}
						}
					
						Platform.runLater(new Runnable() {
							@Override public void run() {
								//gameArea.getChildren().add(p.imageView);
								TowerDefenseAssignment4.gameController.createHitMarker(e, damage, i.nextIndex() - 1);
							}
						});
						/*
						PathTransition pt = new PathTransition();
						Path path = new Path();
						path.getElements().add( new MoveTo( p.getFromX(), p.getFromY() ) );
						path.getElements().add( new LineTo( p.getToX(), p.getToY() ) );
						pt.setDuration(Duration.millis(500));
						pt.setNode(p.imageView);
						pt.setPath(path);
						pt.play();
						System.out.println("created and executed animation");
						
						pt.setOnFinished( new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent event) {
									System.out.println("finished animation");
									gameArea.getChildren().remove(p.imageView);
									e.receiveDamage(damage);
								}
						});*/
					}		
				}
			}
		};
		testMonsterHandler = executor.scheduleWithFixedDelay(shootProjectile, 500, (long) (1000 / fireRate), TimeUnit.MILLISECONDS);
	}
}
