package controller;

import audio.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class VictoryController {

    @FXML
    private Label winnerLabel;

    @FXML
    private Label subtitleLabel;
    @FXML
    private Label victoryLabel;
    private String gameMode = "PVP";
    private String difficulty = "MEDIUM";

    private int winner;

    @FXML
    public void initialize() {
        // Initialize the victory screen.
    }

    public void setWinner(int winner) {
        this.winner = winner;

        if (winner == 0) {
            victoryLabel.setText("");
            winnerLabel.setText("Draw!");
            subtitleLabel.setText("No winner");
        } else {
            winnerLabel.setText("Player " + winner + " has won!");
            subtitleLabel.setText("CONGRATULATIONS!");
        }
    }

    public void setGameSettings(String gameMode, String difficulty) {
        this.gameMode = gameMode;
        this.difficulty = difficulty;
    }

    @FXML
    private void replay() {
        SoundManager.playButton();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/game.fxml"));

            Parent root = loader.load();

            GameController gameController = loader.getController();

            gameController.setGameMode(gameMode);
            gameController.setDifficulty(difficulty);

            Scene scene = new Scene(root, 800, 650);

            scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

            Stage stage = (Stage) winnerLabel.getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Cyber Connect 4");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quitGame(javafx.event.ActionEvent event) {

        SoundManager.playButton();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        stage.close();
    }
}