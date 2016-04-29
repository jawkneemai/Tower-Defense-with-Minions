import javafx.scene.image.ImageView;

public abstract class Enemy extends Image {
	double walkSpeed;
	protected int health, damage;
	
	public Enemy() {}
	
	public Enemy(Integer x, Integer y) {
		centerX = x;
		centerY = y;
	}
	
	public void receiveDamage(int damage) {
		health = health - damage;
	}
	
}
