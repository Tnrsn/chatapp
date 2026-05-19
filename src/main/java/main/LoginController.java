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
	private TextField emailTextField;
	@FXML
	private TextField passwordTextField;
	public void Login(ActionEvent event) throws IOException, InterruptedException {
		
		DBManagement db = new DBManagement();
		
		if(db.SignIn(emailTextField, passwordTextField))
		{
			SceneSwitcher sceneSwitcher = new SceneSwitcher();
			sceneSwitcher.SwitchScene("MainScene.fxml", "MainScene.css", event);
			System.out.println("Login Successful");
		}
		else
		{
			System.out.println("Login Unsuccessful");
		}
		
//		File jar = new File("src/mainapp.jar");
//		System.out.println(jar.getAbsolutePath());
//		new ProcessBuilder("java", "-jar", jar.getAbsolutePath()).start();
	}
}
