package main;
	

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
	
		ServerManagement db = new ServerManagement();
		db.ConnectServer();
		
		//Do not add SceneSwitcher class here
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

		Scene scene = new Scene(root);
		String css = this.getClass().getResource("MainScene.css").toExternalForm();
		scene.getStylesheets().add(css);

		MainController.setPrimaryStage(stage);
		stage.setScene(scene);
//		Removes OS's control bar. Can be used to customize the app even further
//		stage.initStyle(StageStyle.UNDECORATED);
		
		stage.setResizable(false);
		stage.show();
	}
	
}
