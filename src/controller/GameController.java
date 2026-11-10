package controller;

import java.net.URL;
import java.util.ResourceBundle;
import model.*;
import service.AIPlayer;
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
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private Button backToMenuButton;
    @FXML
    private StackPane boardContainer;
    @FXML
    private Pane winningLinePane;

    private Game game;
    private Circle[][] circles = new Circle[Board.ROWS][Board.COLS];
    private boolean animating = false;
    private AIPlayer aiPlayer = new AIPlayer();
    private String difficulty = "MEDIUM";
    private String gameMode = "PVP";

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
        titleLabel.setText("Cyber Connect 4 — Player 1");
    }
    
    @FXML
    public void backToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 650);

        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Cyber Connect 4");
        stage.show();

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
                    // Do nothing if an animation is in progress
                    // or if the game is over.
                    if (animating || game.isGameOver()) {
                        return;
                    }

                    // The human player uses the red tokens.
                    int player = game.getCurrentPlayer();

                    // Find the first available cell starting from the bottom.
                    int line = -1;

                    for (int x = Board.ROWS - 1; x >= 0; x--) {
                        if (game.getBoard().getCell(x, column) == 0) {
                            line = x;
                            break;
                        }
                    }

                    boolean succes = game.playTurn(column);

                    if (succes && line != -1) {
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

        // Create the temporary token.
        Circle token = new Circle(25);
        token.setFill(color);

        // Get the actual position of the target cell.
        double targetY = target.getLayoutY();

        // Place the temporary token in the selected column.
        GridPane.setRowIndex(token, 0);
        GridPane.setColumnIndex(token, column);

        boardGrid.getChildren().add(token);

        // Wait for JavaFX to calculate the positions of the elements.
        boardGrid.applyCss();
        boardGrid.layout();

        // Move vertically towards the target cell.
        double distance = targetY - token.getLayoutY();

        token.setTranslateY(-distance);

        TranslateTransition transition = new TranslateTransition(Duration.millis(500), token);

        transition.setToY(0);

        transition.setOnFinished(event -> {
            boardGrid.getChildren().remove(token);

            // Display the actual board state.
            updateBoard();

            animating = false;

            if (game.isGameOver()) {

                if (game.getWinner() != 0) {
                    int winner = game.getWinner();
                    drawWinningLine(winner);

                    // Wait one second before displaying the victory screen.
                    PauseTransition pause =
                            new PauseTransition(Duration.seconds(1));

                    pause.setOnFinished(e -> showVictoryScreen());

                    pause.play();
                } else {
                    showVictoryScreen();
                }
                animating = false;

            } else {
                animating = false;

                // Trigger the AI if it is Player 2's turn.
                // Human clicks are blocked while the AI is playing.
                if (gameMode.equals("PVE") && game.getCurrentPlayer() == 2) {
                    animating = true;
                    titleLabel.setText("Cyber Connect 4 — AI is thinking...");

                    PauseTransition pause =
                            new PauseTransition(Duration.seconds(0.5));

                    pause.setOnFinished(e -> playAITurn());

                    pause.play();

                } else {

                    titleLabel.setText("Cyber Connect 4 — Player " + game.getCurrentPlayer());
                }
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

            // Get the victory screen controller.
            VictoryController controller = loader.getController();

            // Pass the winner to the controller.
            controller.setWinner(game.getWinner());

            controller.setGameSettings(this.gameMode, this.difficulty);

            // Get the current window.
            Stage stage = (Stage) boardGrid.getScene().getWindow();

            // Replace the current scene.
            Scene scene = new Scene(root);

            // Load the CSS stylesheet.
            scene.getStylesheets().add(
                    getClass().getResource("/view/style.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setTitle("Cyber Connect 4 — Results");

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

        // Make sure the dimensions are up to date.
        boardContainer.applyCss();
        boardContainer.layout();

        // Get the centers of the tokens in the coordinate system
        // of the pane that will contain the line.
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

    private void playAITurn() {

        // Check whether the game is still running
        // and whether it is actually the AI's turn.
        if (game.isGameOver() || game.getCurrentPlayer() != 2) {
            return;
        }

        // The AI chooses a column.
        int column;

        switch (difficulty) {
            case "EASY":
                column = aiPlayer.chooseEasyColumn(game.getBoard());
                break;

            case "MEDIUM":
                column = aiPlayer.chooseMediumColumn(game.getBoard(), 2, 1);
                break;

            case "HARD":
                // The hard difficulty strategy is implemented here.
                column = aiPlayer.chooseHardColumn(game.getBoard(), 2, 1);
                break;

            default:
                column = aiPlayer.chooseMediumColumn(game.getBoard(), 2, 1);
                break;
        }

        // No available column.
        if (column == -1) {
            animating = false;
            return;
        }

        // Find the cell where the token will land.
        int line = -1;

        for (int row = Board.ROWS - 1; row >= 0; row--) {

            if (game.getBoard().getCell(row, column) == 0) {
                line = row;
                break;
            }
        }

        if (line == -1) {
            animating = false;
            return;
        }

        // Register the move in the model.
        boolean success = game.playTurn(column);

        if (success) {

            animating = true;

            // Play the drop sound using SoundManager.
            audio.SoundManager.playDrop();

            // The AI uses the yellow tokens.
            animateToken(line, column, Color.YELLOW);
        }
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameMode() {
        return gameMode;
    }

    public String getDifficulty() {
        return difficulty;
    }
}