//
// Johnathan Mai
// ITP368
// Assignment 2
//

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.geometry.*;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import javafx.animation.*;
import javafx.animation.Animation.Status;
import javafx.animation.PathTransition.*;
import java.lang.Number;
import javafx.event.*;

public class TowerDefenseAssignment4 extends Application {

	// Game Settings
	private static final int SCENE_WIDTH = 1000;
	private static final int SCENE_HEIGHT = 1000;
	
	static GameController gameController;
	
	// Constants
	
	//Image Paths & their image containers
	public static final String LIVES_IMAGE_PATH = "file:sprites/heartLives.png";
	public static final String MONEY_IMAGE_PATH = "file:sprites/moneyCoins.png";
	public static final String SMALL_ENEMY_IMAGE_PATH = "file:sprites/minionsmall.png";
	public static final String MEDIUM_ENEMY_IMAGE_PATH = "file:sprites/minionmedium.png";
	public static final String BOSS_ENEMY_IMAGE_PATH = "file:sprites/minionlarge.png";
	public static final String PROJECTILE_IMAGE_PATH = "file:sprites/projectile.png";

	public static javafx.scene.image.Image smallEnemyImage;
	public static javafx.scene.image.Image mediumEnemyImage;
	public static javafx.scene.image.Image bossEnemyImage;
	public static javafx.scene.image.Image projectileImage;

	
	public static void main( String[] args ) {
		Application.launch( args );
	}
	
	public void start( Stage primaryStage ) {
		BorderPane pane = new BorderPane();
		Pane gameArea = new Pane();
		HBox gameControlsPane = new HBox();
		HBox towerControlsPane = new HBox();
		
		// Setting up UI
		gameController = new GameController(pane, gameArea, gameControlsPane, towerControlsPane); 
		gameController.createUI();
		
		
		// Scene set up
		ScrollPane sp = new ScrollPane();
		sp.setContent(pane);
		Scene scene = new Scene( sp, SCENE_WIDTH, SCENE_HEIGHT );
		primaryStage.setScene( scene );
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	protected static class GameController {
		
		// ------------- Variables ------------- 
		private static ArrayList<Tower> towerList = new ArrayList<Tower>(); // Holds all stationary towers
		private static ArrayList<Enemy> enemyList = new ArrayList<Enemy>(); // Holds all moving enemies
		
		// Grid will be 20x20 50 pixel tiles, so overall map is 1000x1000 pixels.
		private static final int GRID_COLUMNS = 20;
		private static final int GRID_ROWS = 20;
		public static final Landscape[][] grid = new Landscape[GRID_COLUMNS][GRID_ROWS];
		public static final Group gridGroup = new Group(); // displays the grid
		public static final Group grassGroup  = new Group();
		
		javafx.scene.image.Image imageGrassTile;
		public static final String GRASS_IMAGE_PATH = "file:sprites/grass2.png";
		
		private static final String BUTTON_STYLE = "-fx-border-color: #e2e2e2; -fx-border-width: 1.5;" 
				+ "-fx-background-radius: 0; "
				+ "-fx-background-color: #3D3D3D; -fx-font-size: 15pt; "
				+ "-fx-text-fill: #FFFFFF; -fx-background-insets: 0 0 0 0, 0, 1, 2";
		private static final String BUTTON_STYLE_HOVER = "-fx-border-color: #e2e2e2; -fx-border-width: 1.5;" 
					+ "-fx-background-radius: 0; "
					+ "-fx-background-color: #FFFFFF; -fx-font-size: 15pt; "
					+ "-fx-text-fill: #3D3D3D; -fx-background-insets: 0 0 0 0, 0, 1, 2";
		
		// Important Variables
		protected Label labelLives;
		protected static Label labelMoney;
		protected Label labelRounds;
		protected static Integer lives = 15;
		protected static Integer money = 1000;
		protected static Integer round = 0;
		protected static Boolean inRound = false;
		protected Integer enemyStartX = 0;
		protected Integer enemyStartY = 0;
		protected Boolean towerClicked = false;
		protected Tower activeTower;
		protected Boolean towerConstructionActive = false;
		
		protected double originX, originY;
		protected double offsetX, offsetY;
		
		// Panes
		public static BorderPane borderPane;
		public static Pane gameArea;
		public static Pane gameControlsPane;
		public static Pane towerControlsPane;
		
		// Thread Stuff
		
		
		// Constructor
		GameController(BorderPane borderPaneMain, Pane gameAreaMain, Pane gameControlsPaneMain, Pane towerControlsPaneMain) {
			// Panes
			borderPane = borderPaneMain;
			gameArea = gameAreaMain;
			gameControlsPane = gameControlsPaneMain;
			towerControlsPane = towerControlsPaneMain;
			
			// Labels
			labelLives = new Label();
			labelMoney = new Label();
			labelRounds = new Label();
			
			// creates base layer of grass and base
			imageGrassTile = new javafx.scene.image.Image(GRASS_IMAGE_PATH);
			for (int i=0; i < GRID_ROWS; i++) {
				for (int j = 0; j < GRID_COLUMNS; j++) {
					Grass node = new Grass( (50 * i) + 25, (50 * j) + 25, imageGrassTile);
					grassGroup.getChildren().add( node.imageView );
					grid[i][j] = node;
				}
			}		
			enemyStartX = grid[0][2].getX();
			enemyStartY = grid[0][2].getY();
			Base mainBase = new Base(700,500, gameArea);
			drawGravelPath();
			gameArea.getChildren().addAll(grassGroup, gridGroup, mainBase.imageView);			
			loadImages();
		}
		
		// Creates the overall UI.
		protected void createUI() {
 			
			((BorderPane)borderPane).setCenter(gameArea);
			((BorderPane)borderPane).setTop(gameControlsPane);
			
			// Game Controls Panel
			
			HBox roundsBox = new HBox();
			Label roundWord = new Label("Round");
			roundWord.setTextFill(Color.WHITE);
			roundWord.setFont(Font.font(null, FontWeight.BOLD, 50));
			labelRounds.setText(String.valueOf(round));
			labelRounds.setTextFill(Color.WHITE);
			labelRounds.setFont(Font.font(null, FontWeight.BOLD, 50));
			roundsBox.getChildren().addAll(roundWord, labelRounds);
			roundsBox.setSpacing(10);
			roundsBox.setAlignment(Pos.CENTER);
			
			HBox livesBox = new HBox();
			ImageView livesImageView = drawObject(LIVES_IMAGE_PATH);
			livesImageView.setFitHeight(25);
			livesImageView.setFitWidth(25);
			labelLives.setText(String.valueOf(lives));
			labelLives.setTextFill(Color.WHITE);
			labelLives.setFont(Font.font(null, FontWeight.BOLD, 50));
			livesBox.getChildren().addAll(livesImageView, labelLives);
			livesBox.setSpacing(10);
			livesBox.setAlignment(Pos.CENTER);
			
			HBox moneyBox = new HBox();
			ImageView moneyImageView = drawObject(MONEY_IMAGE_PATH);
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
			buttonStartRound.setStyle(BUTTON_STYLE);
			buttonStartRound.addEventHandler(MouseEvent.MOUSE_PRESSED, new ButtonStyleHandlerPressed(buttonStartRound));
			buttonStartRound.addEventHandler(MouseEvent.MOUSE_RELEASED, new ButtonStyleHandlerReleased(buttonStartRound));

			Button buttonStartNewGame = new Button("New Game");
			buttonStartNewGame.setStyle(BUTTON_STYLE);
			buttonStartNewGame.addEventHandler(MouseEvent.MOUSE_PRESSED, new ButtonStyleHandlerPressed(buttonStartNewGame));
			buttonStartNewGame.addEventHandler(MouseEvent.MOUSE_RELEASED, new ButtonStyleHandlerReleased(buttonStartNewGame));

			
			gameControlsPane.getChildren().addAll( roundsBox, livesBox, moneyBox, buttonStartRound, buttonStartNewGame);
			gameControlsPane.setStyle("-fx-background-color: #3D3D3D");
			
			((HBox)gameControlsPane).setAlignment(Pos.CENTER);
			((HBox)gameControlsPane).setSpacing(40);
			
			
			// Tower Controls Panel
			
			((BorderPane)borderPane).setBottom(towerControlsPane);
			ImageView moneyImageViewTowers1 = drawObject(MONEY_IMAGE_PATH);
			ImageView moneyImageViewTowers2 = drawObject(MONEY_IMAGE_PATH);
			ImageView moneyImageViewTowers3 = drawObject(MONEY_IMAGE_PATH);

			moneyImageViewTowers1.setFitHeight(20);
			moneyImageViewTowers1.setFitWidth(20);
			moneyImageViewTowers2.setFitHeight(20);
			moneyImageViewTowers2.setFitWidth(20);
			moneyImageViewTowers3.setFitHeight(20);
			moneyImageViewTowers3.setFitWidth(20);
			
			VBox rapidTowerBox = new VBox();
			Button buttonRapidTower = new Button("Rapid Tower");
			buttonRapidTower.setStyle(BUTTON_STYLE);
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
			buttonSplashTower.setStyle(BUTTON_STYLE);
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
			buttonSniperTower.setStyle(BUTTON_STYLE);
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
					if (!inRound) {
						generateEnemies(round);
						round++;
						labelRounds.setText(String.valueOf(round));
						inRound = true;
					}
				}
			});
			
			buttonStartNewGame.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Restart Game");
					alert.setContentText("Are you sure you want to reset?");
					Optional<ButtonType> alertResult = alert.showAndWait();
					if (alertResult.get() == ButtonType.OK) {
						round = 0;
						lives = 15;
						money = 1000;
						for (int i = 0; i < enemyList.size(); i++) {
							enemyList.remove(i);
						}
						inRound = false;
						labelRounds.setText(String.valueOf(round));
						labelLives.setText(String.valueOf(lives));
						labelMoney.setText(String.valueOf(money));
						gameArea.getChildren().remove( 3, gameArea.getChildren().size());
					} else {
						System.out.println("Didn't reset!");
					}
				}
			});
			
			// Towers Implementation
			// Bottom Tower Button Handlers
			buttonRapidTower.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					if (!towerConstructionActive) {
						if (money >= 200){
							towerConstructionActive = true;
							RapidTower rapidTower = new RapidTower(900, 900, gameArea);
							createTowerDraggerHelper(gameArea, rapidTower);
							gameArea.getChildren().addAll(rapidTower.imageView);
						}
					}
				}
			});
			
			buttonSniperTower.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					if (!towerConstructionActive) {
						if (money >= 250) {
							towerConstructionActive = true;
							SniperTower sniperTower = new SniperTower(900, 900, gameArea);
							createTowerDraggerHelper(gameArea, sniperTower);
							gameArea.getChildren().addAll(sniperTower.imageView);
						}
					}
				}
			});
			
			buttonSplashTower.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					if (!towerConstructionActive) {
						if (money >= 300) {
							towerConstructionActive = true;
							SplashTower splashTower = new SplashTower(900, 900, gameArea);
							createTowerDraggerHelper(gameArea, splashTower);
							gameArea.getChildren().addAll(splashTower.imageView);
						}
					}
				}
			});
		}
				
		// This creates the gravel path for enemies to travel on.. Need to find a better way in the future.
		private void drawGravelPath() {
			gridGroup.getChildren().add(new Gravel(grid[0][2].getX(), grid[0][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[1][2].getX(), grid[1][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[2][2].getX(), grid[2][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[3][2].getX(), grid[3][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[4][2].getX(), grid[4][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[5][2].getX(), grid[5][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[6][2].getX(), grid[6][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[7][2].getX(), grid[7][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][2].getX(), grid[8][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[9][2].getX(), grid[9][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][2].getX(), grid[10][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[11][2].getX(), grid[11][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[12][2].getX(), grid[12][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][2].getX(), grid[13][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[14][2].getX(), grid[14][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[15][2].getX(), grid[15][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[16][2].getX(), grid[16][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[17][2].getX(), grid[17][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[18][2].getX(), grid[18][2].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[19][2].getX(), grid[19][2].getY()).tile);
			
			gridGroup.getChildren().add(new Gravel(grid[19][3].getX(), grid[19][3].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[19][4].getX(), grid[19][4].getY()).tile);
			
			gridGroup.getChildren().add(new Gravel(grid[19][5].getX(), grid[19][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[18][5].getX(), grid[18][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[17][5].getX(), grid[17][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[16][5].getX(), grid[16][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[15][5].getX(), grid[15][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[14][5].getX(), grid[14][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][5].getX(), grid[13][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[12][5].getX(), grid[12][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[11][5].getX(), grid[11][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][5].getX(), grid[10][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[9][5].getX(), grid[9][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][5].getX(), grid[8][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[7][5].getX(), grid[7][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[6][5].getX(), grid[6][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[5][5].getX(), grid[5][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[4][5].getX(), grid[4][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[3][5].getX(), grid[3][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[2][5].getX(), grid[2][5].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[1][5].getX(), grid[1][5].getY()).tile);
			
			gridGroup.getChildren().add(new Gravel(grid[1][6].getX(), grid[1][6].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[1][7].getX(), grid[1][7].getY()).tile);

			gridGroup.getChildren().add(new Gravel(grid[1][8].getX(), grid[1][8].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[2][8].getX(), grid[2][8].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[3][8].getX(), grid[3][8].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[4][8].getX(), grid[4][8].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[5][8].getX(), grid[5][8].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[6][8].getX(), grid[6][8].getY()).tile);

			gridGroup.getChildren().add(new Gravel(grid[6][9].getX(), grid[6][9].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[6][10].getX(), grid[6][10].getY()).tile);

			gridGroup.getChildren().add(new Gravel(grid[6][11].getX(), grid[6][11].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[5][11].getX(), grid[5][11].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[4][11].getX(), grid[4][11].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[3][11].getX(), grid[3][11].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[2][11].getX(), grid[2][11].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[1][11].getX(), grid[1][11].getY()).tile);

			gridGroup.getChildren().add(new Gravel(grid[1][12].getX(), grid[1][12].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[1][13].getX(), grid[1][13].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[1][14].getX(), grid[1][14].getY()).tile);

			gridGroup.getChildren().add(new Gravel(grid[1][15].getX(), grid[1][15].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[2][15].getX(), grid[2][15].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[3][15].getX(), grid[3][15].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[4][15].getX(), grid[4][15].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[5][15].getX(), grid[5][15].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[6][15].getX(), grid[6][15].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[7][15].getX(), grid[7][15].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][15].getX(), grid[8][15].getY()).tile);


			gridGroup.getChildren().add(new Gravel(grid[8][14].getX(), grid[8][14].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][13].getX(), grid[8][13].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][12].getX(), grid[8][12].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][11].getX(), grid[8][11].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][10].getX(), grid[8][10].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][9].getX(), grid[8][9].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][8].getX(), grid[8][8].getY()).tile);

			gridGroup.getChildren().add(new Gravel(grid[8][7].getX(), grid[8][7].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[9][7].getX(), grid[9][7].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][7].getX(), grid[10][7].getY()).tile);

			gridGroup.getChildren().add(new Gravel(grid[10][8].getX(), grid[10][8].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][9].getX(), grid[10][9].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][10].getX(), grid[10][10].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][11].getX(), grid[10][11].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][12].getX(), grid[10][12].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][13].getX(), grid[10][13].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][14].getX(), grid[10][14].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][15].getX(), grid[10][15].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][16].getX(), grid[10][16].getY()).tile);
			
			gridGroup.getChildren().add(new Gravel(grid[10][17].getX(), grid[10][17].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[9][17].getX(), grid[9][17].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][17].getX(), grid[8][17].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[7][17].getX(), grid[7][17].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[6][17].getX(), grid[6][17].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[5][17].getX(), grid[5][17].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[4][17].getX(), grid[4][17].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[3][17].getX(), grid[3][17].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[2][17].getX(), grid[2][17].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[1][17].getX(), grid[1][17].getY()).tile);

			gridGroup.getChildren().add(new Gravel(grid[1][18].getX(), grid[1][18].getY()).tile);
			
			gridGroup.getChildren().add(new Gravel(grid[1][19].getX(), grid[1][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[2][19].getX(), grid[2][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[3][19].getX(), grid[3][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[4][19].getX(), grid[4][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[5][19].getX(), grid[5][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[6][19].getX(), grid[6][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[7][19].getX(), grid[7][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[8][19].getX(), grid[8][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[9][19].getX(), grid[9][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[10][19].getX(), grid[10][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[11][19].getX(), grid[11][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[12][19].getX(), grid[12][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][19].getX(), grid[13][19].getY()).tile);

			gridGroup.getChildren().add(new Gravel(grid[13][19].getX(), grid[13][19].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][18].getX(), grid[13][18].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][17].getX(), grid[13][17].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][16].getX(), grid[13][16].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][15].getX(), grid[13][15].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][14].getX(), grid[13][14].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][13].getX(), grid[13][13].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][12].getX(), grid[13][12].getY()).tile);
			gridGroup.getChildren().add(new Gravel(grid[13][11].getX(), grid[13][11].getY()).tile);
		}
		
		private void loadImages() {
			smallEnemyImage = new javafx.scene.image.Image(SMALL_ENEMY_IMAGE_PATH);
			mediumEnemyImage = new javafx.scene.image.Image(MEDIUM_ENEMY_IMAGE_PATH);
			bossEnemyImage = new javafx.scene.image.Image(BOSS_ENEMY_IMAGE_PATH);
			projectileImage = new javafx.scene.image.Image(PROJECTILE_IMAGE_PATH);
			
		}
		
		// Generates different enemies based on what round it is
		public void generateEnemies(Integer round) {
			switch(round) {
				case 0:
					sendBossEnemy();
					sleep(500);
					sendMediumEnemy();
					sleep(500);
					sendSmallEnemy();
					break;
				case 1:
					sendSmallEnemy();
					sleep(500);
					sendSmallEnemy();
					sleep(500);
					sendSmallEnemy();
					sleep(500);
					sendSmallEnemy();
					sleep(500);
					sendSmallEnemy();
					break;
				case 2:
					sendSmallEnemy();
					sleep(500);
					sendSmallEnemy();
					sleep(500);
					sendSmallEnemy();
					sleep(500);
					sendMediumEnemy();
					sleep(500);
					sendMediumEnemy();
					break;
				case 3:
					sendSmallEnemy();
					sleep(500);
					sendMediumEnemy();
					sleep(500);
					sendMediumEnemy();
					sleep(500);
					sendMediumEnemy();
					sleep(500);
					sendMediumEnemy();
					break;
				case 4:
					sendSmallEnemy();
					sleep(500);
					sendMediumEnemy();
					sleep(500);
					sendMediumEnemy();
					sleep(500);
					sendMediumEnemy();
					sleep(500);
					sendBossEnemy();
					break;
				case 5:
					sendSmallEnemy();
					sleep(500);
					sendMediumEnemy();
					sleep(500);
					sendBossEnemy();
					sleep(500);
					sendBossEnemy();
					sleep(500);
					sendBossEnemy();
					break;
				default:
					break;
			}
		}
		
		private void sendSmallEnemy() {
			SmallEnemy e = new SmallEnemy(enemyStartX, enemyStartY, smallEnemyImage);
			gameArea.getChildren().addAll(e.imageView);
			createPath(e);
			enemyList.add(e);
			// This enables enemies to be clicked for damage
			//e.imageView.setOnMouseClicked(new EnemyClickedHandler(e));
		}
		
		private void sendMediumEnemy() {
			MediumEnemy e = new MediumEnemy(enemyStartX, enemyStartY, mediumEnemyImage);
			gameArea.getChildren().addAll(e.imageView);
			createPath(e);
			enemyList.add(e);
			//e.imageView.setOnMouseClicked(new EnemyClickedHandler(e));
		}
		
		private void sendBossEnemy() {
			BossEnemy e = new BossEnemy(enemyStartX, enemyStartY, bossEnemyImage);
			gameArea.getChildren().addAll(e.imageView);
			createPath(e);
			enemyList.add(e);
			//e.imageView.setOnMouseClicked(new EnemyClickedHandler(e));
		}
		
		// Creates the path the given enemy to travel, following the gravel path. 
		// Also includes handler for losing/winning game.
		private void createPath( Enemy e ) {
			double travelSpeed = 600000 / e.walkSpeed;
			
			
			PathTransition patht = new PathTransition();
			Path path = new Path();

		    path.getElements().add (new MoveTo (grid[0][2].getX(), grid[0][2].getY()));
		    path.getElements().add (new LineTo (grid[19][2].getX(), grid[19][2].getY()));
		    path.getElements().add (new LineTo (grid[19][5].getX(), grid[19][5].getY()));
		    path.getElements().add (new LineTo (grid[1][5].getX(), grid[1][5].getY()));
		    path.getElements().add (new LineTo (grid[1][8].getX(), grid[1][8].getY()));
		    path.getElements().add (new LineTo (grid[6][8].getX(), grid[6][8].getY()));
		    path.getElements().add (new LineTo (grid[6][11].getX(), grid[6][11].getY()));
		    path.getElements().add (new LineTo (grid[1][11].getX(), grid[1][11].getY()));
		    path.getElements().add (new LineTo (grid[1][15].getX(), grid[1][15].getY()));
		    path.getElements().add (new LineTo (grid[8][15].getX(), grid[8][15].getY()));
		    path.getElements().add (new LineTo (grid[8][7].getX(), grid[8][7].getY()));
		    path.getElements().add (new LineTo (grid[10][7].getX(), grid[10][7].getY()));
		    path.getElements().add (new LineTo (grid[10][17].getX(), grid[10][17].getY()));
		    path.getElements().add (new LineTo (grid[1][17].getX(), grid[1][17].getY()));
		    path.getElements().add (new LineTo (grid[1][19].getX(), grid[1][19].getY()));
		    path.getElements().add (new LineTo (grid[13][19].getX(), grid[13][19].getY()));
		    path.getElements().add (new LineTo (grid[13][11].getX(), grid[13][11].getY()));


			patht.setDuration(Duration.millis(travelSpeed));
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
			patht.setOnFinished(new EnemyFinishHandler(e));
		}
		
		// Adds Node to pane and arraylist
		
		// Creates a floating "-1" for every time you damage an enemy
		public static void createHitMarker(Enemy e, int damage, int enemyIndex) {
			Bounds enemyBounds = e.imageView.localToParent(e.imageView.getBoundsInParent());
			double enemyX = enemyBounds.getMinX();
			double enemyY = enemyBounds.getMinY();
			
			if (e.health <= 0) { // When it is the last health point of the enemy, it dies
				gameArea.getChildren().remove(e.imageView);
				if (enemyList.size() == 0) { // When it is the last enemy of the round
					if (round == 5) { // when round 5 finishes, you win
						displayWinMessage();
					}
				}
			}
			
			Label hit = new Label(String.valueOf(damage));
			hit.setTextFill(Color.RED);
			hit.setFont(Font.font(null, FontWeight.BOLD, 18));
			hit.setLayoutX(enemyX);
			hit.setLayoutY(enemyY);
			gameArea.getChildren().add(hit);
			FadeTransition ft = new FadeTransition(Duration.millis(1000), hit);
			ft.setFromValue(1.0);
			ft.setToValue(0.0);
			ft.play();
			ft.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					gameArea.getChildren().remove(hit);

					System.out.println(String.valueOf(enemyList.size()));
				}
			});
		}
		
		// Returns an ImageView object when given an image path
		public ImageView drawObject(String imagePath) {
			javafx.scene.image.Image image = new javafx.scene.image.Image(imagePath);
			ImageView imageView = new ImageView(image);
			return imageView;
		}
		
		// Creates the interface to "construct" a tower
		private void createTowerDraggerHelper(Pane gameArea, Tower tower) {
			FadeTransition ft = new FadeTransition(Duration.millis(1000), tower.attackRadiusVisual);
			ft.setFromValue(1.0);
			ft.setToValue(0.05);
			ft.setCycleCount(Timeline.INDEFINITE);
			ft.setAutoReverse(true);
			ft.play();
			Label labelDragIndicator = new Label("Drag tower to where you want it!");
			labelDragIndicator.setTextFill(Color.WHITE);
			labelDragIndicator.setFont(Font.font(null, FontWeight.BOLD, 17));
			labelDragIndicator.setLayoutX(710);
			labelDragIndicator.setLayoutY(870);
			
			// Buttons for placing tower or cancel event
			Button buttonAccept = new Button("Place");
			Button buttonCancel = new Button("Cancel");
			buttonAccept.setStyle(BUTTON_STYLE);
			buttonCancel.setStyle(BUTTON_STYLE);
			buttonAccept.setLayoutX(900);
			buttonAccept.setLayoutY(750);
			buttonCancel.setLayoutX(887);
			buttonCancel.setLayoutY(800);
			buttonAccept.setOnMousePressed(new ButtonStyleHandlerPressed(buttonAccept));
			buttonAccept.setOnMouseReleased(new ButtonStyleHandlerReleased(buttonAccept));
			buttonCancel.setOnMousePressed(new ButtonStyleHandlerPressed(buttonCancel));
			buttonCancel.setOnMouseReleased(new ButtonStyleHandlerReleased(buttonCancel));
			
			gameArea.getChildren().addAll(tower.attackRadiusVisual, labelDragIndicator, buttonAccept, buttonCancel);

			// Button Event Handlers
			tower.imageView.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) { // Sets up coordinates for dragging in below handler
					originX = e.getSceneX();
					originY = e.getSceneY();
					offsetX = ((ImageView)(e.getSource())).getTranslateX();
					offsetY = ((ImageView)(e.getSource())).getTranslateY();
					gameArea.getChildren().remove(labelDragIndicator);
					
				}
			});
			
			tower.imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					double differenceX = e.getSceneX() - originX;
					double differenceY = e.getSceneY() - originY;
					double newX = offsetX + differenceX;
					double newY = offsetY + differenceY;
					
					((ImageView)(e.getSource())).setTranslateX(newX);
					((ImageView)(e.getSource())).setTranslateY(newY);
					tower.attackRadiusVisual.setTranslateX(newX);
					tower.attackRadiusVisual.setTranslateY(newY);
					
				}
			});
			
			tower.imageView.setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {}
			});
			
			buttonAccept.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					tower.imageView.setOnMousePressed(null);
					tower.imageView.setOnMouseDragged(null);
					tower.setX(tower.imageView.getX() - (tower.imageView.getFitWidth()/2));
					tower.setY(tower.imageView.getY() - (tower.imageView.getFitHeight()/2));
					tower.attackRadiusVisual.setFill( new Color(1,1,1,0.0) );
					towerList.add(tower);
					gameArea.getChildren().removeAll(labelDragIndicator, buttonAccept, buttonCancel);
					money = money - tower.cost;
					labelMoney.setText(String.valueOf(money));
					towerConstructionActive = false;
				}
			});
			
			buttonCancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					towerConstructionActive = false;
					tower.imageView.setOnMousePressed(null);
					tower.imageView.setOnMouseDragged(null);
					gameArea.getChildren().removeAll(tower.attackRadiusVisual, labelDragIndicator, buttonAccept, buttonCancel, tower.imageView);
				}
			});			
		}
		
		// Sleeper
		private void sleep(int milliseconds) {
			try {
				Thread.sleep(milliseconds);
			} catch (Exception e) {
			}
		}
		
		// Displays win or lose message on screen
		public static void displayWinMessage() {
			Label win = new Label("You win! For now..");
			win.setTextFill(Color.WHITE);
			win.setFont(Font.font(null, FontWeight.BOLD, 80));
			win.setLayoutX(100);
			win.setLayoutY(250);
			gameArea.getChildren().add( win );
		}
		public void displayGameOver() {
			Label lose = new Label("You lost :(. Try again!");
			lose.setTextFill(Color.RED);
			lose.setFont(Font.font(null, FontWeight.BOLD, 80));
			lose.setLayoutX(100);
			lose.setLayoutY(250);
			gameArea.getChildren().add(lose);
		}

		
		// Handles when an enemy reaches end
		protected class EnemyFinishHandler implements EventHandler<ActionEvent> {
			Enemy enemy;
			
			public EnemyFinishHandler(Enemy e) {
				enemy = e;
			}
			
			@Override
			public void handle(ActionEvent event) {
				if (enemy.health <= 0) {
					// Nothing happens here because this was an enemy that died and the imageview was taken off the pane.
				} else {
					gameArea.getChildren().remove(enemy.imageView);
					lives--;
					labelLives.setText(String.valueOf(lives));
					if (enemyList.size() == 0) { // when there are no more enemies in a round
						inRound = false;
						if (round == 5) { // when round 5 finishes, you win
							displayWinMessage();
						}
					}
				}
				if (lives == 0) { // when lives reaches zero, then we lose :(
					displayGameOver();
				}
			}
		}
		
		// GENERAL BUTTON HANDLERS
		protected class ButtonStyleHandlerPressed implements EventHandler<MouseEvent> {
			Button activeButton;
			public ButtonStyleHandlerPressed(Button button) {
				activeButton = button;
			}
			@Override
			public void handle(MouseEvent e) {
				activeButton.setStyle(BUTTON_STYLE_HOVER);
			}
		}
		
		protected class ButtonStyleHandlerReleased implements EventHandler<MouseEvent> {
			Button activeButton;
			public ButtonStyleHandlerReleased(Button button) {
				activeButton = button;
			}
			@Override
			public void handle(MouseEvent e) {
				activeButton.setStyle(BUTTON_STYLE);
			}
		}
	
		// Getters and Setters
		public static ArrayList<Enemy> getEnemyList() {
			return enemyList;
		}
		
		public static int getRound() {
			return round;
		}
		
		public static ArrayList<Tower> getTowerList() {
			return towerList;
		}
		
		public static int getMoney() {
			return money;
		}
		
		public static void addMoney(int rewards) {
			money += rewards;
			labelMoney.setText(String.valueOf(money));
		}
	}
}


