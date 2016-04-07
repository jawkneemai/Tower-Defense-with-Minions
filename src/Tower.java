import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public abstract class Tower extends Image{
	int damage; // in arbitrary damage units
	double fireRate; // in units of shots per second
	double attackRadius;
	boolean splashDamage;
	int cost;
	Circle attackRadiusVisual;
	
	
	public Tower() {}
	public Tower(Integer x, Integer y) {
		centerX = x;
		centerY = y;
	}
	
}
