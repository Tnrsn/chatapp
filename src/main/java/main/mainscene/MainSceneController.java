package main.mainscene;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.app.ServerManagement;
import main.app.WindowController;
import main.mainscene.peopleblock.PeopleBlockController;
import main.mainscene.search.SearchManager;

public class MainSceneController {
	
	@FXML
	private AnchorPane root;
	
	@FXML
	private HBox toolBar;
	@FXML
	private HBox sideDragBar;
	@FXML
	private Label usernameLabel;
	
	@FXML
	private VBox searchResults;
	@FXML
	private TextField searchField;
	
	public void initialize()
	{
		//Sets the window draggable
	    Platform.runLater(() -> {
	        Stage stage = (Stage)toolBar.getScene().getWindow();

	        WindowController.setDraggable(toolBar, stage);
	        WindowController.setDraggable(sideDragBar, stage);
	    });
	    
	    //Sets username label on main scene load
	    try {
			usernameLabel.setText(ServerManagement.getUsername());
		} catch (IOException e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		} 
	    
	    //Unfocuses the search bar if clicked on anywhere else
	    root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
	        if (event.getTarget() != searchField) 
	        {
	            root.requestFocus();
	        }
	    });
	    
	    
	    // Loading a FXML to an another one...
	    try
	    {
	        for(int i = 0; i < 10; i++)
	        {
	            FXMLLoader loader = new FXMLLoader(
	                getClass().getResource("/main/mainscene/peopleblock/PeopleBlock.fxml")
	            );
	            
	            Parent block = loader.load();
	            PeopleBlockController controller = loader.getController();
	            controller.setName("test " + i);
	            
	            searchResults.getChildren().add(block);
	        }
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
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
	
	@FXML
	private void handleSearch(ActionEvent event) throws IOException, InterruptedException {
	    System.out.println("Searching stuff");
	    
	    
	    SearchManager.searchPeople(searchField.getText());
	}
	
}
