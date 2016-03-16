import javafx.scene.paint.Color;

public class Grass extends Landscape {
	
	public Grass() {}
	
	public Grass(Integer x, Integer y) {
		super(x, y);
		buildable = true;
		walkable = false;
		tileColor = Color.rgb(0, 174, 98);
		tile.setFill(tileColor);
		
	}
}
