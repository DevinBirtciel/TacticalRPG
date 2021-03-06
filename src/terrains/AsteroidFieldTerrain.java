package terrains;

import java.awt.Point;
import java.util.Random;

import terrainVisuals.AsteroidFieldVisual;
import model.Popup;
import model.Ship;

public class AsteroidFieldTerrain extends Terrain implements Popup{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6914481105273488129L;
	private static final int MAX_DAMAGE = 8;
	
	public AsteroidFieldTerrain(Point newLocation) {
		super(newLocation);
		this.setVisual(new AsteroidFieldVisual(this));
	}

	@Override
	public void applyEffect(Ship ship) {
		Random random = new Random();
		int damage = random.nextInt(MAX_DAMAGE) + 1; // damage
		ship.updateHull(-damage);
	}

	@Override
	public String getName() {
		String name = "Asteroid Field";
		return name;
	}

	@Override
	public String getDescription() {
		String description = "Damages passing ships.";
		return description;
	}
}
