package controller;

import java.io.IOException;

import audio.SoundManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button playButton;

    @FXML
    private void startGame(javafx.event.ActionEvent event) {

        SoundManager.playButton();

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/game.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 650);

            scene.getStylesheets().add(
                    getClass().getResource("/view/style.css")
                            .toExternalForm());

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(scene);
            stage.setTitle("Cyber Connect 4");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showRules() {

        SoundManager.playButton();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Comment jouer");
        alert.setHeaderText("Règles de Cyber Connect 4");

        alert.setContentText(
                "1. Chaque joueur joue à tour de rôle.\n\n"
                + "2. Cliquez sur une colonne pour déposer un jeton.\n\n"
                + "3. Alignez quatre jetons horizontalement, "
                + "verticalement ou en diagonale pour gagner.\n\n"
                + "4. Si le plateau est rempli sans gagnant, "
                + "la partie est nulle.");

        alert.showAndWait();
    }

    @FXML
    private void quitGame() {

    	Platform.exit();
		System.exit(0);
    }
}