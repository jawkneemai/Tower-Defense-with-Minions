import javafx.scene.image.ImageView;

public class Projectile extends Image {
	public static final String imagePath = "file:sprites/projectile.png";

	public Projectile(Integer x, Integer y) {
		centerX = x;
		centerY = y;
		image = new javafx.scene.image.Image(imagePath);
		imageView = new ImageView( image );
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
	}
	
}
