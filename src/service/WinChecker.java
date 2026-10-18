package service;

import model.*;
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
	
	
}













