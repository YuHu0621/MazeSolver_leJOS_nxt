import java.util.Arrays;
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
 * @author CaitlinCoggins and yuhu
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
		//rest orientation
		orientation = (orientation + 1) % 4;
		//rotate
		((DifferentialPilot) robot).rotate(136);
	
	}

	/**
	 * Bot turn right and update orientation.
	 */
	public static void turnRight() {
		//reset orientation
		orientation = (orientation - 1 + 4) % 4;
		//rotate
		((DifferentialPilot) robot).rotate(-134);

	}
	
	/**
	 * Check if the cell has unvisited neighbor
	 * @return return true if it has unvisited neighbor
	 */
	public static boolean hasUnvisitedNeighbor(){
		
		return !currCell.visitedAllNeighbor();
	}
	
	/**
	 * helper method to check the orientation of the next cell 
	 * @param nextCell the next cell
	 * @return return the orientation of the next cell
	 */
	private static int nextCellOrientation(Cell currCell, Cell nextCell){
		if(nextCell.getRow()<currCell.getRow()){
			return NORTH;
		}else if(nextCell.getRow()> currCell.getRow()){
			return SOUTH;
		}else{
			if(nextCell.getCol() < currCell.getCol()){
				return WEST;
			}else{
				return EAST;
			}
		}
	}

	/**
	 * backtrack pop cell from the botPath stack
	 * Robot go back at dead corner.
	 */
	public static void backtrack(){
		if(!botPath.isEmpty()){
			Cell nextCell = botPath.pop();
			turnToNextCell(currCell, nextCell);
			robot.travel(-20);
			currCell = nextCell;
		}
	}
	
	/**
	 * Bot go forward.
	 */
	public static void move()
	{
			//get array of unvisited neighbor cells
			Cell[] newArray = getNextUnvisitedCells();
			//if there's none, robot doesn't move.
			if(newArray.length == 0){
				return;
			}
			//set next cell 
			Cell nextCell = newArray[0];
			System.out.println(nextCell.getRow() + " " + nextCell.getCol());
			nextCell.setVisited();
			turnToNextCell(currCell, nextCell);
			
			
			if(getDistance()>20){
				robot.travel(-20);
				botPath.push(currCell);
				currCell = nextCell;
			}else{
				setWall();
			}

			
		}
		
		
		
	

	private static boolean turnToNextCell(Cell curr, Cell nextCell) {
		
		//get the orientation of the next cell
		int nextCellOrientation = nextCellOrientation(curr, nextCell);
		if(nextCellOrientation == EAST)
		{
			if(orientation == NORTH)
			{
				turnRight();
				return true;
			}
		
			else if(orientation == SOUTH)
			{
				turnLeft();
				return true;
			}
		
			else if(orientation == WEST)
			{
				turnLeft();
				turnLeft();
				return true;
			}
		
			else
			{
				//go forward, don't turn
				return false;
				
			}
		}
		else if(nextCellOrientation == WEST)
		{
			if(orientation == NORTH)
			{
				turnLeft();
				return true;
			}
		
			else if(orientation == SOUTH)
			{
				turnRight();
				return true;
			}
			
			else if(orientation == EAST)
			{
				turnLeft();
				turnLeft();
				return true;
			}
			else
			{

				return false;
			}
		}
		else if(nextCellOrientation == NORTH)
		{
			if(orientation == WEST)
			{
				turnRight();
				return true;
			}
		
			else if(orientation == EAST)
			{
				turnLeft();
				return true;
			}
		
			else if(orientation == SOUTH)
			{
				turnLeft();
				turnLeft();
				return true;
			}
			else
			{

				return false;
			}
		}
		else
		{
			if(orientation == WEST)
			{
				turnLeft();
				return true;
			}
		
			else if(orientation == EAST)
			{
				turnRight();
				return true;
			}
		
			else if(orientation == NORTH)
			{
				turnLeft();
				turnLeft();
				return true;
			}
			else
			{

				return false;
			}
		}
	}

	
	/**
	 * helper method to get an array of unvisited neighbor cells.
	 * @return return an array of unvisited neighbor cell.
	 */
	private static Cell[] getNextUnvisitedCells() {
		int count = 0;
		Cell[] min = new Cell[4];
		boolean hasUnvisitedCell = false;
		if(legalPosition(currCell.getRow()+1,currCell.getCol())&&!isWall(robotArray[currCell.getRow()+1][currCell.getCol()])  )
		{
			
			if(!robotArray[currCell.getRow()+1][currCell.getCol()].visited()){
				min[count] = robotArray[currCell.getRow()+1][currCell.getCol()];
				count++;
				hasUnvisitedCell = true;
			}
		}
		if(legalPosition(currCell.getRow()-1,currCell.getCol())&&!isWall(robotArray[currCell.getRow()-1][currCell.getCol()]) )
		{
			
			if(!robotArray[currCell.getRow()-1][currCell.getCol()].visited()){
				min[count] = robotArray[currCell.getRow()-1][currCell.getCol()];
				count++;
				hasUnvisitedCell = true;
			}
		}
		if(legalPosition(currCell.getRow(), currCell.getCol()+1)&&!isWall(robotArray[currCell.getRow()][currCell.getCol()+1])  )
		{
			
			if(!robotArray[currCell.getRow()][currCell.getCol()+1].visited()){	
				min[count] = robotArray[currCell.getRow()][currCell.getCol()+1];
				count++;
				hasUnvisitedCell = true;
			}
		}
		if(legalPosition(currCell.getRow(), currCell.getCol()-1)&&!isWall(robotArray[currCell.getRow()][currCell.getCol()-1]) )
		{
			
			if(!robotArray[currCell.getRow()][currCell.getCol()-1].visited()){
				min[count] = robotArray[currCell.getRow()][currCell.getCol()-1];
				count++;
				hasUnvisitedCell = true;
			}
		}
		
		if(hasUnvisitedCell == false){
			//the currCell has no unvisited neighbor
			//it has nowhere to go to
			//should backtrack
			currCell.setVisitedAllNeighbor();
		}
		//trim empty index in the end of the array
		Cell[] newArray = new Cell[count];
		System.arraycopy(min, 0, newArray, 0, count);
		
		//sort cell based on heuristic
		Arrays.sort(newArray);
		
		return newArray;
	}
	
	/**
	 * helper method to check if the cell is wall
	 * @param c
	 * @return
	 */
	private static boolean isWall(Cell c)
	{
		if(c.getInt()==-1)
			return true;
		else
			return false;
	}
	
	/**
	 * reset Robot world
	 */
	public static void resetRobotWorld()
	{
		for(int r = 0; r < 5; r++){
			for(int c = 0; c < 8; c++){
				if(!isWall(robotArray[r][c]))
					robotArray[r][c].setInt(20);
		
			}
		}
		goal.setInt(0);
	}
	
	/**
	 * set cell heuristics 
	 * @param c cell
	 */
	public static void setRobotStepsFromGoal(Cell c)
	{
		resetRobotWorld();
		Vector<Cell> frontier = new Vector<Cell>();
		frontier.addElement(c);
		
		while(!frontier.isEmpty())
		{
			Cell frontierCell = frontier.elementAt(0);
			frontier.removeElementAt(0);
			
			// Check cell above
			int frontierCellRow = frontierCell.getRow();
			int frontierCellIntVal = frontierCell.getInt();
			if(legalPosition(frontierCellRow-1, frontierCell.getCol())){
				Cell upCell = robotArray[frontierCellRow-1][frontierCell.getCol()];
				if(frontierCellRow-1 >=0 && frontierCellIntVal+1<upCell.getInt()&&!isWall(upCell)){
					frontier.addElement(upCell);
					upCell.setInt(calculateScore(frontierCell, upCell));
				}
			}
			// Check cell left
			if(legalPosition(frontierCellRow, frontierCell.getCol()-1)){
				Cell leftCell = robotArray[frontierCellRow][frontierCell.getCol()-1];
				if(frontierCell.getCol()-1 >=0 && frontierCellIntVal+1<leftCell.getInt()&&!isWall(leftCell)){
					frontier.addElement(leftCell);
					leftCell.setInt(calculateScore(frontierCell, leftCell));
				}
			}
			// Check cell down
			if(legalPosition(frontierCellRow+1, frontierCell.getCol())){
				Cell downCell = robotArray[frontierCellRow+1][frontierCell.getCol()];
				if(frontierCellRow +1 < 5 && frontierCellIntVal+1<downCell.getInt()&&!isWall(downCell)){
					frontier.addElement(downCell);
					downCell.setInt(calculateScore(frontierCell, downCell));
				}
			}
			// Check cell right
			if(legalPosition(frontierCellRow,frontierCell.getCol()+1)){
				Cell rightCell = robotArray[frontierCellRow][frontierCell.getCol()+1];
				if(frontierCell.getCol()+1< 8 && frontierCellIntVal+1<rightCell.getInt()&&!isWall(rightCell)){
					frontier.addElement(rightCell);
					rightCell.setInt(calculateScore(frontierCell, rightCell));
	
				}
			}
		}
	}
	
	/**
	 * helper method to calculate score of next cell based on current cell.
	 * @param current current cell
	 * @param next next cell
	 * @return return a score of the next cell
	 */
	private static int calculateScore(Cell current, Cell next)
	{
		return current.getInt()-current.getHeuristic()+1+next.getHeuristic();
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
		
		System.out.println("return to start");
	
		Cell currLocation = currCell;
		
		while (currLocation != null && !botPath.isEmpty()) {
			Sound.beep();
			Cell c = botPath.pop();
			turnToNextCell(currLocation, c);
			robot.travel(-20);
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
	
	/**
	 * check if the robot has reached goal.
	 * @return return true if the robot reached goal.
	 */
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
				robotArray[i][j].setInt(getManhattanDistance(robotArray[i][j]));
			}
		}
	}
	
	/**
	 * helper method to calculate manhattan distance 
	 * @param current current cell
	 * @return return the manhattan distance
	 */
	private static int getManhattanDistance(Cell current)
	{
		
		return (Math.abs(4-current.getRow()))+Math.abs(7-current.getCol());
	}
	
	
	private static boolean legalPosition(int x, int y) {
		
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
	
	/**
	 * set wall and recalculate heuristics of cell
	 */
	public static void setWall()
	{
		if(orientation == EAST && legalPosition(currCell.getRow(), currCell.getCol()+1))
			robotArray[currCell.getRow()][currCell.getCol()+1].setInt(-1);

		if(orientation == NORTH && legalPosition(currCell.getRow()-1, currCell.getCol()))
			robotArray[currCell.getRow()-1][currCell.getCol()].setInt(-1);
				
		if(orientation == WEST && legalPosition(currCell.getRow(),currCell.getCol()-1))
			robotArray[currCell.getRow()][currCell.getCol()-1].setInt(-1);
		
		if(orientation == SOUTH && legalPosition(currCell.getRow()+1, currCell.getCol()))
			robotArray[currCell.getRow()+1][currCell.getCol()].setInt(-1);
		
		setRobotStepsFromGoal(goal);
	}

	// array is here
	public static void main(String[] args) {
		robotArray = new Cell[5][8];
		arraySetup();
		currCell = robotArray[0][0];
		goal = robotArray[4][7];

		// the default behavior
		Behavior b0 = new Backtrack();
		Behavior b1 = new MoveForward();
		Behavior b2 = new SenseUltrasonic();
		Behavior b3 = new SenseLight();

		Behavior[] bArray = { b0, b1, b3, b2 };

		// create the arbitrator
		Arbitrator arby = new Arbitrator(bArray, true);

		arby.start();

	}
}
