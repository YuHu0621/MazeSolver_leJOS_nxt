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
		if(Robot.getDistance()<20){
			System.out.println(Robot.getDistance());
		}
		return Robot.getDistance()<20 /*|| Robot.touchWall()*/;
	}

	@Override
	public void action() {
		suppressed = false;
		if(!Robot.isReturning){
			Robot.setWall();
			Robot.turnRight();
			Robot.forward();
		}
		//Robot.updateCellPath();
		if (Robot.isMoving()) {
			Thread.yield();
		}
	}

	@Override
	public void suppress() {
	
		suppressed = true;
	}

}
