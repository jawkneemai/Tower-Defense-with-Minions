import javafx.application.Application;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SniperTower extends Tower {
	
	public static final String imagePath = "file:sprites/snipertower.png";
	
	public SniperTower() {
		cost = 250;
		damage = 50;
		fireRate = 0.5;
		attackRadius = 10.0;
		splashDamage = false;
	}
	public SniperTower(Integer x, Integer y) {
		super(x, y);
		image = new Image(imagePath);
		imageView = new ImageView(image);
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
		
		cost = 250;
		damage = 50;
		fireRate = 0.5;
		attackRadius = 20.0;
		splashDamage = false;
		
		attackRadiusVisual = new Circle();
		attackRadiusVisual.setRadius(attackRadius * 20);
		attackRadiusVisual.setCenterX((double)x + 25);
		attackRadiusVisual.setCenterY((double)y + 25);
		attackRadiusVisual.setFill(new Color(1,1,1,.3));
	}

}
