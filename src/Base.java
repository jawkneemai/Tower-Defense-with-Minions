import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Base extends Tower {
	
	public static final String imagePath = "file:sprites/basetower2.png";
	
	public Base() {}
	public Base(Integer x, Integer y) {
		super(x, y);
		image = new Image(imagePath);
		imageView = new ImageView(image);
		imageView.setFitHeight(100);
		imageView.setFitWidth(100);
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
		
		damage = 20;
		fireRate = 2;
		attackRadius = 7.0;
		splashDamage = false;
	}

}