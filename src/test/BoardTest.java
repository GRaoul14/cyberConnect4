package test;
import model.*;
import service.*;

public class BoardTest {
	public static void main(String[] args) {
		Board board = new Board();
		WinChecker winchecker = new WinChecker();
		
		board.dropToken(0, 1);
		board.dropToken(1, 2);
		
		System.out.println(board.getCell(5, 0));
		System.out.println(board.getCell(5, 1));
	}

}
