import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SplashTower extends Tower {
	
	public static final String imagePath = "file:sprites/splashtower.png";
	
	public SplashTower() {}
	public SplashTower(Integer x, Integer y) {
		super(x, y);
		image = new Image(imagePath);
		imageView = new ImageView(image);
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
		
		damage = 20;
		fireRate = 2;
		attackRadius = 7.0;
		splashDamage = true;
	}
}
