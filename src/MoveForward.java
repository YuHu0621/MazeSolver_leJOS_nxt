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
		//System.out.println( Robot.currCell.getCol() == 7 && Robot.currCell.getRow() == 4);
//		if(Robot.currCell.getCol() == 7 && Robot.currCell.getRow() == 4 ){
//			Robot.returnToStart();
//			return false;
//		}
		return true;

	}

	@Override
	public void action() {
		suppressed = false;
		Robot.move();
		if (!suppressed /*|| Robot.isMoving()*/) {
			
			//if(!Robot.isReturning){
			
				
			//}
			Thread.yield();
			
			
		}
	}

	@Override
	public void suppress() {
		System.out.println("moving suppressed");
		suppressed = true;
	}

}
