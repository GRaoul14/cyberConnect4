package controller;

import java.net.URL;
import java.util.ResourceBundle;
import model.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

public class GameController implements Initializable{
	@FXML private Label player1Label;
	@FXML private Label player2Label;
	@FXML private Label titleLabel;
	@FXML private GridPane boardGrid;
	@FXML private Button restartButton;
	private Game game;
	private Circle[][] circles = new Circle[Board.ROWS][Board.COLS];
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		game = new Game();
		createBoard();
	}
	@FXML
	public void restartGame() {
		game = new Game();
		createBoard();
		titleLabel.setText("Cyber Connect 4 — Joueur 1");
	}
	
	public void createBoard() {
		boardGrid.getChildren().clear();
		
		boardGrid.getColumnConstraints().clear();
		boardGrid.getRowConstraints().clear();

		for (int j = 0; j < Board.COLS; j++) {
		    ColumnConstraints column = new ColumnConstraints();
		    column.setPercentWidth(100.0 / Board.COLS);
		    column.setFillWidth(true);
		    boardGrid.getColumnConstraints().add(column);
		}

		for (int i = 0; i < Board.ROWS; i++) {
		    RowConstraints row = new RowConstraints();
		    row.setPercentHeight(100.0 / Board.ROWS);
		    row.setFillHeight(true);
		    boardGrid.getRowConstraints().add(row);
		}
		
		for (int i = 0; i < Board.ROWS; i++) {
			for (int j = 0; j < Board.COLS; j++) {
				Circle circle = new Circle(25);
				circle.setFill(Color.LIGHTGRAY);
				circles[i][j] = circle;
				final int row = i;
				final int column = j;
				circle.setOnMouseClicked(event -> {
					boolean succes = game.playTurn(column);
					
					if (succes) {
						updateBoard();
						
						if (game.isGameOver()) {
							if (game.getWinner() != 0) {
								titleLabel.setText("Joueur " +game.getWinner()+ " a gagné");
							} else {
								titleLabel.setText("Match nul !");
							}
						} else {
							titleLabel.setText("Cyber Connect 4 — Joueur " + game.getCurrentPlayer());
						}
						
					}
				});
				boardGrid.add(circle, j, i);
			}
			
		}
	}
	
	public void updateBoard() {
		for (int i = 0; i < Board.ROWS; i++) {
			for (int j = 0; j < Board.COLS; j++) {
				int value = game.getBoard().getCell(i, j);
				if (value == 0) {
					circles[i][j].setFill(Color.LIGHTGRAY);
				} else if (value == 1) {
					circles[i][j].setFill(Color.RED);
				} else {
					circles[i][j].setFill(Color.YELLOW);
				}
			}
		}
	}
	

}
