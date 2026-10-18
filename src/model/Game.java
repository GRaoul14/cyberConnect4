package model;
import service.*;

public class Game {
	private final Board board;
	private final WinChecker winChecker;
	private int currentPlayer;
	private boolean gameOver;
	private int winner;
	
	public Game() {
		this.board = new Board();
		this.winChecker = new WinChecker();
		this.currentPlayer = 1;
		this.gameOver = false;
		this.winner = 0;
	}
	
	public boolean playTurn(int column) {
		if (gameOver) return false;
		if (!board.dropToken(column, currentPlayer)) {
			return false;
		}
		if (winChecker.hasWon(board, currentPlayer)) {
			gameOver = true;
			winner = getCurrentPlayer();
		} else if (board.isFull()) {
			gameOver = true;
		} else {
			currentPlayer = (currentPlayer == 1) ? 2 : 1;
		}
			
		return true;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public Board getBoard() {
		return board;
	}
	
	public int getWinner() {
		return winner;
	}
	
	
}
