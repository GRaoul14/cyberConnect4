package test;
import service.*;
import model.*;

public class WinCheckerTest {
	public static void main(String[] args) {
		Board board = new Board();
		WinChecker winchecker = new WinChecker();
		
		System.out.println(board.dropToken(0, 1));
		System.out.println(board.dropToken(1, 1));
		System.out.println(board.dropToken(2, 1));
		
		System.out.println(winchecker.hasWon(board, 1));
	}
}
