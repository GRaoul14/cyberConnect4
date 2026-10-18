package model;

public class Board {
	public static final int ROWS = 6;
    public static final int COLS = 7;
    
    private final int[][] grid = new int[ROWS][COLS];

	public Board() {
		
	}
	
	public boolean dropToken(int column, int playerId) {
		if (column < 0 || column >= COLS || (playerId != 1 && playerId != 2)) return false;
		for (int i = ROWS-1; i >= 0; i--) {
			if(grid[i][column] == 0) {
				grid[i][column] = playerId;
				return true;
			}
		}
		return false;
	}
	
	public int getCell(int row, int column) {
		if (row < 0 || row >= ROWS || column < 0 || column >= COLS) return -1;
		return grid[row][column];
	}
	
	public boolean isFull() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (grid[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}
    
}
