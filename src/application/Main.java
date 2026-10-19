package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
			Scene scene = new Scene(root, 800, 650);
			scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Cyber Connect 4");
			primaryStage.show();
			primaryStage.setWidth(1000);
			primaryStage.setHeight(750);
			primaryStage.setResizable(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
