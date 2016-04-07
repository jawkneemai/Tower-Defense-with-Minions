import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;

public class Gravel extends Landscape {
	
	public static final String gravelImagePath = "file:sprites/gravel.png";

	public Gravel(Integer x, Integer y) {
		super(x, y);
		walkable = true;
		buildable = false;
		image = new javafx.scene.image.Image(gravelImagePath);
		tile = new ImageView(image);
		tile.setFitHeight(50);
		tile.setFitWidth(50);
		tile.setX(x - (tile.getFitWidth()/2));
		tile.setY(y - (tile.getFitHeight()/2));


		 
	}

}
