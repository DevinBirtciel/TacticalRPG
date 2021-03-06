package specific_ships_items;

import java.awt.Point;

import model.Ship;
import shipVisuals.WarpVisual;

public class WarpGateShip extends Ship {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7499370162619467007L;

	public WarpGateShip(Point newLocation){
		super(newLocation);
		String description = "Faster-than-light\ntransport.";
		this.constructorAid("WarpGate", 0, 1, 0, 1, 0, 0, description, 0, 0, 0, 0, false, 0);
		
		setVisual(new WarpVisual(this));
	}
	
//	@Override
//	public void setTeam(int newTeam){
//		if(newTeam == 0){
//			super.setTeam(newTeam);
//			setFileName("wormholeBlue.png");
//			setVisual(new WarpVisual(this));
//		}
//		else if(newTeam == 1){
//			super.setTeam(newTeam);
//			setFileName("wormholeRed.png");
//			setVisual(new WarpVisual(this));
//		}
//		else if(newTeam == 2){
//			super.setTeam(newTeam);
//			setFileName("wormholeGreen.png");
//			setVisual(new WarpVisual(this));
//		}
//	}
}
