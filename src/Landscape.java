import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public abstract class Landscape extends Image {
	static final int width = 50;
	static final int height = 50;
	
	ImageView tile;
	Color tileColor;
	boolean walkable;
	boolean buildable;
	
	public Landscape() {}
	public Landscape(Integer x, Integer y) {
		centerX = x;
		centerY = y;
	}
}
