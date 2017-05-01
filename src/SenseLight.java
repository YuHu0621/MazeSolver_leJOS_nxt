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

		if(Robot.reachGoal()){
			return true;
		}
		return Robot.getLightValue() > 50;
	}

	@Override
	public void action() {
		suppressed = false;
		Robot.returnToStart();
		//system exit when robot is back at the start cell.
		System.exit(0);
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}