package main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.DBManagement;

public class RegisterController {


	public void SwitchToLoginScene(ActionEvent event) throws IOException
	{
		SceneSwitcher sceneSwitch = new SceneSwitcher();
		sceneSwitch.SwitchScene("Login.FXML", "Login.css", event);
		
	}
	
	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private TextField emailTextField;
	
	public void ConfirmRegister(ActionEvent event) throws IOException, InterruptedException
	{
		System.out.println("Register Has Started");
		
		DBManagement db = new DBManagement();
		db.SignUp(usernameTextField, passwordTextField, emailTextField);
	}
}
