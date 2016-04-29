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

public class BossEnemy extends Enemy {
	// int walkspeed, health, damage 
	public BossEnemy (Integer x, Integer y, javafx.scene.image.Image image) {
		super(x, y);
		imageView = new ImageView(image); 
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
		imageView.setFitHeight(100);
		imageView.setPreserveRatio(true);

		walkSpeed = 8;
		health = 2000;
	}
	
	public int getHealth() {
		return health;
	}
	public void setHealth(int newHealth) {
		health = newHealth;
	}
}
