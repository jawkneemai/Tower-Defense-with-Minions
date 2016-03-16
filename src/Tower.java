import javafx.scene.image.ImageView;

public abstract class Tower extends Image{
	int damage; // in arbitrary damage units
	double fireRate; // in units of shots per second
	double attackRadius;
	boolean splashDamage;
	
	
	public Tower() {}
	public Tower(Integer x, Integer y) {
		centerX = x;
		centerY = y;
		
	}
	
}
