import lejos.robotics.subsumption.Behavior;

/**
 * Sense ultrasonic is activated when wall is detected.
 * 
 * @author CaitlinCoggins and yuhu
 *
 */
public class SenseUltrasonic implements Behavior {

	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		if(Robot.getDistance()<20){
			System.out.println(Robot.getDistance());
		}
		return Robot.getDistance()<20;
	}

	@Override
	public void action() {
		suppressed = false;
		if(!Robot.isReturning){
			Robot.setWall();
			Robot.turnLeft();
			
		}
		
		if (Robot.isMoving()) {
			Thread.yield();
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
