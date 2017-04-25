import lejos.robotics.subsumption.Behavior;

/**
 * MoveForward is the default behavior that is not suppressed unless the robot
 * is out of the edge or run into wall or get to goal.
 * 
 * @author yuhu
 *
 */
public class MoveForward implements Behavior {
	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		// Stop moving forward if it's going to move out of bound.
		if (Robot.currCell.getCol() != 7 && Robot.currCell.getRow() != 4)
			return true;
		return false;
	}

	@Override
	public void action() {
		suppressed = false;
		
		while (!suppressed || Robot.isMoving()) {
			Thread.yield();
			if (Robot.canMoveForward()) {
				Robot.forward();
			} else {
				Robot.turnRight();
				if (Robot.canMoveForward()) {
					Robot.forward();
				} else {
					Robot.turnRight();
					Robot.turnRight();
					if (Robot.canMoveForward()) {
						Robot.forward();
					} else {
						Robot.turnLeft();
						Robot.forward();
					}
				}
			}
			Robot.updateCellPath();
		}
	}

	@Override
	public void suppress() {
		System.out.println("moving suppressed");
		suppressed = true;
	}

}
