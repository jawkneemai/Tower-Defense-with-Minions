import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;

public class Grass extends Landscape {

	public Grass() {}
	
	public Grass(Integer x, Integer y, javafx.scene.image.Image grassTile) {
		super(x, y);
		buildable = true;
		walkable = false;
		
		image = grassTile;
		imageView = new ImageView(image);
		imageView.setFitHeight(50);
		imageView.setFitWidth(50);
		imageView.setX(x - (imageView.getFitWidth()/2));
		imageView.setY(y - (imageView.getFitHeight()/2));
		
		
	}
}
