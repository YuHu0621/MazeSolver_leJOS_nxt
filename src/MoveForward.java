import lejos.robotics.subsumption.Behavior;

/**
 * MoveForward is the behavior that is suppressed when there's no unvisited neighbor cells to move to.
 * 
 * @author CaitlinCoggins and yuhu
 *
 */
public class MoveForward implements Behavior {
	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		if(Robot.hasUnvisitedNeighbor())
			return true;
		return false;

	}

	@Override
	public void action() {
		suppressed = false;
		Robot.move();
		if (!suppressed ) {
			Thread.yield();
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
