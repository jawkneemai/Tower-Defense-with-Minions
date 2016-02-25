// 
// Johnathan Mai
// ITP368, Spring 2016
// Tower Defense Project
// 

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.animation.*;
import javafx.util.*;
import java.util.Random;

public class TowerDefense extends Application {
	public static void main( String[] args ) {
		Application.launch( args );
	}
	
	public void start( Stage primaryStage ) {
		// Overall Layout
		BorderPane pane = new BorderPane();
		HBox toolbar = new HBox();
		toolbar.setStyle("-fx-background-color: yellow");
		Button button = new Button("top");
		toolbar.getChildren().add(button);
		GridPane gameArea = new GridPane();
		gameArea.setStyle("-fx-background-color: blue");
		Button button2 = new Button("center");
		gameArea.getChildren().add(button2);
		HBox selectionArea = new HBox();
		selectionArea.setStyle("-fx-background-color: green");
		Button button3 = new Button("bottom");
		selectionArea.getChildren().add(button3);
		pane.setTop(toolbar);
		pane.setCenter(gameArea);
		pane.setBottom(selectionArea);
		
		
		
		
		
		Scene scene = new Scene( pane, 400, 400 );
		primaryStage.setScene( scene );
		primaryStage.show();
	}
}
