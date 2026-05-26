package main.app;

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
	
	private Stage SwitchScene(String FXML, String CSS, ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML));
		Parent root = loader.load();

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
		Stage stage = SwitchScene("/main/mainscene/MainScene.fxml", "/main/mainscene/MainScene.css", event);
		
		//---Set properties---
		WindowController.setResizable(scene, stage, 600, 800);
		
		stage.show();
	}
	
	public void SwitchToRegisterScene(ActionEvent event) throws IOException
	{
		Stage stage = SwitchScene("/main/login/RegisterScene.fxml", "/main/login/Login.css", event);
		
		//---Set properties---
		// Add an initialize function inside the LoginController and call setDraggable from there, just like in WindowController.
//		WindowController.setDraggable(scene, stage);
		stage.show();
	}
	
	public void SwitchToLoginScene(ActionEvent event) throws IOException
	{
		Stage stage = SwitchScene("/main/login/Login.fxml", "/main/login/Login.css", event);
		
		//---Set Properties---
		
		stage.show();
	}
}
