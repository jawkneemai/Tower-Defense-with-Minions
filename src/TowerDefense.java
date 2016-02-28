// 
// Johnathan Mai
// ITP368, Spring 2016
// Tower Defense Project
// 

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.animation.*;
import javafx.util.*;

import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.util.Random;

public class TowerDefense extends Application {
	public static void main( String[] args ) {
		Application.launch( args );
	}
	
	public void start( Stage primaryStage ) {
		// ------------------------------------- Variables -------------------------------------
		
		
		
		// ------------------------------------- GUI -------------------------------------
		// Overall Layout
		BorderPane pane = new BorderPane();
		
		// Top toolbar, will add later when game is more developed, no need for Assignment 1
		//HBox toolbar = new HBox();
		//toolbar.setStyle("-fx-background-color: yellow");
		//Button button = new Button("top");
		//toolbar.getChildren().add(button);
		
		// Center game area
		GridPane gameArea = new GridPane();
		gameArea.setStyle("-fx-background-color: white");
		
		// Bottom toolbar
		VBox selectionArea = new VBox();
		selectionArea.setStyle("-fx-background-color: #FFCCCC");
		selectionArea.setPadding(new Insets(20.0));
		selectionArea.setSpacing(10);
		selectionArea.setAlignment(Pos.CENTER);

		Label imageChoiceDescription = new Label("Choose your image:");
		ChoiceBox imageChoice = new ChoiceBox(FXCollections.observableArrayList("Grass", "Gravel", "SniperTower", "SplashTower", "RapidTower", "SmallMinion", "MediumMinion", "BossMinion", "Base", "Projectile"));
		Label scaleTextFieldDescription = new Label("Enter a percentage to scale your image by: (100% is normal size)");
		Button submitButton = new Button("Update");
		
		// HBox to hold the TextField and % sign on same line
		HBox scaleInputBox = new HBox();
		scaleInputBox.setAlignment(Pos.CENTER);
		TextField scaleInput = new TextField("");
		Label percentageSign = new Label("%");
		scaleInputBox.getChildren().addAll(scaleInput, percentageSign);
		scaleInput.setMaxWidth(100);
		
		selectionArea.getChildren().addAll(imageChoiceDescription, imageChoice, scaleTextFieldDescription, scaleInputBox, submitButton);
		
		//pane.setTop(toolbar);
		pane.setCenter(gameArea);
		pane.setBottom(selectionArea);
		
		// ------------------------------------- FUNCTION -------------------------------------
		// Listeners
		submitButton.setOnAction( e -> {
			String currentImageChoice = new String();
			currentImageChoice = imageChoice.getValue().toString();
			System.out.println(currentImageChoice);
			if (scaleInput.getText() != null && !(scaleInput.getText().isEmpty())) {
				try {
					System.out.println(Double.parseDouble(scaleInput.getText()));
				} catch (Exception e) {
					scaleInput.setText("");
					errormsg.setText("Please only input numbers");
					return;
				}
			}
			
		});
		
		
		
		
		Scene scene = new Scene( pane, 800, 800 );
		primaryStage.setScene( scene );
		primaryStage.show();
	}
}
