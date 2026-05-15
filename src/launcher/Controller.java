package launcher;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
	
	public void Register(ActionEvent event) throws IOException {
		System.out.println("Register");
		
		SceneSwitcher switchScene = new SceneSwitcher();
		switchScene.SwitchScene("RegisterScene.fxml", "application.css", event);
	}
	
	public void Login(ActionEvent e) throws IOException {
		//It should open "mainapp" here first before doing anything else in this function
		File jar = new File("src/mainapp.jar");
		System.out.println(jar.getAbsolutePath());
//		new ProcessBuilder("java", "-jar", jar.getAbsolutePath()).start();
	}
}
