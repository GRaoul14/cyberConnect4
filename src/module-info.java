module CyberConnect4 {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.media;
	
	opens application to javafx.graphics, javafx.fxml;
	opens controller to javafx.fxml;
}
