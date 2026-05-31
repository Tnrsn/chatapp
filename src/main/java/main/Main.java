package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	public static void main(String[] args) { 
		launch(args);
	}
	//Some functions/logic in this client would normally be implemented on the server side.
	//Because of project time constraints, it was not possible for me to move everything to the server side iun time.
	//But everything works at the moment
	
	@Override
	public void start(Stage stage) throws Exception {
//		Make sure you run the project with java21
//		System.out.println(System.getProperty("java.home"));
//		System.out.println(System.getProperty("java.version"));
//		System.out.println(System.getProperty("java.runtime.version"));
		
		Parent root = FXMLLoader.load(getClass().getResource("/main/login/Login.fxml"));

		Scene scene = new Scene(root);
		String css = this.getClass().getResource("/main/login/Login.css").toExternalForm();
		scene.getStylesheets().add(css);
		
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
	}
	
}
