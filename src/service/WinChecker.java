package service;

import model.*;
import java.util.ArrayList;
import java.util.List;
public class WinChecker {
	
	public boolean checkHorizontal(Board board, int playerId) {
		for (int i = 0; i < board.ROWS; i++) {
			for (int j = 0; j < board.COLS - 3; j++) {
				int cell1 = board.getCell(i, j);
				int cell2 = board.getCell(i, j+1);
				int cell3 = board.getCell(i, j+2);
				int cell4 = board.getCell(i, j+3);
				
				if (cell1 == playerId && cell2 == cell1 && cell3 == cell1 && cell4 == cell1) {
					return true;
				}
				
			}
		}
		return false;
	}
	
	public boolean checkVertical(Board board, int playerId) {
		for (int i = 0; i < board.COLS; i++) {
			for (int j = 0; j <= board.ROWS - 4; j++) {
				int cell1 = board.getCell(j, i);
				int cell2 = board.getCell(j+1, i);
				int cell3 = board.getCell(j+2, i);
				int cell4 = board.getCell(j+3, i);
				
				if (cell1 == playerId && cell2 == cell1 && cell3 == cell1 && cell4 == cell1) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkDiagonalDescending(Board board, int playerId) {
		for (int i = 0; i <= board.ROWS - 4; i++) {
			for (int j = 0; j <= board.COLS - 4; j++) {
				int cell1 = board.getCell(i, j);
				int cell2 = board.getCell(i+1, j+1);
				int cell3 = board.getCell(i+2, j+2);
				int cell4 = board.getCell(i+3, j+3);
				
				if (cell1 == playerId && cell2 == cell1 && cell3 == cell1 && cell4 == cell1) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkDiagonalAscending(Board board, int playerId) {
		for (int i = board.ROWS - 1; i >= 3; i--) {
			for (int j = 0; j <= board.COLS - 4; j++) {
				int cell1 = board.getCell(i, j);
				int cell2 = board.getCell(i-1, j+1);
				int cell3 = board.getCell(i-2, j+2);
				int cell4 = board.getCell(i-3, j+3);
				
				if (cell1 == playerId && cell2 == cell1 && cell3 == cell1 && cell4 == cell1) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hasWon(Board board, int playerId) {
		return (checkVertical(board, playerId) || checkHorizontal(board, playerId) || checkDiagonalAscending(board, playerId) || checkDiagonalDescending(board, playerId));
	}
	
	public List<int[]> getWinningCells(Board board, int playerId) {

	    // Vérification horizontale
	    for (int row = 0; row < Board.ROWS; row++) {
	        for (int col = 0; col <= Board.COLS - 4; col++) {

	            if (isFourConnected(board, playerId,
	                    row, col, 0, 1)) {

	                return createWinningCells(row, col, 0, 1);
	            }
	        }
	    }

	    // Vérification verticale
	    for (int row = 0; row <= Board.ROWS - 4; row++) {
	        for (int col = 0; col < Board.COLS; col++) {

	            if (isFourConnected(board, playerId,
	                    row, col, 1, 0)) {

	                return createWinningCells(row, col, 1, 0);
	            }
	        }
	    }

	    // Diagonale descendante ↘
	    for (int row = 0; row <= Board.ROWS - 4; row++) {
	        for (int col = 0; col <= Board.COLS - 4; col++) {

	            if (isFourConnected(board, playerId,
	                    row, col, 1, 1)) {

	                return createWinningCells(row, col, 1, 1);
	            }
	        }
	    }

	    // Diagonale ascendante ↗
	    for (int row = 3; row < Board.ROWS; row++) {
	        for (int col = 0; col <= Board.COLS - 4; col++) {

	            if (isFourConnected(board, playerId,
	                    row, col, -1, 1)) {

	                return createWinningCells(row, col, -1, 1);
	            }
	        }
	    }

	    return new ArrayList<>();
	}
	
	private boolean isFourConnected(
	        Board board,
	        int playerId,
	        int row,
	        int col,
	        int rowDirection,
	        int colDirection) {

	    for (int i = 0; i < 4; i++) {

	        int currentRow = row + i * rowDirection;
	        int currentCol = col + i * colDirection;

	        if (board.getCell(currentRow, currentCol) != playerId) {
	            return false;
	        }
	    }

	    return true;
	}
	
	private List<int[]> createWinningCells(
	        int row,
	        int col,
	        int rowDirection,
	        int colDirection) {

	    List<int[]> cells = new ArrayList<>();

	    for (int i = 0; i < 4; i++) {
	        cells.add(new int[] {
	            row + i * rowDirection,
	            col + i * colDirection
	        });
	    }

	    return cells;
	}
	
	
}













