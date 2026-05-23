package main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RegisterController {


	public void SwitchToLoginScene(ActionEvent event) throws IOException
	{
		SceneSwitcher sceneSwitch = new SceneSwitcher();
		sceneSwitch.SwitchToLoginScene(event);
	}
	
	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private TextField repasswordTextField; //Re enter password field
	@FXML
	private TextField emailTextField;
	
	public void ConfirmRegister(ActionEvent event) throws IOException, InterruptedException
	{
		System.out.println("Register Has Started");
		
		if(passwordTextField.getText().equals(repasswordTextField.getText()))
		{
			ServerManagement server = new ServerManagement();
			SceneSwitcher ss = new SceneSwitcher();
			
			server.SignUp(usernameTextField, passwordTextField, emailTextField);
			ss.SwitchToMainScene(event);
		}
		else
		{
			System.out.println("Passwords are not matching");
		}
	}
}
