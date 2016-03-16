import javafx.scene.shape.*;
import javafx.scene.paint.Color;


public class Gravel extends Landscape {
	
	public Gravel() {}
	public Gravel(Integer x, Integer y) {
		super(x, y);
		walkable = true;
		buildable = false;
		tileColor = Color.rgb(189, 140, 107);
		tile.setFill(tileColor);
		 
	}

}
