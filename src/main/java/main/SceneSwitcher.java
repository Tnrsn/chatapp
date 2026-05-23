package main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SceneSwitcher {

	private Stage stage;
	private Scene scene;
	
	//Please do not use this function on other classes, instead add a new function in this class to make setting properties easier
	private Stage SwitchScene(String FXML, String CSS, ActionEvent event) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource(FXML));

//		stage.initStyle(StageStyle.UNDECORATED);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		scene = new Scene(root);
		
		String css = this.getClass().getResource(CSS).toExternalForm();
		scene.getStylesheets().add(css);
		
		stage.setScene(scene);
		stage.centerOnScreen();
		
		return stage;
	}
	
	public void SwitchToMainScene(ActionEvent event) throws IOException
	{
		Stage stage = SwitchScene("MainScene.fxml", "MainScene.css", event);
		
		//---Set properties---
		stage.setResizable(true);
		
		stage.show();
	}
	
	public void SwitchToRegisterScene(ActionEvent event) throws IOException
	{
		Stage stage = SwitchScene("RegisterScene.fxml", "Login.css", event);
		
		//---Set properties---
//		stage.setResizable(true);
//		WindowController.setResizable(scene, stage);
		WindowController.setDraggable(scene, stage);
		stage.show();
	}
	
	public void SwitchToLoginScene(ActionEvent event) throws IOException
	{
		Stage stage = SwitchScene("Login.fxml", "Login.css", event);
		
		//---Set Properties---
//		stage.setResizable(true);
		WindowController.setDraggable(scene, stage);
		
		stage.show();
	}
}
