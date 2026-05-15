package launcher;

import java.io.IOException;

import javafx.event.ActionEvent;

public class RegisterController {


	public void SwitchToLoginScene(ActionEvent event) throws IOException
	{
		SceneSwitcher sceneSwitch = new SceneSwitcher();
		sceneSwitch.SwitchScene("LauncherApp.FXML", "application.css", event);
		
	}
	
	public void Register(ActionEvent event)
	{
		
	}
}
