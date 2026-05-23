package main;
	

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		
//		Make sure you run the project with java21
//		System.out.println(System.getProperty("java.home"));
//		System.out.println(System.getProperty("java.version"));
//		System.out.println(System.getProperty("java.runtime.version"));
		
		//Do not use SceneSwitcher class here for now.
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

		Scene scene = new Scene(root);
		String css = this.getClass().getResource("Login.css").toExternalForm();
		scene.getStylesheets().add(css);
		
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		
	}
	
}
