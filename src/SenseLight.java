import lejos.robotics.subsumption.Behavior;

/**
 * Sense light behavior detect the goal and call returnToStart method.
 * 
 * @author yuhu
 *
 */
public class SenseLight implements Behavior {

	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		if(suppressed != true)
			return Robot.getLightValue() > 50;
		return false;
	}

	@Override
	public void action() {
		suppressed = true;
		Robot.stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}