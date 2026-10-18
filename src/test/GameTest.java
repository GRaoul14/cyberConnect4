package test;

import model.Game;

public class GameTest {
	public static void main(String[] args) {
		Game game = new Game();
		System.out.println(game.getCurrentPlayer());
		System.out.println(game.playTurn(3));
		game.getBoard().getCell(5, 3);
		System.out.println(game.getCurrentPlayer());
		System.out.println(game.isGameOver());
		System.out.println(game.playTurn(-1));
		System.out.println(game.getCurrentPlayer());
	}
}
