import javafx.scene.image.ImageView;

public abstract class Enemy extends Image {
	float walkSpeed;
	int health, damage;
	
	public Enemy() {}
	
	public Enemy(Integer x, Integer y) {
		centerX = x;
		centerY = y;
	}
}
