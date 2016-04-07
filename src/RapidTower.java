import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class RapidTower extends Tower {
	
	public static final String imagePath = "file:sprites/rapidtower.png";
	
	public RapidTower() {
		cost = 200;
		damage = 5;
		fireRate = 4;
		attackRadius = 5.0;
		splashDamage = false;
	}
	public RapidTower(Integer x, Integer y) {
		super(x, y);
		image = new Image(imagePath);
		imageView = new ImageView(image);
		imageView.setX(x - ((imageView.getFitWidth())/2));
		imageView.setY(y - ((imageView.getFitHeight())/2));
		
		cost = 200;
		damage = 5;
		fireRate = 4;
		attackRadius = 5.0;
		splashDamage = false;
		
		attackRadiusVisual = new Circle();
		attackRadiusVisual.setRadius(attackRadius * 20);
		attackRadiusVisual.setCenterX((double)x + 25);
		attackRadiusVisual.setCenterY((double)y + 25);
		attackRadiusVisual.setFill(new Color(1,1,1,.3));
	}

}
