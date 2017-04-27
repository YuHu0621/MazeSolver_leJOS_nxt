import java.util.Stack;
import java.util.Vector;

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
	private static Cell[][] robotArray;
	private static Cell goal;
	public static Cell currCell;
	private static int orientation = 0;
	public static UltrasonicSensor uSensor = new UltrasonicSensor(SensorPort.S3);
	public static LightSensor lSensor = new LightSensor(SensorPort.S4);
	public static TouchSensor tSensor = new TouchSensor(SensorPort.S1);
	private static Stack<Cell> botPath = new Stack<Cell>();
	
	

	private static final int EAST = 0;
	private static final int NORTH = 1;
	private static final int WEST = 2;
	private static final int SOUTH = 3;
	
	public static boolean isReturning;

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
	
	public static void move()
	{
		if(!Robot.isReturning)
		{
			int downCell;
			int upCell;
			
			int count = 0;
			
			if(legalPosition(robotArray[currCell.getRow()+1][currCell.getCol()])&&!isWall(robotArray[currCell.getRow()+1][currCell.getCol()]))
			{
				count++;
				downCell = robotArray[currCell.getRow()+1][currCell.getCol()].getInt();
			}
			if(legalPosition(robotArray[currCell.getRow()-1][currCell.getCol()])&&!isWall(robotArray[currCell.getRow()-1][currCell.getCol()]))
			{
				count++;
				upCell = robotArray[currCell.getRow()-1][currCell.getCol()].getInt();
			}
			
			
			int forwardCell = robotArray[currCell.getRow()][currCell.getCol()+1].getInt();
			int backCell = robotArray[currCell.getRow()][currCell.getCol()-1].getInt();
			
			
		
			int min = Math.min(downCell, Math.min(upCell, Math.min(forwardCell, backCell)));
		
			if(min == forwardCell)
			{
				if(orientation == NORTH)
				{
					turnRight();
				}
			
				else if(orientation == SOUTH)
				{
					turnLeft();
				}
			
				else if(orientation == WEST)
				{
					turnLeft();
					turnLeft();
				}
			
				else
				{
					robot.travel(-20);
				}
			}
			else if(min == backCell)
			{
				if(orientation == NORTH)
				{
					turnLeft();
				}
			
				else if(orientation == SOUTH)
				{
					turnRight();
				}
				
				else if(orientation == EAST)
				{
					turnLeft();
					turnLeft();
				}
				else
				{
					robot.travel(-20);
				}
			}
			else if(min == upCell)
			{
				if(orientation == WEST)
				{
					turnRight();
				}
			
				else if(orientation == EAST)
				{
					turnLeft();
				}
			
				else if(orientation == SOUTH)
				{
					turnLeft();
					turnLeft();
				}
				else
				{
					robot.travel(-20);
				}
			}
			else if(min == downCell)
			{
				if(orientation == WEST)
				{
					turnLeft();
				}
			
				else if(orientation == EAST)
				{
					turnRight();
				}
			
				else if(orientation == NORTH)
				{
					turnLeft();
					turnLeft();
				}
				else
				{
					robot.travel(-20);
				}
			}
		}
		else
			robot.travel(-20);
	}
	
	public static boolean isWall(Cell c)
	{
		if(c.getInt()==-1)
			return true;
		else
			return false;
	}
	
	public static void resetRobotWorld()
	{
		for(int r = 0; r < 7; r++){
			for(int c = 0; c < 10; c++){
				if(!isWall(robotArray[r][c]))
					robotArray[r][c].setInt(20);
		
			}
		}
		goal.setInt(0);
	}
	
	public static void setRobotStepsFromGoal(Cell c)
	{
		resetRobotWorld();
		Vector<Cell> frontier = new Vector<Cell>();
		frontier.addElement(c);
		
		while(!frontier.isEmpty())
		{
			Cell checkCell = frontier.elementAt(0);
			frontier.removeElementAt(0);
			
			// Check cell to the left
			if(checkCell.getRow()-1 >=0 && checkCell.getInt()+1<robotArray[checkCell.getRow()-1][checkCell.getCol()].getInt()&&!isWall(robotArray[checkCell.getRow()-1][checkCell.getCol()])){
				frontier.addElement(robotArray[checkCell.getRow()-1][checkCell.getCol()]);
				robotArray[checkCell.getRow()-1][checkCell.getCol()].setInt(calculateScore(checkCell, robotArray[checkCell.getRow()-1][checkCell.getCol()]));
			}
			
			// Check cell above
			if(checkCell.getCol()-1 >=0 && checkCell.getInt()+1<robotArray[checkCell.getRow()][checkCell.getCol()-1].getInt()&&!isWall(robotArray[checkCell.getRow()][checkCell.getCol()-1])){
				frontier.addElement(robotArray[checkCell.getRow()][checkCell.getCol()-1]);
				robotArray[checkCell.getRow()][checkCell.getCol()-1].setInt(calculateScore(checkCell, robotArray[checkCell.getRow()][checkCell.getCol()-1]));
			}
			
			// Check cell to the right
			if(checkCell.getRow() +1 < 6 && checkCell.getInt()+1<robotArray[checkCell.getRow()+1][checkCell.getCol()].getInt()&&!isWall(robotArray[checkCell.getRow()+1][checkCell.getCol()])){
				frontier.addElement(robotArray[checkCell.getRow()+1][checkCell.getCol()]);
				robotArray[checkCell.getRow()+1][checkCell.getCol()].setInt(calculateScore(checkCell, robotArray[checkCell.getRow()+1][checkCell.getCol()]));
			}
			
			// Check cell below
			if(checkCell.getCol()+1< 9 && checkCell.getInt()+1<robotArray[checkCell.getRow()][checkCell.getCol()+1].getInt()&&!isWall(robotArray[checkCell.getRow()][checkCell.getCol()+1])){
				frontier.addElement(robotArray[checkCell.getRow()][checkCell.getCol()+1]);
				robotArray[checkCell.getRow()][checkCell.getCol()+1].setInt(calculateScore(checkCell, robotArray[checkCell.getRow()][checkCell.getCol()+1]));

			}
		}
	}
	
	public static int calculateScore(Cell current, Cell next)
	{
		return current.getInt()-current.getHeuristic()+1+next.getHeuristic();
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
			currCell = robotArray[row][col + 1];
		} else if (orientation == NORTH) {
			currCell = robotArray[row - 1][col];
		} else if (orientation == WEST) {
			currCell = robotArray[row][col - 1];
		} else if (orientation == SOUTH) {
			currCell = robotArray[row + 1][col];
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
	
		Cell currLocation = botPath.pop();
		//Cell currLocation = new Cell(4,7);
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
					move();
					
				}
				else if (goalX > currX){
					turnRight();
					move();
					
				}
				else {
					if (goalY > currY){
						move();
						
					}
					else{
						backward();
						
					}
				}
			} else if (orientation == NORTH) {
				if (goalY < currY){
					turnLeft();
					move();
					
				}
				else if (goalY > currY){
					turnRight();
					move();
					
				}
				else {
					if (goalX < currX){
						move();
						
					}
					else{
						backward();
						
					}
				}
			} else if (orientation == WEST) {
				if (goalX > currX){
					turnLeft();
					move();
					
				}
				else if (goalX < currX){
					turnRight();
					move();
					
				}
				else {
					if (goalY < currY){
						move();
						
					}
					else{
						backward();
						
					}
				}
			} else if (orientation == SOUTH) {
				if (goalY > currY){
					
					turnLeft();
					move();
					
				}
				else if (goalY < currY){
					
					turnRight();
					move();
					
				}
				else {
					if (goalX > currX){
						move();
					}
					else{
						backward();
					}
				}
			}
			currLocation = c;
		}
		

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
	
	public static boolean reachGoal(){
		
		return currCell.getCol() == 7 && currCell.getRow() == 4;
	
	}
	
	public static void arraySetup()
	{
		for(int i=0; i<robotArray.length; ++i)
		{
			for(int j=0; j<robotArray[0].length; ++j)
			{
				robotArray[i][j]=new Cell(i, j);
				robotArray[i][j].setHeuristic(getManhattanDistance(robotArray[i][j]));
			}
		}
	}
	
	public static int getManhattanDistance(Cell current)
	{
		//System.out.println((Math.abs(goal.getRow()-current.getRow()))+Math.abs(goal.getCol()-current.getCol()));
		return (Math.abs(4-current.getRow()))+Math.abs(7-current.getCol());
	}
	private static boolean legalPosition(Cell c) {
		int x = c.getRow();
		int y = c.getCol();
        if (x < 0) {
        	return false;
        }
        if (y < 0) {
        	return false;
        }
        if (x > (5 - 1)) {
        	return false;
        }
        if (y > (8 - 1)) {
        	return false;
        }
        return true;
    }
	public static void setWall()
	{
		if(orientation == EAST)
			robotArray[currCell.getRow()][currCell.getCol()+1].setInt(200);

		if(orientation == NORTH)
			robotArray[currCell.getRow()-1][currCell.getCol()].setInt(200);
				
		if(orientation == WEST)
			robotArray[currCell.getRow()][currCell.getCol()-1].setInt(200);
		
		if(orientation == SOUTH)
			robotArray[currCell.getRow()+1][currCell.getCol()].setInt(200);
		
		setRobotStepsFromGoal(goal);
	}

	// array is here
	public static void main(String[] args) {
		robotArray = new Cell[5][8];
		arraySetup();
		currCell = robotArray[0][0];
		goal = robotArray[4][7];

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
