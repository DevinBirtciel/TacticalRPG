package specific_ships_items;

import java.util.List;

import utils.Observer;
import view.Explosion;
import actions.TimerAction;
import model.Ability;
import model.Castable;
import model.Ship;

public class MineAbility extends Ability {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5829288804288134273L;
	private static int DAMAGE = 35;
	public static int RANGE = 3;

	public MineAbility() {
		super("Explode", "Mine explodes, dealing\n" + DAMAGE
				+ " damage to all enemies \nwithin " + RANGE + " tiles.",
				Castable.TargetType.NONE, 0, 1);

	}
	
	public void useWithoutTarget(Observer notifyWhenDone){
		int team = 0;
		if(getOwner().getTeam() == 0)
			team = 1;
		
		List<Ship> enemies = getOwner().getLevel().getTargettableShipsWithinCircularArea(getOwner(), RANGE, team);
		for(Ship s : enemies){
			s.updateHull(-DAMAGE);
			s.getVisual().updateDisplayHealth();
		}
		getOwner().setHull(0);

		// placeholder animation
		TimerAction timer = new TimerAction(30, notifyWhenDone);
		addChild(timer);
		timer.start();
		
		addChild(new Explosion(4, 15, getOwner().getVisual()));
	}

}
