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
//		if(suppressed != true)
//			return Robot.getLightValue() > 50;
//		return false;
		if(Robot.reachGoal() ){
			System.out.println("reach goal");
			return true;
		}
		return Robot.getLightValue() > 50;
	}

	@Override
	public void action() {
		suppressed = false;
		Robot.returnToStart();
		System.exit(0);
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}