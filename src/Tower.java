import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public abstract class Tower extends Image{
	int damage; // in arbitrary damage units
	double fireRate; // in units of shots per second
	double attackRadius;
	boolean splashDamage;
	int cost;
	Circle attackRadiusVisual;
	protected final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	protected ScheduledFuture<?> testMonsterHandler;
	Projectile p;
	Pane gameArea;
	
	
	
	public Tower() {}
	public Tower(Integer x, Integer y, Pane gameArea) {
		centerX = x;
		centerY = y;
		this.gameArea = gameArea;
	}
}
