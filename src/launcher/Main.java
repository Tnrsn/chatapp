package launcher;
	

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	public static void main(String[] args) {
		try {
		Runtime.getRuntime().exec("path");
		} catch (Exception e){}
		launch(args);
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
	
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

		Scene scene = new Scene(root);
		String css = this.getClass().getResource("application.css").toExternalForm();
		scene.getStylesheets().add(css);
//		, Color.rgb(32, 30, 31, 1.0)
//		stage.setFullScreen(true);
		
		stage.setScene(scene);
		stage.show();
	}
	
}
