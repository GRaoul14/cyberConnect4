package controller;

import java.io.IOException;

import audio.SoundManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    public void startGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mode.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();

    }

    public void startPlayerVsPlayer(ActionEvent event) throws IOException {
        startGameWithMode(event, "PVP");
    }

    public void startPlayerVsAI(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/difficulty.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 650);

        scene.getStylesheets().add(
                getClass().getResource("/view/style.css").toExternalForm()
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Cyber Connect 4");
        stage.show();
    }
    
    @FXML
    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mode.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 650);

        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Cyber Connect 4");
        stage.show();

    }
    
    private void startGameWithMode(ActionEvent event, String mode) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/game.fxml"));

        Parent root = loader.load();

        GameController controller = loader.getController();
        controller.setGameMode(mode);

        Scene scene = new Scene(root, 800, 650);
        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Cyber Connect 4");
        stage.show();
    }

    @FXML
    private void startEasy(ActionEvent event) {
        startGameWithDifficulty(event, "EASY");
    }

    @FXML
    private void startMedium(ActionEvent event) {
        startGameWithDifficulty(event, "MEDIUM");
    }

    @FXML
    private void startHard(ActionEvent event) {
        startGameWithDifficulty(event, "HARD");
    }

    private void startGameWithDifficulty(ActionEvent event, String difficulty) {
        SoundManager.playButton();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/game.fxml"));

            Parent root = loader.load();

            GameController gameController = loader.getController();

            // Select the player-versus-AI game mode.
            gameController.setGameMode("PVE");

            // Set the difficulty level.
            gameController.setDifficulty(difficulty);

            Scene scene = new Scene(root, 800, 650);

            scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Cyber Connect 4");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @FXML
    private void showRules() {
        SoundManager.playButton();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How to Play");
        alert.setHeaderText("Cyber Connect 4 Rules");

        alert.setContentText("1. Each player takes turns.\n\n"
                + "2. Click on a column to drop a token.\n\n"
                + "3. Align four tokens horizontally, "
                + "vertically or diagonally to win.\n\n"
                + "4. If the board is full and there is no winner, "
                + "the game ends in a draw.");

        alert.showAndWait();
    }

    @FXML
    private void quitGame() {
        Platform.exit();
        System.exit(0);
    }
}