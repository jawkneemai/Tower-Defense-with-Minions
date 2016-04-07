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
import javafx.scene.input.MouseEvent;
import javafx.geometry.*;
import javafx.scene.Group;
import java.util.Random;
import javafx.animation.*;
import javafx.animation.Animation.Status;
import javafx.animation.PathTransition.*;
import java.lang.Number;
import javafx.event.*;

public class TowerDefenseAssignment3 extends Application {

	// Game Settings
	private static final int sceneWidth = 1000;
	private static final int sceneHeight = 1000;
	
	GameController gameController;
	
	// Constants
	public static final String livesImagePath = "file:sprites/heartLives.png";
	public static final String moneyImagePath = "file:sprites/moneyCoins.png";
	
	public static void main( String[] args ) {
		Application.launch( args );
	}
	
	public void start( Stage primaryStage ) {
		BorderPane pane = new BorderPane();
		Pane gameArea = new Pane();
		HBox gameControlsPane = new HBox();
		HBox towerControlsPane = new HBox();
		
		// Setting up UI
		gameController = new GameController(gameArea); 
		gameController.createUI(pane, gameArea, gameControlsPane, towerControlsPane);
		RapidTower test = new RapidTower(500,500);
		SniperTower test2 = new SniperTower(200,600);
		SplashTower test3 = new SplashTower(400,400);
		gameArea.getChildren().addAll(test.imageView, 
													test2.imageView, 
													test3.imageView);
		//Rectangle clipper = new Rectangle(0, 0, 1000,1000);
		//gameArea.setClip(clipper);
		
		// Scene set up
		ScrollPane sp = new ScrollPane();
		sp.setContent(pane);
		Scene scene = new Scene( sp, sceneWidth, sceneHeight );
		primaryStage.setScene( scene );
		primaryStage.show();

		
		// Adding animations to enemies
		/*for (int i = 0; i < GameController.enemyList.length; i++) {
			GameController.createPath( GameController.enemyList[i], 
					GameController.enemyList[i].getX(), 
					GameController.enemyList[i].getY() );
		}*/		
	}
	
	protected static class GameController {
		
		// ------------- Variables ------------- 
		public static final Tower[] towerList = new Tower[10]; // Holds all stationary towers
		public static final Enemy[] enemyList = new Enemy[10]; // Holds all moving enemies
		
		// Grid will be 20x20 50 pixel tiles, so overall map is 1000x1000 pixels. Window is 350x350.
		private static final int gridColumns = 20;
		private static final int gridRows = 20;
		public static final Landscape[][] grid = new Landscape[gridColumns][gridRows];
		public static final Group gridGroup = new Group(); // displays the grid
		
		javafx.scene.image.Image imageGrassTile;
		public static final String grassImagePath = "file:sprites/grass2.png";
		final String buttonStyle = "-fx-border-color: #e2e2e2; -fx-border-width: 1.5;" 
				+ "-fx-background-radius: 0; "
				+ "-fx-background-color: #3D3D3D; -fx-font-size: 15pt; "
				+ "-fx-text-fill: #FFFFFF; -fx-background-insets: 0 0 0 0, 0, 1, 2";
		final String buttonStyleHover = "-fx-border-color: #e2e2e2; -fx-border-width: 1.5;" 
					+ "-fx-background-radius: 0; "
					+ "-fx-background-color: #FFFFFF; -fx-font-size: 15pt; "
					+ "-fx-text-fill: #3D3D3D; -fx-background-insets: 0 0 0 0, 0, 1, 2";
		
		protected Label labelLives;
		protected Label labelMoney;
		protected Integer lives = 50;
		protected Integer money = 1000;
		
		
		GameController(Pane pane) {
			
			labelLives = new Label();
			labelMoney = new Label();
			
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
		}
		
		protected void createUI(Pane borderPane, Pane gameArea, Pane gameControlsPane, Pane towerControlsPane) {
 			
			((BorderPane)borderPane).setCenter(gameArea);
			((BorderPane)borderPane).setTop(gameControlsPane);
			
			// Game Controls Panel
			
			HBox livesBox = new HBox();
			ImageView livesImageView = drawObject(livesImagePath);
			livesImageView.setFitHeight(25);
			livesImageView.setFitWidth(25);
			labelLives.setText(String.valueOf(lives));
			labelLives.setTextFill(Color.WHITE);
			labelLives.setFont(Font.font(null, FontWeight.BOLD, 50));
			livesBox.getChildren().addAll(livesImageView, labelLives);
			livesBox.setSpacing(10);
			livesBox.setAlignment(Pos.CENTER);
			
			HBox moneyBox = new HBox();
			ImageView moneyImageView = drawObject(moneyImagePath);
			moneyImageView.setFitHeight(35);
			moneyImageView.setFitWidth(35);
			labelMoney.setText(String.valueOf(money));
			labelMoney.setTextFill(Color.WHITE);
			labelMoney.setFont(Font.font(null, FontWeight.BOLD, 50));
			moneyBox.getChildren().addAll(moneyImageView, labelMoney);
			moneyBox.setSpacing(10);
			moneyBox.setAlignment(Pos.CENTER);
		
			// Start Round Button
			Button buttonStartRound = new Button("Start Round");
			buttonStartRound.setStyle(buttonStyle);
			buttonStartRound.addEventHandler(MouseEvent.MOUSE_PRESSED, new ButtonStyleHandlerPressed(buttonStartRound));
			buttonStartRound.addEventHandler(MouseEvent.MOUSE_RELEASED, new ButtonStyleHandlerReleased(buttonStartRound));

			Button buttonStartNewGame = new Button("New Game");
			buttonStartNewGame.setStyle(buttonStyle);
			buttonStartNewGame.addEventHandler(MouseEvent.MOUSE_PRESSED, new ButtonStyleHandlerPressed(buttonStartNewGame));
			buttonStartNewGame.addEventHandler(MouseEvent.MOUSE_RELEASED, new ButtonStyleHandlerReleased(buttonStartNewGame));

			
			gameControlsPane.getChildren().addAll( livesBox, moneyBox, buttonStartRound, buttonStartNewGame);
			gameControlsPane.setStyle("-fx-background-color: #3D3D3D");
			
			((HBox)gameControlsPane).setAlignment(Pos.CENTER);
			((HBox)gameControlsPane).setSpacing(50);
			
			
			// Tower Controls Panel
			
			((BorderPane)borderPane).setBottom(towerControlsPane);
			ImageView moneyImageViewTowers1 = drawObject(moneyImagePath);
			ImageView moneyImageViewTowers2 = drawObject(moneyImagePath);
			ImageView moneyImageViewTowers3 = drawObject(moneyImagePath);

			moneyImageViewTowers1.setFitHeight(20);
			moneyImageViewTowers1.setFitWidth(20);
			moneyImageViewTowers2.setFitHeight(20);
			moneyImageViewTowers2.setFitWidth(20);
			moneyImageViewTowers3.setFitHeight(20);
			moneyImageViewTowers3.setFitWidth(20);
			
			VBox rapidTowerBox = new VBox();
			Button buttonRapidTower = new Button("Rapid Tower");
			buttonRapidTower.setStyle(buttonStyle);
			buttonRapidTower.addEventHandler(MouseEvent.MOUSE_PRESSED, new ButtonStyleHandlerPressed(buttonRapidTower));
			buttonRapidTower.addEventHandler(MouseEvent.MOUSE_RELEASED, new ButtonStyleHandlerReleased(buttonRapidTower));
			Label labelRapidTowerCost = new Label( String.valueOf(new RapidTower().cost) );
			labelRapidTowerCost.setTextFill(Color.WHITE);
			HBox rapidTowerCostBox = new HBox();
			rapidTowerCostBox.getChildren().addAll( moneyImageViewTowers1, labelRapidTowerCost );
			rapidTowerCostBox.setSpacing(10);
			rapidTowerCostBox.setAlignment(Pos.CENTER);
			rapidTowerBox.getChildren().addAll( buttonRapidTower, rapidTowerCostBox );
			rapidTowerBox.setSpacing(10);
			rapidTowerBox.setAlignment(Pos.CENTER);
			
			VBox splashTowerBox = new VBox();
			Button buttonSplashTower = new Button("Splash Tower");
			buttonSplashTower.setStyle(buttonStyle);
			buttonSplashTower.addEventHandler(MouseEvent.MOUSE_PRESSED, new ButtonStyleHandlerPressed(buttonSplashTower));
			buttonSplashTower.addEventHandler(MouseEvent.MOUSE_RELEASED, new ButtonStyleHandlerReleased(buttonSplashTower));
			Label labelSplashTowerCost = new Label( String.valueOf(new SplashTower().cost) );
			labelSplashTowerCost.setTextFill(Color.WHITE);
			HBox splashTowerCostBox = new HBox();
			splashTowerCostBox.getChildren().addAll( moneyImageViewTowers2, labelSplashTowerCost );
			splashTowerCostBox.setSpacing(10);
			splashTowerCostBox.setAlignment(Pos.CENTER);
			splashTowerBox.getChildren().addAll( buttonSplashTower, splashTowerCostBox );
			splashTowerBox.setSpacing(10);
			splashTowerBox.setAlignment(Pos.CENTER);
			
			VBox sniperTowerBox = new VBox();
			Button buttonSniperTower = new Button("Sniper Tower");
			buttonSniperTower.setStyle(buttonStyle);
			buttonSniperTower.addEventHandler(MouseEvent.MOUSE_PRESSED, new ButtonStyleHandlerPressed(buttonSniperTower));
			buttonSniperTower.addEventHandler(MouseEvent.MOUSE_RELEASED, new ButtonStyleHandlerReleased(buttonSniperTower));
			Label labelSniperTowerCost = new Label( String.valueOf(new SniperTower().cost) );
			labelSniperTowerCost.setTextFill(Color.WHITE);
			HBox sniperTowerCostBox = new HBox();
			sniperTowerCostBox.getChildren().addAll( moneyImageViewTowers3, labelSniperTowerCost );
			sniperTowerCostBox.setSpacing(10);
			sniperTowerCostBox.setAlignment(Pos.CENTER);
			sniperTowerBox.getChildren().addAll( buttonSniperTower, sniperTowerCostBox );
			sniperTowerBox.setSpacing(10);
			sniperTowerBox.setAlignment(Pos.CENTER);
			
			Label labelTowerTitle = new Label("Towers");
			labelTowerTitle.setTextFill(Color.WHITE);
			labelTowerTitle.setFont(Font.font(null, FontWeight.BOLD, 50));
			
			towerControlsPane.getChildren().addAll( labelTowerTitle, rapidTowerBox, splashTowerBox, sniperTowerBox);
			towerControlsPane.setStyle("-fx-background-color: #3D3D3D");
			((HBox)towerControlsPane).setAlignment(Pos.CENTER);
			((HBox)towerControlsPane).setSpacing(50);
			towerControlsPane.setPadding(new Insets(15, 0, 15, 0));
			
			
			// Top Control Button Handlers
			buttonStartRound.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
				}
			});
			
			buttonStartNewGame.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					System.out.println("new game clicked!");
				}
			});
			
			// Bottom Tower Button Handlers
			buttonRapidTower.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					System.out.println("new game clicked!");
				}
			});
			
			buttonSniperTower.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					System.out.println("new game clicked!");
				}
			});
			
			buttonSplashTower.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					System.out.println("new game clicked!");
				}
			});
		}
				
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
			rt.setByAngle(7);
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
		
		// Returns an ImageView object when given an image path
		public ImageView drawObject(String imagePath) {
			javafx.scene.image.Image image = new javafx.scene.image.Image(imagePath);
			ImageView imageView = new ImageView(image);
			return imageView;
		}
		
		// GENERAL BUTTON HANDLERS
		protected class ButtonStyleHandlerPressed implements EventHandler<MouseEvent> {
			Button activeButton;
			public ButtonStyleHandlerPressed(Button button) {
				activeButton = button;
			}
			@Override
			public void handle(MouseEvent e) {
				activeButton.setStyle(buttonStyleHover);
			}
		}
		
		protected class ButtonStyleHandlerReleased implements EventHandler<MouseEvent> {
			Button activeButton;
			public ButtonStyleHandlerReleased(Button button) {
				activeButton = button;
			}
			@Override
			public void handle(MouseEvent e) {
				activeButton.setStyle(buttonStyle);
			}
		}
	}
}


