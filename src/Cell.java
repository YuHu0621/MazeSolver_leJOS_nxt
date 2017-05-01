
/**
 * Cell class implements Comparable
 * @author Caitlin and yuhu
 *
 */
public class Cell implements Comparable<Cell>{
	//instant variable
	private int row;
	private int col;
	private boolean visited;
	private int heuristic;
	private int value;
	private boolean visitedAllNeighbor;
	//constructor
	public Cell(int r, int c) {
		row = r;
		col = c;
		visited = false;
		heuristic = 0;
		value = 0;
		visitedAllNeighbor = false;
	}

	/**
	 * set the cell as visited cell
	 */
	public void setVisited() {
		visited = true;
	}

	/**
	 * get the row of the cell
	 * @return return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * get col of the cell
	 * @return return col
	 */
	public int getCol() {
		return col;
	}

	/**
	 * return true if robot has visited the cell
	 * @return return true if robot has visited cell
	 */
	public boolean visited() {
		return visited;
	}
	

	/**
	 * set true if all of its neighbor has been explored.
	 */
	public void setVisitedAllNeighbor(){
		visitedAllNeighbor = true;
	}
	
	/**
	 * return true if all of its neighbor has been explored.
	 * @return return ture if all of its neighbors have been explored.
	 */
	public boolean visitedAllNeighbor(){
		return visitedAllNeighbor;
	}
	
	/**
	 * get manhatten distance of the cell.
	 * @return return the heuristic
	 */
	public int getHeuristic()
	{
		return heuristic;
	}
	
	/**
	 * set the heuristic of the cell.
	 * @param h
	 */
	public void setHeuristic(int h)
	{
		heuristic = h;
	}
	
	/**
	 * set int value of the cell.
	 * @param v int value.
	 */
	public void setInt(int v)
	{
		value = v;
	}
	
	/**
	 * get int value of the cell.
	 * @return
	 */
	public int getInt()
	{
		return value;
	}

	/**
	 * compare cell based on the intVal.
	 */
	@Override
	public int compareTo(Cell c) {
		if(this.getInt()<c.getInt())
		{
			return -1;
		}
		else if(this.getInt()>c.getInt())
		{
			return 1;
		}
		return 0;
	}
}
