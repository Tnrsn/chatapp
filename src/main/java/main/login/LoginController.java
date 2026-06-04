package main.login;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.app.SceneSwitcher;
import main.app.ServerManagement;
import main.app.WindowController;


public class LoginController {
	
	@FXML 
	private BorderPane toolBar;
	@FXML
	private Label errorText;
	@FXML
	private TextField emailTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
    public void initialize() {
		
		//Sets the window draggable
		Platform.runLater(() -> {
			Stage stage = (Stage)toolBar.getScene().getWindow();
			WindowController.setDraggable(toolBar, stage);
		});
		
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
	
	public void Register(ActionEvent event) throws IOException 
	{
		System.out.println("Register");
		
		SceneSwitcher switchScene = new SceneSwitcher();
		switchScene.SwitchToRegisterScene(event);
	}
	

	public void Login(ActionEvent event) throws IOException, InterruptedException, SQLException
    {
        ServerManagement server = new ServerManagement();

        if(server.SignIn(emailTextField, passwordTextField))
        {
            SceneSwitcher sceneSwitcher = new SceneSwitcher();
            sceneSwitcher.SwitchToMainScene(event);
            System.out.println("Login Successful");
        }
        else
        {
            System.out.println("Login Unsuccessful");
            errorText.setVisible(true);
            
            if (ServerManagement.isConnectionError()) {
                errorText.setText("No internet connection or server is offline.");
            } else {
                errorText.setText("E-mail or password is incorrect.");
            }
        }
    }
	@FXML
	public void closeWindow(ActionEvent event) {
	    System.exit(0);
	}
}
