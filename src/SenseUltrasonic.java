import lejos.robotics.subsumption.Behavior;

/**
 * Sense ultrasonic is activated when wall is detected.
 * 
 * @author yuhu
 *
 */
public class SenseUltrasonic implements Behavior {

	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return Robot.getDistance()<10 || Robot.touchWall();
	}

	@Override
	public void action() {
		suppressed = false;
		Robot.turnLeft();
		Robot.forward();
		while (Robot.isMoving()) {
			Thread.yield();
		}
	}

	@Override
	public void suppress() {
		System.out.println("bumper suppressed");
		suppressed = true;
	}

}
