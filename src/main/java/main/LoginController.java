package main;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	public void Register(ActionEvent event) throws IOException {
		System.out.println("Register");
		
		SceneSwitcher switchScene = new SceneSwitcher();
		switchScene.SwitchScene("RegisterScene.fxml", "Login.css", event);
	}
	
	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField passwordTextField;
	public void Login(ActionEvent event) throws IOException, InterruptedException {
		
		DBManagement db = new DBManagement();
		
		if(db.SignIn(usernameTextField, passwordTextField))
		{
			//Mainapp should open inside of here
			SceneSwitcher sceneSwitcher = new SceneSwitcher();
			sceneSwitcher.SwitchScene("MainScene.FXML", "MainScene.css", event);
		}
		
//		File jar = new File("src/mainapp.jar");
//		System.out.println(jar.getAbsolutePath());
//		new ProcessBuilder("java", "-jar", jar.getAbsolutePath()).start();
	}
}
