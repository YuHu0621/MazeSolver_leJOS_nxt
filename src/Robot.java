import java.util.Stack;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.ArcMoveController;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * A robot with a light sensor and an Ultrasonic sensor.
 * 
 * @author yuhu
 *
 */
public class Robot {

	private static ArcMoveController robot = new DifferentialPilot(5.6f, 11.0f, Motor.A, Motor.C, true);
	// row and col of starting cell
	public static Cell currCell = new Cell(0, 0);
	private static int orientation = 0;
	public static UltrasonicSensor uSensor = new UltrasonicSensor(SensorPort.S3);
	public static LightSensor lSensor = new LightSensor(SensorPort.S4);
	public static TouchSensor tSensor = new TouchSensor(SensorPort.S1);
	private static Stack<Cell> botPath = new Stack<Cell>();
	

	private static final int EAST = 0;
	private static final int NORTH = 1;
	private static final int WEST = 2;
	private static final int SOUTH = 3;
	
	public static boolean isReturning = false;

	/**
	 * Check if the bot is moving.
	 * 
	 * @return return true if the bot is moving.
	 */
	public static boolean isMoving() {
		return robot.isMoving();
	}

	/**
	 * Bot turn left and update orientation.
	 * 
	 */
	public static void turnLeft() {
	//	System.out.println("turn left");
		orientation = (orientation + 1) % 4;
		((DifferentialPilot) robot).rotate(130);
	//	System.out.println(orientation);
	}

	/**
	 * Bot turn right and update orientation.
	 */
	public static void turnRight() {
		//System.out.println("turn left");
		orientation = (orientation - 1 + 4) % 4;
		((DifferentialPilot) robot).rotate(-130);
		//System.out.println(orientation);
	}

	/**
	 * Bot go forward.
	 */


	public static void forward(){
		//System.out.println(isReturning);
		if(isReturning){
		//	System.out.println("forward");
			robot.travel(-20);
		}else {
			if (Robot.canMoveForward()) {
			robot.travel(-20);
		} else {
			turnRight();
			if (Robot.canMoveForward()) {
				robot.travel(-20);
			} else {
				turnLeft();
				turnLeft();
				if (Robot.canMoveForward()) {
					robot.travel(-20);
				} else {
					turnLeft();
					robot.travel(-20);
				}
			}
		}
		
			updateCellPath();
		}
	}
		
	/**
	 * Bot go backward.
	 */
	public static void backward() {
		//System.out.println("backward");
		robot.travel(20);
		//System.out.println(orientation);
	}

	/**
	 * Check if the robot is walking within the maze
	 * 
	 * @return return false if the robot is going to fall out of the edge.
	 */
	public static boolean canMoveForward() {
		int row = currCell.getRow();
		int col = currCell.getCol();
		if (orientation == EAST) {
			if (col + 1 >= 8)
				return false;

		} else if (orientation == NORTH) {
			if (row - 1 <= 0)
				return false;

		} else if (orientation == WEST) {
			if (col - 1 <= 0)
				return false;
		} else if (orientation == SOUTH) {
			if (row + 1 >= 5)
				return false;

		}
		return true;
	}

	/**
	 * Update the botPath.
	 */
	public static void updateCellPath() {
		int row = currCell.getRow();
		int col = currCell.getCol();
		botPath.push(currCell);
		if (orientation == EAST) {
			currCell = new Cell(row, col + 1);
		} else if (orientation == NORTH) {
			currCell = new Cell(row - 1, col);
		} else if (orientation == WEST) {
			currCell = new Cell(row, col - 1);
		} else if (orientation == SOUTH) {
			currCell = new Cell(row + 1, col);
		}
	}

	/**
	 * light sensor method to detect goal cell
	 * 
	 * @return return light value.
	 */
	public static int getLightValue() {
		return lSensor.getLightValue();
	}

	/**
	 * stop the robot
	 */
	public static void stop() {
		Robot.stop();
	}

	/**
	 * Pops new cells of the stack moveStack and moves the robot to those
	 * locations.
	 **/
	public static void returnToStart() {
		isReturning = true;
		orientation = 3;
		System.out.println("return to start");
	
		//Cell currLocation = botPath.pop();
		Cell currLocation = new Cell(4,7);
		while (currLocation != null && !botPath.isEmpty()) {
			Sound.beep();
			Cell c = botPath.pop();

			int goalX = c.getRow();
			int goalY = c.getCol();
			
			// Figures out the direction the robot needs to move from the goal
			// state
			// and the current orientation of the robot.
			int currX = currLocation.getRow();
			int currY = currLocation.getCol();
			
			System.out.println(orientation + " " + currX + " " + currY);
			if (orientation == EAST) {
				if (goalX < currX){
					turnLeft();
					forward();
					
				}
				else if (goalX > currX){
					turnRight();
					forward();
					
				}
				else {
					if (goalY > currY){
						forward();
						
					}
					else{
						backward();
						
					}
				}
			} else if (orientation == NORTH) {
				if (goalY < currY){
					turnLeft();
					forward();
					
				}
				else if (goalY > currY){
					turnRight();
					forward();
					
				}
				else {
					if (goalX < currX){
						forward();
						
					}
					else{
						backward();
						
					}
				}
			} else if (orientation == WEST) {
				if (goalX > currX){
					turnLeft();
					forward();
					
				}
				else if (goalX < currX){
					turnRight();
					forward();
					
				}
				else {
					if (goalY < currY){
						forward();
						
					}
					else{
						backward();
						
					}
				}
			} else if (orientation == SOUTH) {
				if (goalY > currY){
					
					turnLeft();
					forward();
					
				}
				else if (goalY < currY){
					
					turnRight();
					forward();
					
				}
				else {
					if (goalX > currX){
						forward();
					}
					else{
						backward();
					}
				}
			}
			currLocation = c;
		}
		System.exit(0);

	}

	/**
	 * ultra sensor detect wall
	 * 
	 * @return return distance to the wall.
	 */
	public static int getDistance() {
		return uSensor.getDistance();
	}

	public static boolean touchWall() {
		return tSensor.isPressed();
	}

	// array is here
	public static void main(String[] args) {

		// the default behavior
		Behavior b1 = new MoveForward();
		Behavior b2 = new SenseUltrasonic();
		Behavior b3 = new SenseLight();

		Behavior[] bArray = { b1, b3, b2 };

		// create the arbitrator
		Arbitrator arby = new Arbitrator(bArray, true);

		arby.start();

		returnToStart();

	}
}
