package main.login;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.app.SceneSwitcher;
import main.app.ServerManagement;
import main.app.WindowController;

public class RegisterController {
	@FXML
	private Label errorText;
	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private TextField repasswordTextField; //Re enter password field
	@FXML
	private TextField emailTextField;
	@FXML
	private BorderPane toolBar;
	
	public void initialize()
	{
		//Sets the window draggable
		Platform.runLater(() -> {
			Stage stage = (Stage)toolBar.getScene().getWindow();
			WindowController.setDraggable(toolBar, stage);
//			System.out.println("AAAAAAAAAA");
		});
	}
	
	public void SwitchToLoginScene(ActionEvent event) throws IOException
	{
		SceneSwitcher sceneSwitch = new SceneSwitcher();
		sceneSwitch.SwitchToLoginScene(event);
	}
	
	
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
			// errortext bura yazcan 
		{
			System.out.println("Passwords are not matching");
		}
	}
	@FXML
	public void closeWindow(ActionEvent event) {
	    System.exit(0);
	}
}
