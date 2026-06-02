package main.mainscene.sidebar;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import main.mainscene.MainSceneController;
import main.mainscene.community.Community;

public class SBCommunityBlocksController {
	
	private MainSceneController mainSceneController;
	
	private Community community;
	@FXML
	private Button communityButton;
	@FXML
	private Text communityName;
	
	public MainSceneController getMainSceneController()
	{
		return mainSceneController;
	}
	public void setMainSceneController(MainSceneController mainSceneController) 
	{
		this.mainSceneController = mainSceneController;
	}
	
	public Community getCommunity() 
	{
		return community;
	}
	public void setCommunity(Community community) 
	{
		this.community = community;
	}
	
	public void setCommunityName(String communityName)
	{
		this.communityName.setText(communityName);
	}
	
	@FXML
	public void openChat() throws IOException, InterruptedException
	{
		mainSceneController.openCommunityChat(community);
	}	
}
