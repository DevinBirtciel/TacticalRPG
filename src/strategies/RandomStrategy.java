package strategies;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

import model.Level;
import model.Ship;

public class RandomStrategy implements Strategy, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6935141353152725294L;

	public void doNextAction(Ship ship, Level level){		
		Random random = new Random();	

		if(ship.getCanAttack()){
			List<Ship> targets = level.getPossibleTargetsForAI(ship);
			if(targets.size() >= 1){
				int randomShip = random.nextInt(targets.size());
				level.attackShip(ship, targets.get(randomShip));
			} 
			ship.setCanAttack(false);

		}
		else if(ship.getCanMove()){
			List<Point> potentialMoves = level.getPossibleMovesForAI(ship);
			
			while(potentialMoves.size() > 0){
				int randomMove = random.nextInt(potentialMoves.size());
				Point move = potentialMoves.get(randomMove);
				
				if(!ship.getLocation().equals(move)){ // don't move to the tile the ship is already on
					level.moveShipTo(ship, move.x, move.y);
					break;
				}	
				potentialMoves.remove(randomMove);
			}
			ship.setCanMove(false);
			
		}  else {
			level.waitShip(ship);
		}
		
	}
}
