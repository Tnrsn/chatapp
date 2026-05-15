package launcher;
	

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class LauncherApp extends Application {
	public static void main(String[] args) {
//		try {
//		Runtime.getRuntime().exec("/mainapp/Main.java");
//		} catch (Exception e){}
		launch(args);
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
	
		DBManagement db = new DBManagement();
		db.ConnectServer();
		
		//Do not add SceneSwitcher class here
		Parent root = FXMLLoader.load(getClass().getResource("LauncherApp.fxml"));

		Scene scene = new Scene(root);
		String css = this.getClass().getResource("application.css").toExternalForm();
		scene.getStylesheets().add(css);

		stage.setScene(scene);
//		Removes OS's control bar. Can be used to customize the app even further
//		stage.initStyle(StageStyle.UNDECORATED);
		
		stage.setResizable(false);
		stage.show();
	}
	
}
