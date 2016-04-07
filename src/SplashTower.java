import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SplashTower extends Tower {
	
	public static final String imagePath = "file:sprites/splashtower.png";
	
	public SplashTower() {
		cost = 300;
		damage = 20;
		fireRate = 2;
		attackRadius = 7.0;
		splashDamage = true;
	}
	public SplashTower(Integer x, Integer y) {
		super(x, y);
		image = new Image(imagePath);
		imageView = new ImageView(image);
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
		
		cost = 300;
		damage = 20;
		fireRate = 2;
		attackRadius = 7.0;
		splashDamage = true;
		
		attackRadiusVisual = new Circle();
		attackRadiusVisual.setRadius(attackRadius * 20);
		attackRadiusVisual.setCenterX((double)x + 25);
		attackRadiusVisual.setCenterY((double)y + 25);
		attackRadiusVisual.setFill(new Color(1,1,1,.3));
	}
}
