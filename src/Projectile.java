import javafx.scene.image.ImageView;

public class Projectile extends Image {
	
	private Integer fromX, toX, fromY, toY = 0;
	
	public Projectile(Integer frX, Integer frY, Integer tX, Integer tY, javafx.scene.image.Image loadedImage) {
		centerX = 0;
		centerY = 0;
		fromX = frX;
		fromY = frY;
		toX = tX;
		toY = tY;
		image = loadedImage;
		imageView = new ImageView( image );
		imageView.setX(centerX - (imageView.getFitWidth()/2));
		imageView.setY(centerY - (imageView.getFitHeight()/2));
	}
	
	public Integer getFromX() {
		return fromX;
	}
	
	public Integer getFromY() {
		return fromY;
	}
	
	public Integer getToY() {
		return toY;
	}
	
	public Integer getToX() {
		return toX;
	}
	
}
