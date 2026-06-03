package main.mainscene.communityblock;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.mainscene.MainSceneController;
import main.mainscene.community.Community;
import main.mainscene.community.CommunityAPIClient;
import main.mainscene.community.CommunitySearchResults;
import main.mainscene.community.tags.CommunityTagsController;

public class CommunityBlockController {

	private MainSceneController mainController;
	private Community community;
	private List<String> tags;
	
	@FXML
	private Label communityName;
	@FXML
	private Label communityDescription;
	@FXML
	private HBox tagList;

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
	
	public void setCommunityDescriptionLabel(String communityDescription)
	{
		this.communityDescription.setText(communityDescription);
	}
	
	@FXML
	public void joinCommunity(ActionEvent event) throws IOException, InterruptedException
	{
		CommunityAPIClient.joinCommunity(community.getId());
		
		System.out.println("You have joined to the community!");
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public void setCommunityTagButtons(List<String> tags) throws IOException
	{
		for(String s : tags)
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/communityblock/SearchScreenTags.fxml"));
			
			Parent block = loader.load();
			block.getStylesheets().add(getClass().getResource("/main/mainscene/communityblock/CommunityBlock.fxml").toExternalForm());
			SearchScreenTagsController tag = loader.getController(); //80
			tag.setTag(s);
			
			tagList.getChildren().add(block);
			
		}
	}
}
