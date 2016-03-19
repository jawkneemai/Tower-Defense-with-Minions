import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.*;

public class User extends Enemy {
	public static final String imagePath = "file:sprites/personsprite.png";
	public User (Integer x, Integer y) {
		super(x, y);
		image = new Image(imagePath);
		imageView = new ImageView(image); 
		imageView.setFitHeight(50);
		imageView.setPreserveRatio(true);
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
	}
	
	public User() {}
	
	public void setX(Double newX) {
		imageView.setX(newX - ((imageView.getFitWidth())/2) - 25);
		centerX = newX.intValue();
	}
	
	public void setY(Double newY) {
		imageView.setY(newY - ((imageView.getFitHeight())/2));
		centerY = newY.intValue();
	}

}