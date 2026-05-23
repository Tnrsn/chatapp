package main;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainSceneController {
	
	@FXML
	private HBox toolBar;
	@FXML
	private HBox sideDragBar;
	public void initialize()
	{
	    Platform.runLater(() -> {
	        Stage stage = (Stage)toolBar.getScene().getWindow();

	        WindowController.setDraggable(toolBar, stage);
	        WindowController.setDraggable(sideDragBar, stage);
	    });
	}
	
	public void MinimizeWindow(ActionEvent event)
	{
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setIconified(true);
	}
	
	public void FullscreenWindow(ActionEvent event)
	{
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setMaximized(!stage.isMaximized());
	}
	
	public void CloseApp(ActionEvent event)
	{
		Platform.exit();
	}
	
	public void SendMessage(ActionEvent event) throws IOException, InterruptedException
	{
		if(ServerManagement.isTokenValid())
		{
			System.out.println("Valid");
		}
		else
		{
			System.out.println("Not Valid");
		}
	}
}
