package main;
	

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
	
		DBManagement db = new DBManagement();
		db.ConnectServer();
		
		//Do not add SceneSwitcher class here
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

		Scene scene = new Scene(root);
		String css = this.getClass().getResource("MainScene.css").toExternalForm();
		scene.getStylesheets().add(css);

		stage.setScene(scene);
//		Removes OS's control bar. Can be used to customize the app even further
//		stage.initStyle(StageStyle.UNDECORATED);
		
		stage.setResizable(false);
		stage.show();
	}
	
}
