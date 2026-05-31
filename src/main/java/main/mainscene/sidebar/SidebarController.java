package main.mainscene.sidebar;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import main.app.ServerManagement;
import main.mainscene.MainSceneController;
import main.mainscene.message.MessageManager;
import main.mainscene.user.User;

public class SidebarController {

	private MainSceneController mainSceneController;
	@FXML 
	private Button friendButton;
	@FXML
	private Text usernameText;
	
	private User user;
	
	public void setUser(User user)
	{
		this.user = user;
	}
	public User getUser()
	{
		return user;
	}
	
	public void setMainSceneController(MainSceneController controller)
	{
		this.mainSceneController = controller;
	}
	
	public void setUsername(String username)
	{
		this.usernameText.setText(username);
	}
	
	@FXML
	public void openChat() throws IOException, InterruptedException
	{
		mainSceneController.openChat(user.id);
	}
	
}
