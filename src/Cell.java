
public class Cell {

	private int row;
	private int col;
	private boolean visited;

	public Cell(int r, int c) {
		row = r;
		col = c;
		visited = false;
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
}
