import lejos.robotics.subsumption.Behavior;

/**
 * back track is the default behavior. When there's no unvisited cell to move to, robot starts to backtrack.
 * @author CaitlinCoggins and yuhu
 *
 */
public class Backtrack implements Behavior{
	private boolean suppressed = false;
	@Override
	public void action() {
		suppressed = false;
		Robot.backtrack();
		if(!suppressed){
			Thread.yield();
		}
		
	}

	@Override
	public void suppress() {
		suppressed = true;
		
	}

	@Override
	public boolean takeControl() {
		return true;
	}

}
