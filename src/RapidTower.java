import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RapidTower extends Tower {
	
	public static final String imagePath = "file:sprites/rapidtower.png";
	
	public RapidTower() {}
	public RapidTower(Integer x, Integer y) {
		super(x, y);
		image = new Image(imagePath);
		imageView = new ImageView(image);
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
		
		damage = 5;
		fireRate = 4;
		attackRadius = 5.0;
		splashDamage = false;
	}

}
