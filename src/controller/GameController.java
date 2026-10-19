package controller;

import java.net.URL;
import java.util.ResourceBundle;
import model.*;
import audio.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import javafx.scene.shape.Line;
import javafx.scene.effect.DropShadow;
import javafx.animation.PauseTransition;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.effect.DropShadow;
import java.util.List;


public class GameController implements Initializable {
	@FXML
	private Label player1Label;
	@FXML
	private Label player2Label;
	@FXML
	private Label titleLabel;
	@FXML
	private GridPane boardGrid;
	@FXML
	private Button restartButton;
	@FXML
	private StackPane boardContainer;
	@FXML
	private Pane winningLinePane;
	
	private Game game;
	private Circle[][] circles = new Circle[Board.ROWS][Board.COLS];
	private boolean animating = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		game = new Game();
		createBoard();
	}

	@FXML
	public void restartGame() {
		SoundManager.playButton();
		game = new Game();
		createBoard();
		titleLabel.setText("Cyber Connect 4 — Joueur 1");
	}

	public void createBoard() {
		 boardGrid.getChildren().clear();
		 winningLinePane.getChildren().clear();

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
					if (animating || game.isGameOver()) {
						return;
					}
					int player = game.getCurrentPlayer();
					int line = -1;

					for (int x = Board.ROWS - 1; x >= 0; x--) {
						if (game.getBoard().getCell(x, column) == 0) {
							line = x;
							break;
						}
					}
						
						boolean succes = game.playTurn(column);
						
						if (succes) {
							SoundManager.playDrop();
							animating = true;
							Color color = (player == 1) ? Color.RED : Color.YELLOW;
							animateToken(line, column, color);
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

	private void animateToken(int row, int column, Color color) {

		Circle target = circles[row][column];

		// Créer le pion temporaire
		Circle token = new Circle(25);
		token.setFill(color);

		// Obtenir la position réelle de la case cible
		double targetY = target.getLayoutY();

		// Placer le pion temporaire dans la colonne choisie
		GridPane.setRowIndex(token, 0);
		GridPane.setColumnIndex(token, column);

		boardGrid.getChildren().add(token);

		// Attendre que JavaFX calcule les positions des éléments
		boardGrid.applyCss();
		boardGrid.layout();

		// Déplacement vertical vers la case cible
		double distance = targetY - token.getLayoutY();

		token.setTranslateY(-distance);

		TranslateTransition transition = new TranslateTransition(Duration.millis(500), token);

		transition.setToY(0);

		transition.setOnFinished(event -> {
			boardGrid.getChildren().remove(token);

			// Afficher l'état réel du plateau
			updateBoard();

			animating = false;

			if (game.isGameOver()) {
				int winner = game.getWinner();
				
				if (winner != 0) {
					drawWinningLine(winner);

		            // Attendre une seconde avant d'afficher l'écran de victoire.
		            PauseTransition pause =
		                    new PauseTransition(Duration.seconds(1));

		            pause.setOnFinished(e -> showVictoryScreen());

		            pause.play();
				} else {
					showVictoryScreen();
				}
				
			} else {
				titleLabel.setText("Cyber Connect 4 — Joueur " + game.getCurrentPlayer());
			}
		});

		transition.play();
	}
	
	private void showVictoryScreen() {
		SoundManager.playVictory();
	    try {
	        FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/view/victory.fxml")
	        );

	        Parent root = loader.load();

	        // Récupérer le contrôleur de l'écran de victoire
	        VictoryController controller = loader.getController();

	        // Transmettre le gagnant
	        controller.setWinner(game.getWinner());

	        // Récupérer la fenêtre actuelle
	        Stage stage = (Stage) boardGrid.getScene().getWindow();

	        // Remplacer la scène actuelle
	        Scene scene = new Scene(root);

	        // Charger le CSS
	        scene.getStylesheets().add(
	            getClass().getResource("/view/style.css").toExternalForm()
	        );

	        stage.setScene(scene);
	        stage.setTitle("Cyber Connect 4 — Résultat");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void drawWinningLine(int player) {

	    List<int[]> winningCells =
	            new service.WinChecker()
	                    .getWinningCells(game.getBoard(), player);

	    if (winningCells.size() != 4) {
	        return;
	    }

	    int[] firstCell = winningCells.get(0);
	    int[] lastCell = winningCells.get(3);

	    Circle firstCircle =
	            circles[firstCell[0]][firstCell[1]];

	    Circle lastCircle =
	            circles[lastCell[0]][lastCell[1]];

	    // S'assurer que les dimensions sont à jour.
	    boardContainer.applyCss();
	    boardContainer.layout();


	    // Récupérer les centres des jetons dans les coordonnées
	    // du panneau qui accueillera la ligne.
	    javafx.geometry.Point2D start =
	            winningLinePane.sceneToLocal(
	                    firstCircle.localToScene(0, 0));

	    javafx.geometry.Point2D end =
	            winningLinePane.sceneToLocal(
	                    lastCircle.localToScene(0, 0));

	    Line line = new Line(
	            start.getX(),
	            start.getY(),
	            end.getX(),
	            end.getY()
	    );

	    line.setStroke(Color.WHITE);
	    line.setStrokeWidth(5);
	    line.setMouseTransparent(true);

	    DropShadow glow = new DropShadow();
	    glow.setColor(Color.CYAN);
	    glow.setRadius(15);

	    line.setEffect(glow);

	    winningLinePane.getChildren().clear();
	    winningLinePane.getChildren().add(line);
	}
}
