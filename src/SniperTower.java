import javafx.application.Application;
import javafx.scene.image.*;
import javafx.scene.image.Image;

public class SniperTower extends Tower {
	
	public static final String imagePath = "file:sprites/snipertower.png";
	
	public SniperTower() {}
	public SniperTower(Integer x, Integer y) {
		super(x, y);
		image = new Image(imagePath);
		imageView = new ImageView(image);
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
		
		damage = 50;
		fireRate = 0.5;
		attackRadius = 10.0;
		splashDamage = false;
	}

}
