package main.mainscene.communityblock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.mainscene.MainSceneController;
import main.mainscene.community.Community;

public class CommunityBlockController {

	private MainSceneController mainController;
	private Community community;
	
	@FXML
	private Label communityName;

	public MainSceneController getMainController() 
	{
		return mainController;
	}

	public void setMainController(MainSceneController mainSceneController) 
	{
		this.mainController = mainSceneController;
	}
	
	public void setCommunityNameLabel(String communityName)
	{
		this.communityName.setText(communityName);
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}
	
	@FXML
	public void joinCommunity(ActionEvent event)
	{
		System.out.println("You have joined to the community!");
	}
	
}
