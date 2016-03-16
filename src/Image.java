import javafx.scene.image.ImageView;

public abstract class Image {
	ImageView imageView;
	javafx.scene.image.Image image;
	protected Integer centerX, centerY;
	
	public Image() {
	}
		
	public Integer getX() {
		return centerX;
	}
	
	public Integer getY() {
		return centerY;
	}
}
