package main;

import java.io.IOException;
import java.sql.SQLException;

import app.SceneSwitcher;
import app.ServerManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
	
	public void Register(ActionEvent event) throws IOException 
	{
		System.out.println("Register");
		
		SceneSwitcher switchScene = new SceneSwitcher();
		switchScene.SwitchToRegisterScene(event);
	}
	
	@FXML
	private Label errorText;
	@FXML
	private TextField emailTextField;
	@FXML
	private TextField passwordTextField;
	public void Login(ActionEvent event) throws IOException, InterruptedException, SQLException 
	{
		
		ServerManagement server = new ServerManagement();

		if(server.SignIn(emailTextField, passwordTextField))
		{
			//Login Successful
			SceneSwitcher sceneSwitcher = new SceneSwitcher();
			sceneSwitcher.SwitchToMainScene(event);
			System.out.println("Login Successful");

		}
		else 
		{
//			Login Unsuccessful
			System.out.println("Login Unsuccessful");
		}
	}
}
