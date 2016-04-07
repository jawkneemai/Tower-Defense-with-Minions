//
// Johnathan Mai
// ITP368
// Assignment 2
//

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.scene.Group;
import java.util.Random;
import javafx.animation.*;
import javafx.animation.Animation.Status;
import javafx.animation.PathTransition.*;
import java.lang.Number;
import javafx.event.*;

public class TowerDefenseAssignment2 extends Application {

	private int sceneWidth = 350;
	private int sceneHeight = 350;
	
	GameController gameController;
	
	public static void main( String[] args ) {
		Application.launch( args );
	}
	
	public void start( Stage primaryStage ) {
		Pane pane = new Pane();
		
		gameController = new GameController(pane);
		
		// Directional Keys Listener
		pane.setOnMouseClicked ( e -> {
			GameController.createUserPath(GameController.user, 
								GameController.user.getX(), 
								GameController.user.getY(), 
								e.getX(), 
								e.getY());
			GameController.user.setX(e.getX());
			GameController.user.setY(e.getY());
		});		
		
		// Scene set up
		Scene scene = new Scene( pane, sceneWidth, sceneHeight );
		primaryStage.setScene( scene );
		primaryStage.show();

		
		// Adding animations to enemies
		for (int i = 0; i < GameController.enemyList.length; i++) {
			GameController.createPath( GameController.enemyList[i], 
					GameController.enemyList[i].getX(), 
					GameController.enemyList[i].getY() );
		}
		
		
	}
	
	private static class GameController {
		public static final Tower[] towerList = new Tower[10]; // Holds all stationary towers
		public static final Enemy[] enemyList = new Enemy[10]; // Holds all moving enemies
		
		// Grid will be 20x20 50 pixel tiles, so overall map is 1000x1000 pixels. Window is 350x350.
		private static final int gridColumns = 20;
		private static final int gridRows = 20;
		public static final Landscape[][] grid = new Landscape[gridColumns][gridRows];
		public static final Group gridGroup = new Group(); // displays the grid
		
		// User Class
		public static User user;
		
		javafx.scene.image.Image imageGrassTile;
		public static final String grassImagePath = "file:sprites/grass2.png";
		
		GameController(Pane pane) {
			
			// Creating Background
			// creates base layer of grass.
			imageGrassTile = new javafx.scene.image.Image(grassImagePath);
			for (int i=0; i < gridRows; i++) {
				for (int j = 0; j < gridColumns; j++) {
					Grass node = new Grass( (50 * i) + 25, (50 * j) + 25, imageGrassTile);
					gridGroup.getChildren().add( node.imageView );
					grid[i][j] = node;
				}
			}		
			pane.getChildren().add(gridGroup);

			
			// Draws everything on screen (Gravel Path, Random Towers, Enemy NPCs)
			drawGravelPath();	
			drawRandomTowers();
			for (int i = 0; i < GameController.towerList.length; i++) {
				pane.getChildren().add(GameController.towerList[i].imageView);
			}
			drawNPCs();
			for (int i = 0; i < GameController.enemyList.length; i++) {
				pane.getChildren().add(GameController.enemyList[i].imageView);
			}
			
			// User Control
			GameController.user = new User( grid[0][0].getX(), grid[0][0].getY());
			pane.getChildren().add(GameController.user.imageView);
			
		}
		
		GameController() {}
		
		// This creates the gravel path for enemies to travel on.. Need to find a better way in the future.
		private void drawGravelPath() {
			gridGroup.getChildren().set(2, new Gravel(grid[0][2].getX(), grid[0][2].getY()).tile);
			gridGroup.getChildren().set(22, new Gravel(grid[1][2].getX(), grid[1][2].getY()).tile);
			gridGroup.getChildren().set(42, new Gravel(grid[2][2].getX(), grid[2][2].getY()).tile);
			gridGroup.getChildren().set(62, new Gravel(grid[3][2].getX(), grid[3][2].getY()).tile);
			gridGroup.getChildren().set(82, new Gravel(grid[4][2].getX(), grid[4][2].getY()).tile);
			gridGroup.getChildren().set(102, new Gravel(grid[5][2].getX(), grid[5][2].getY()).tile);
			gridGroup.getChildren().set(122, new Gravel(grid[6][2].getX(), grid[6][2].getY()).tile);
			gridGroup.getChildren().set(142, new Gravel(grid[7][2].getX(), grid[7][2].getY()).tile);
			gridGroup.getChildren().set(162, new Gravel(grid[8][2].getX(), grid[8][2].getY()).tile);
			gridGroup.getChildren().set(182, new Gravel(grid[9][2].getX(), grid[9][2].getY()).tile);
			gridGroup.getChildren().set(202, new Gravel(grid[10][2].getX(), grid[10][2].getY()).tile);
			gridGroup.getChildren().set(222, new Gravel(grid[11][2].getX(), grid[11][2].getY()).tile);
			gridGroup.getChildren().set(242, new Gravel(grid[12][2].getX(), grid[12][2].getY()).tile);
			gridGroup.getChildren().set(262, new Gravel(grid[13][2].getX(), grid[13][2].getY()).tile);
			gridGroup.getChildren().set(282, new Gravel(grid[14][2].getX(), grid[14][2].getY()).tile);
			gridGroup.getChildren().set(302, new Gravel(grid[15][2].getX(), grid[15][2].getY()).tile);
			gridGroup.getChildren().set(322, new Gravel(grid[16][2].getX(), grid[16][2].getY()).tile);
			gridGroup.getChildren().set(342, new Gravel(grid[17][2].getX(), grid[17][2].getY()).tile);
			gridGroup.getChildren().set(362, new Gravel(grid[18][2].getX(), grid[18][2].getY()).tile);
			gridGroup.getChildren().set(382, new Gravel(grid[19][2].getX(), grid[19][2].getY()).tile);
			
			gridGroup.getChildren().set(383, new Gravel(grid[19][3].getX(), grid[19][3].getY()).tile);
			gridGroup.getChildren().set(384, new Gravel(grid[19][4].getX(), grid[19][4].getY()).tile);
			
			gridGroup.getChildren().set(385, new Gravel(grid[19][5].getX(), grid[19][5].getY()).tile);
			gridGroup.getChildren().set(365, new Gravel(grid[18][5].getX(), grid[18][5].getY()).tile);
			gridGroup.getChildren().set(345, new Gravel(grid[17][5].getX(), grid[17][5].getY()).tile);
			gridGroup.getChildren().set(325, new Gravel(grid[16][5].getX(), grid[16][5].getY()).tile);
			gridGroup.getChildren().set(305, new Gravel(grid[15][5].getX(), grid[15][5].getY()).tile);
			gridGroup.getChildren().set(285, new Gravel(grid[14][5].getX(), grid[14][5].getY()).tile);
			gridGroup.getChildren().set(265, new Gravel(grid[13][5].getX(), grid[13][5].getY()).tile);
			gridGroup.getChildren().set(245, new Gravel(grid[12][5].getX(), grid[12][5].getY()).tile);
			gridGroup.getChildren().set(225, new Gravel(grid[11][5].getX(), grid[11][5].getY()).tile);
			gridGroup.getChildren().set(205, new Gravel(grid[10][5].getX(), grid[10][5].getY()).tile);
			gridGroup.getChildren().set(185, new Gravel(grid[9][5].getX(), grid[9][5].getY()).tile);
			gridGroup.getChildren().set(165, new Gravel(grid[8][5].getX(), grid[8][5].getY()).tile);
			gridGroup.getChildren().set(145, new Gravel(grid[7][5].getX(), grid[7][5].getY()).tile);
			gridGroup.getChildren().set(125, new Gravel(grid[6][5].getX(), grid[6][5].getY()).tile);
			gridGroup.getChildren().set(105, new Gravel(grid[5][5].getX(), grid[5][5].getY()).tile);
			gridGroup.getChildren().set(85, new Gravel(grid[4][5].getX(), grid[4][5].getY()).tile);
			gridGroup.getChildren().set(65, new Gravel(grid[3][5].getX(), grid[3][5].getY()).tile);
			gridGroup.getChildren().set(45, new Gravel(grid[2][5].getX(), grid[2][5].getY()).tile);
			gridGroup.getChildren().set(25, new Gravel(grid[1][5].getX(), grid[1][5].getY()).tile);
			
			gridGroup.getChildren().set(26, new Gravel(grid[1][6].getX(), grid[1][6].getY()).tile);
			gridGroup.getChildren().set(27, new Gravel(grid[1][7].getX(), grid[1][7].getY()).tile);

			gridGroup.getChildren().set(28, new Gravel(grid[1][8].getX(), grid[1][8].getY()).tile);
			gridGroup.getChildren().set(48, new Gravel(grid[2][8].getX(), grid[2][8].getY()).tile);
			gridGroup.getChildren().set(68, new Gravel(grid[3][8].getX(), grid[3][8].getY()).tile);
			gridGroup.getChildren().set(88, new Gravel(grid[4][8].getX(), grid[4][8].getY()).tile);
			gridGroup.getChildren().set(108, new Gravel(grid[5][8].getX(), grid[5][8].getY()).tile);
			gridGroup.getChildren().set(128, new Gravel(grid[6][8].getX(), grid[6][8].getY()).tile);

			gridGroup.getChildren().set(129, new Gravel(grid[6][9].getX(), grid[6][9].getY()).tile);
			gridGroup.getChildren().set(130, new Gravel(grid[6][10].getX(), grid[6][10].getY()).tile);

			gridGroup.getChildren().set(131, new Gravel(grid[6][11].getX(), grid[6][11].getY()).tile);
			gridGroup.getChildren().set(111, new Gravel(grid[5][11].getX(), grid[5][11].getY()).tile);
			gridGroup.getChildren().set(91, new Gravel(grid[4][11].getX(), grid[4][11].getY()).tile);
			gridGroup.getChildren().set(71, new Gravel(grid[3][11].getX(), grid[3][11].getY()).tile);
			gridGroup.getChildren().set(51, new Gravel(grid[2][11].getX(), grid[2][11].getY()).tile);
			gridGroup.getChildren().set(31, new Gravel(grid[1][11].getX(), grid[1][11].getY()).tile);

			gridGroup.getChildren().set(32, new Gravel(grid[1][12].getX(), grid[1][12].getY()).tile);
			gridGroup.getChildren().set(33, new Gravel(grid[1][13].getX(), grid[1][13].getY()).tile);
			gridGroup.getChildren().set(34, new Gravel(grid[1][14].getX(), grid[1][14].getY()).tile);

			gridGroup.getChildren().set(35, new Gravel(grid[1][15].getX(), grid[1][15].getY()).tile);
			gridGroup.getChildren().set(55, new Gravel(grid[2][15].getX(), grid[2][15].getY()).tile);
			gridGroup.getChildren().set(75, new Gravel(grid[3][15].getX(), grid[3][15].getY()).tile);
			gridGroup.getChildren().set(95, new Gravel(grid[4][15].getX(), grid[4][15].getY()).tile);
			gridGroup.getChildren().set(115, new Gravel(grid[5][15].getX(), grid[5][15].getY()).tile);
			gridGroup.getChildren().set(135, new Gravel(grid[6][15].getX(), grid[6][15].getY()).tile);
			gridGroup.getChildren().set(155, new Gravel(grid[7][15].getX(), grid[7][15].getY()).tile);
			gridGroup.getChildren().set(175, new Gravel(grid[8][15].getX(), grid[8][15].getY()).tile);


			gridGroup.getChildren().set(174, new Gravel(grid[8][14].getX(), grid[8][14].getY()).tile);
			gridGroup.getChildren().set(173, new Gravel(grid[8][13].getX(), grid[8][13].getY()).tile);
			gridGroup.getChildren().set(172, new Gravel(grid[8][12].getX(), grid[8][12].getY()).tile);
			gridGroup.getChildren().set(171, new Gravel(grid[8][11].getX(), grid[8][11].getY()).tile);
			gridGroup.getChildren().set(170, new Gravel(grid[8][10].getX(), grid[8][10].getY()).tile);
			gridGroup.getChildren().set(169, new Gravel(grid[8][9].getX(), grid[8][9].getY()).tile);
			gridGroup.getChildren().set(168, new Gravel(grid[8][8].getX(), grid[8][8].getY()).tile);

			gridGroup.getChildren().set(167, new Gravel(grid[8][7].getX(), grid[8][7].getY()).tile);
			gridGroup.getChildren().set(187, new Gravel(grid[9][7].getX(), grid[9][7].getY()).tile);
			gridGroup.getChildren().set(207, new Gravel(grid[10][7].getX(), grid[10][7].getY()).tile);

			gridGroup.getChildren().set(208, new Gravel(grid[10][8].getX(), grid[10][8].getY()).tile);
			gridGroup.getChildren().set(209, new Gravel(grid[10][9].getX(), grid[10][9].getY()).tile);
			gridGroup.getChildren().set(210, new Gravel(grid[10][10].getX(), grid[10][10].getY()).tile);
			gridGroup.getChildren().set(211, new Gravel(grid[10][11].getX(), grid[10][11].getY()).tile);
			gridGroup.getChildren().set(212, new Gravel(grid[10][12].getX(), grid[10][12].getY()).tile);
			gridGroup.getChildren().set(213, new Gravel(grid[10][13].getX(), grid[10][13].getY()).tile);
			gridGroup.getChildren().set(214, new Gravel(grid[10][14].getX(), grid[10][14].getY()).tile);
			gridGroup.getChildren().set(215, new Gravel(grid[10][15].getX(), grid[10][15].getY()).tile);
			gridGroup.getChildren().set(216, new Gravel(grid[10][16].getX(), grid[10][16].getY()).tile);
			
			gridGroup.getChildren().set(217, new Gravel(grid[10][17].getX(), grid[10][17].getY()).tile);
			gridGroup.getChildren().set(197, new Gravel(grid[9][17].getX(), grid[9][17].getY()).tile);
			gridGroup.getChildren().set(177, new Gravel(grid[8][17].getX(), grid[8][17].getY()).tile);
			gridGroup.getChildren().set(157, new Gravel(grid[7][17].getX(), grid[7][17].getY()).tile);
			gridGroup.getChildren().set(137, new Gravel(grid[6][17].getX(), grid[6][17].getY()).tile);
			gridGroup.getChildren().set(117, new Gravel(grid[5][17].getX(), grid[5][17].getY()).tile);
			gridGroup.getChildren().set(97, new Gravel(grid[4][17].getX(), grid[4][17].getY()).tile);
			gridGroup.getChildren().set(77, new Gravel(grid[3][17].getX(), grid[3][17].getY()).tile);
			gridGroup.getChildren().set(57, new Gravel(grid[2][17].getX(), grid[2][17].getY()).tile);
			gridGroup.getChildren().set(37, new Gravel(grid[1][17].getX(), grid[1][17].getY()).tile);

			gridGroup.getChildren().set(38, new Gravel(grid[1][18].getX(), grid[1][18].getY()).tile);
			
			gridGroup.getChildren().set(39, new Gravel(grid[1][19].getX(), grid[1][19].getY()).tile);
			gridGroup.getChildren().set(59, new Gravel(grid[2][19].getX(), grid[2][19].getY()).tile);
			gridGroup.getChildren().set(79, new Gravel(grid[3][19].getX(), grid[3][19].getY()).tile);
			gridGroup.getChildren().set(99, new Gravel(grid[4][19].getX(), grid[4][19].getY()).tile);
			gridGroup.getChildren().set(119, new Gravel(grid[5][19].getX(), grid[5][19].getY()).tile);
			gridGroup.getChildren().set(139, new Gravel(grid[6][19].getX(), grid[6][19].getY()).tile);
			gridGroup.getChildren().set(159, new Gravel(grid[7][19].getX(), grid[7][19].getY()).tile);
			gridGroup.getChildren().set(179, new Gravel(grid[8][19].getX(), grid[8][19].getY()).tile);
			gridGroup.getChildren().set(199, new Gravel(grid[9][19].getX(), grid[9][19].getY()).tile);
			gridGroup.getChildren().set(219, new Gravel(grid[10][19].getX(), grid[10][19].getY()).tile);
			gridGroup.getChildren().set(239, new Gravel(grid[11][19].getX(), grid[11][19].getY()).tile);
			gridGroup.getChildren().set(259, new Gravel(grid[12][19].getX(), grid[12][19].getY()).tile);
			gridGroup.getChildren().set(279, new Gravel(grid[13][19].getX(), grid[13][19].getY()).tile);

			gridGroup.getChildren().set(279, new Gravel(grid[13][19].getX(), grid[13][19].getY()).tile);
			gridGroup.getChildren().set(278, new Gravel(grid[13][18].getX(), grid[13][18].getY()).tile);
			gridGroup.getChildren().set(277, new Gravel(grid[13][17].getX(), grid[13][17].getY()).tile);
			gridGroup.getChildren().set(276, new Gravel(grid[13][16].getX(), grid[13][16].getY()).tile);
			gridGroup.getChildren().set(275, new Gravel(grid[13][15].getX(), grid[13][15].getY()).tile);
			gridGroup.getChildren().set(274, new Gravel(grid[13][14].getX(), grid[13][14].getY()).tile);
			gridGroup.getChildren().set(273, new Gravel(grid[13][13].getX(), grid[13][13].getY()).tile);
			gridGroup.getChildren().set(272, new Gravel(grid[13][12].getX(), grid[13][12].getY()).tile);
			gridGroup.getChildren().set(271, new Gravel(grid[13][11].getX(), grid[13][11].getY()).tile);
		}
		
		// Draws random stationary towers.
		private void drawRandomTowers() {
			towerList[0] = new Base(700, 500);
			towerList[1] = new RapidTower(100, 50);
			towerList[2] = new RapidTower(350,650);
			towerList[3] = new RapidTower(700, 200);
			towerList[4] = new SniperTower(750, 800);
			towerList[5] = new SniperTower(500, 200);
			towerList[6] = new SniperTower(450, 450);
			towerList[7] = new SplashTower(900, 50);
			towerList[8] = new SplashTower(50, 800);
			towerList[9] = new SplashTower(0, 450);
		}
		
		// Draws random enemy sprites to wander around
		private void drawNPCs() {
			Random r = new Random();
			for (int i = 0; i < enemyList.length; i++) {
				int index = r.nextInt(3);
				switch (index) {
					case 0:
						enemyList[i] = new SmallEnemy(500, 500);
						break;
					case 1:
						enemyList[i] = new MediumEnemy(500,500);
						break;
					case 2:
						enemyList[i] = new BossEnemy(100, 100);
						break;
					default:
						break;
				}
			}
		}
		
		// Creates a random path for enemies
		public static void createPath( Enemy e, int oldX, int oldY ) {
			Random r = new Random();	
			
			// generate new (x,y) values for the enemy to go to. 50x multiplier to give span of entire "world"	
			int newX = r.nextInt(gridColumns * 50);
			int newY = r.nextInt(gridRows * 50);
			
			// path transition
			PathTransition patht = new PathTransition();
			Path path = new Path();

		    path.getElements().add (new MoveTo (oldX, oldY));
		    path.getElements().add (new LineTo (newX, newY));
			patht.setDuration(Duration.millis(5000));
			patht.setNode(e.imageView);
			patht.setPath(path);
			
			// rotate transition
			RotateTransition rt = new RotateTransition( Duration.millis(100), e.imageView );
			rt.setFromAngle(-5);
			rt.setByAngle(10);
			rt.setCycleCount(Timeline.INDEFINITE);
			rt.setAutoReverse(true);
			
			// parallel transition to put above two together:
			ParallelTransition pt = new ParallelTransition( patht, rt );
			pt.play();
			
			// Repeat animation after finished.
			patht.setOnFinished( new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if ( patht.getStatus() == Status.RUNNING) {
							return;
						} else {
							createPath(e, newX, newY ); // send new coordinates as next old coordinates
						}
					}
			});
			
			
		}
		
		// Creates a path for the user every mouse click
		public static void createUserPath( User u, double oldX, double oldY, double newX, double newY) {
			// path transition
			PathTransition patht = new PathTransition();
			Path path = new Path();
			
			path.getElements().add (new MoveTo (oldX, oldY));
		    path.getElements().add (new LineTo (newX, newY));
			patht.setDuration(Duration.millis(2000));
			patht.setNode(u.imageView);
			patht.setPath(path);
			
			// rotate transition
			RotateTransition rt = new RotateTransition( Duration.millis(100), u.imageView );
			rt.setFromAngle(-5);
			rt.setByAngle(10);
			rt.setCycleCount(Timeline.INDEFINITE);
			rt.setAutoReverse(true);
			
			// parallel transition to put above two together:
			ParallelTransition pt = new ParallelTransition( patht, rt );
			pt.play();
			
			patht.setOnFinished( new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if ( patht.getStatus() == Status.RUNNING) {
						return;
					} else {
						pt.stop();
					}
				}
			});
		}	

		
	}
}


