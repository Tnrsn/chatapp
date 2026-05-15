package launcher;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {

	private Stage stage;
	private Scene scene;
	
	public void SwitchScene(String FXML, String CSS, ActionEvent event) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource(FXML));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		
		String css = this.getClass().getResource(CSS).toExternalForm();
		scene.getStylesheets().add(css);
		
		stage.setScene(scene);
		stage.show();
	}
}
