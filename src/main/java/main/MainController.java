package main;

import javafx.stage.Stage;

public class MainController {

	private static Stage primaryStage;
	
	public static void setPrimaryStage(Stage stage) {
		primaryStage = stage;
	}
	
	public static Stage getPrimaryStage() {
		return primaryStage;
	}
}
