
public class Cell {

	private int row;
	private int col;
	private boolean visited;
	private int heuristic;
	private int value;

	public Cell(int r, int c) {
		row = r;
		col = c;
		visited = false;
		heuristic = 0;
		value = 0;
	}

	public void setVisited() {
		visited = true;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean visited() {
		return visited;
	}
	
	public int getHeuristic()
	{
		return heuristic;
	}
	
	public void setHeuristic(int h)
	{
		heuristic = h;
	}
	
	public void setInt(int v)
	{
		value = v;
	}
	
	public int getInt()
	{
		return value;
	}
}
