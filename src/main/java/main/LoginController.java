package main;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.app.SceneSwitcher;
import main.app.ServerManagement;


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
	@FXML
    public void initialize() {
        emailTextField.setOnAction(event -> {
            try {
                Login(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        passwordTextField.setOnAction(event -> {
            try {
                Login(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
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
			System.out.println("Login Unsuccessful");
			errorText.setVisible(true);
			errorText.setText("E-mail or password is incorrect.");
		}
	}
	@FXML
	public void closeWindow(ActionEvent event) {
	    System.exit(0);
	}
}
