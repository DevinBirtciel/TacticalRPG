package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import specific_ships_items.BattleCruiser;
import specific_ships_items.Bomber;
import specific_ships_items.Fighter;
import specific_ships_items.Mothership;
import specific_ships_items.RepairShip;
import specific_ships_items.Scout;
import specific_ships_items.WarpGateShip;
import terrains.AsteroidTerrain;
import terrains.PlanetTerrain2x2;
import terrains.Terrain;

/**
 * Tried to wrap all the Tile classes into one class to simplify things and cut
 * down on the number of overall classes. After a ship has moved away from a
 * tile, or if for some other reason a tile becomes empty call setEmpty() to
 * reset the tile back to previous conditions. A built in alternative to this is
 * just making a new tile. Several different constructors have been provided to
 * minimize outside method calls.
 */

public class Tile extends GameObject implements Comparable<Tile>{

	private boolean isOccupied, hasTerrain, hasShip;
	private Highlight highlight;
	private boolean mousedOver;
	private Ship ship;
	private int mapX, mapY;
	private Terrain terrain;
	
	//Variables for shortestPath algorithm
	private Tile prev;
	private int distance;
	
	/*
	 * highlight colors
	 */
	public enum Highlight {
		NONE, BLUE, RED, GREEN;
	}

	private static float highlightAlpha = .4f;
	private static Color blueColor = new Color(0f, .2f, 1f, highlightAlpha);
	private static Color lightBlueColor = new Color(.5f, .5f, 1f,
			highlightAlpha);
	private static Color redColor = new Color(1f, .2f, .2f, highlightAlpha);
	private static Color lightRedColor = new Color(1f, .5f, .5f, highlightAlpha);
	private static Color greenColor = new Color(0f, .8f, .3f, highlightAlpha);
	private static Color lightGreenColor = new Color(.5f, 1f, .5f,
			highlightAlpha);

	/*
	 * Constructors
	 */
	
	/**
	 * Empty tile constructor for creation at certain coordinates.
	 */
	public Tile(int mapX, int mapY) {
		setEmpty();
		this.mapX = mapX;
		this.mapY = mapY;
	}

	/**
	 * Ship tile constructor.
	 * 
	 * @param newShip
	 */
	public Tile(Ship newShip) {
		setEmpty();
		mapX = newShip.getLocation().x;
		mapY = newShip.getLocation().y;
		setHasShip(true, newShip);
	}

	/**
	 * Terrain tile constructor
	 */
	public Tile(boolean trueIfTerrainCanBeOccupied, Terrain newTerrain) {
		setEmpty();
		mapX = newTerrain.getLocation().x;
		mapY = newTerrain.getLocation().y;
		setTerrain(trueIfTerrainCanBeOccupied, newTerrain);
	}
	
	/**
	 * Occupied or Unoccupied generic terrain Tile constructor
	 */
	public Tile(boolean trueIfTerrainCanBeOccupied, int x, int y){
		setEmpty();
		mapX = x;
		mapY = y;
		setIsOccupied(true);
		setHasTerrain(true);
	}

	public void draw(Graphics g) {
		switch (highlight) {
		case NONE:
			break;
		case BLUE:
			if (mousedOver) {
				g.setColor(lightBlueColor);
			} else {
				g.setColor(blueColor);
			}
			break;
		case RED:
			if (mousedOver) {
				g.setColor(lightRedColor);
			} else {
				g.setColor(redColor);
			}
			break;
		case GREEN:
			if (mousedOver) {
				g.setColor(lightGreenColor);
			} else {
				g.setColor(greenColor);
			}
			break;

		}
		
		if (highlight != Highlight.NONE) {
			Point px = Map.mapToPixelCoords(new Point(mapX, mapY));
			g.fillRect((int) px.getX(), (int) px.getY(), Map.TILESIZE,
					Map.TILESIZE);
		}
		
		// terrain debugging
		if(hasTerrain && isOccupied){
			g.setColor(new Color(.5f, .5f, .5f, .4f));

			Point px = Map.mapToPixelCoords(new Point(mapX, mapY));
			g.fillRect((int) px.getX(), (int) px.getY(), Map.TILESIZE,
					Map.TILESIZE);
		}
	}
	
	/**
	 * Resets the tile back to default state.
	 */
	public void setEmpty() {
		setHasShip(false, null);
		setIsOccupied(false);
		removeTerrain();
		setHighlight(Highlight.NONE);
	}
	
	/*
	 * Basic Getters and Setters
	 */
	public boolean getIsOccupied() {
		return isOccupied;
	}

	public void setIsOccupied(boolean trueIfTileIsOccupied) {
		isOccupied = trueIfTileIsOccupied;
	}

	public Highlight getHighlight() {
		return highlight;
	}

	public void setHighlight(Highlight newHighlight) {
		highlight = newHighlight;
	}

	public void setMousedOver(boolean b) {
		mousedOver = b;
	}

	public boolean getHasTerrain() {
		return hasTerrain;
	}
	
	public void setHasTerrain(boolean newHasTerrainValue){
		hasTerrain = newHasTerrainValue;
	}

	public Terrain getTerrain(){
		return terrain;
	}
	
	/**
	 * Automatically handles the tile being occupied.
	 */
	public void setTerrain(boolean trueIfTerrainCanBeOccupied, Terrain newTerrain) {
		hasTerrain = true;
		terrain = newTerrain;
		if (!trueIfTerrainCanBeOccupied)
			setIsOccupied(true);
	}
	
	public void removeTerrain(){
		hasTerrain = false;
		terrain = null;
		if(!hasShip)
			isOccupied = false;
	}

	public boolean getHasShip() {
		return hasShip;
	}

	/**
	 * Automatically handles the tile being occupied.
	 */
	public void setHasShip(boolean newHasShip, Ship theShip) {
		hasShip = newHasShip;
		ship = theShip;
		setIsOccupied(newHasShip);
	}

	

	public Ship getShip() {
		if (!hasShip)
			System.out
					.println("This tile does not have a Ship to return. Considering calling getHasShip() to see if it has a Ship first.");
		return ship;
	}
	
	/*
	 * End of basic Setters and Getters
	 */
	
	/*
	 * Methods for shortestPath algorithm.
	 */
	
	public void setDistance(int newDistance){
		distance = newDistance;
	}
	
	public int getDistance(){
		return distance;
	}
	
	public void setPreviousTile(Tile aTile){
		prev = aTile;
	}
	
	public Tile getPreviousTile(){
		return prev;
	}

	public Point getLocation(){
		return new Point(mapX, mapY);
	}
	
	@Override
	public int compareTo(Tile other) {
		if(this.distance == other.getDistance())
			return 0;
		else if(this.distance > other.getDistance())
			return 1;
		else if(this.distance < other.getDistance())
			return -1;
		else{
			System.out.println("Something went wrong with the comparator!");
			return -999;
		}
	}
	
	/*
	 * End of methods for shortestPath algorithm
	 */
	
	
	/*
	 * toString()
	 * 
	 * Breakdown of what letters mean.  Please don't delete commented println's since they can be used to debug in the future.
	 * S = Scout
	 * F = Fighter
	 * B = Bomber
	 * C = Cruiser
	 * M = Mothership
	 * E = Engineer
	 * X = Sniper
	 * A = Asteroid Terrain
	 * P = Planet Terrain
	 * W = WarpGate Terrain
	 */
	
	public String toString(){
		String result = "";
		if(hasShip && ship instanceof Scout){
			result += "S";
		}
		else if(hasShip && ship instanceof Fighter){
			result += "F";
		}
		else if(hasShip && ship instanceof Bomber){
			result += "B";
		}
		else if(hasShip && ship instanceof BattleCruiser){
			result += "C";
		}
		else if(hasShip && ship instanceof Mothership){
			result += "M";
		}
		else if(hasShip && ship instanceof RepairShip){
			result += "E";
		}
		else if(hasShip && ship instanceof WarpGateShip){
			result += "W";
		}
//		else if(hasShip && ship instanceof Sniper){
//			result += "X";
//		}
		else if(hasTerrain && terrain instanceof AsteroidTerrain){
			result += "A";
		}
		else if(hasTerrain && terrain instanceof PlanetTerrain2x2){
			result += "P";
		}
		else if(hasTerrain){
			result += "p";
		}
		else if(!isOccupied){
			result += "0";
		}
		else{
			System.out.println("Some error happened reading this Tile.  Placed 1 in for spot.");
			result += "1";
		}
		return result;
	}
}
