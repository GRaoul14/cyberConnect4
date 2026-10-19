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

    @FXML private Label winnerLabel;

    @FXML private Label subtitleLabel;
    @FXML private Label victoryLabel;

    private int winner;

    @FXML
    public void initialize() {
        // Initialisation de l'écran de victoire
    }

    public void setWinner(int winner) {
        this.winner = winner;

        if (winner == 0) {
        	victoryLabel.setText("");
            winnerLabel.setText("MATCH NUL !");
            subtitleLabel.setText("Personne n'a gagné cette partie.");
        } else {
            winnerLabel.setText("JOUEUR " + winner + " A GAGNÉ !");
            subtitleLabel.setText("Félicitations pour cette victoire !");
        }
    }

    @FXML
    private void replay() {
    	SoundManager.playButton();
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/game.fxml")
            );

            Parent root = loader.load();

            Scene scene = new Scene(root);

            scene.getStylesheets().add(
                getClass().getResource("/view/style.css").toExternalForm()
            );

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

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }
}