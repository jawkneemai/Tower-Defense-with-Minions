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
	
	public void setX(double x) {
		centerX = (int)(x);
	}
	
	public Integer getY() {
		return centerY;
	}
	
	public void setY(double y) {
		centerY = (int)y;
	}
}
