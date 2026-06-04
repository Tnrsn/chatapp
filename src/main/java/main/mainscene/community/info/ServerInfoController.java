package main.mainscene.community.info;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.mainscene.MainSceneController;
import main.mainscene.community.CommunityAPIClient;
import main.mainscene.communityblock.SearchScreenTagsController;
import main.mainscene.user.User;

public class ServerInfoController {

	private MainSceneController mainSceneController;
	private UUID conversationId;
    @FXML
    private VBox serverMembersList;
    @FXML
    private Label serverName;
    @FXML
    private TextArea serverDescription;
    @FXML
    private HBox serverTags;
    
    public void setInfos(CommunityInfo info) throws IOException
    {	
    	serverName.setText(info.getInfo().getCommunity().getName());
    	serverDescription.setText(info.getInfo().getCommunity().getDescription());
    	addTags(info.getInfo().getTags());
    	addMembers(info.getMembers());
    	conversationId = info.getInfo().getCommunity().getConversationId();
    }
    
    private void addTags(List<String> tags) throws IOException
    {
		for(String s : tags)
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/communityblock/SearchScreenTags.fxml"));
			
			Parent block = loader.load();
			block.getStylesheets().add(getClass().getResource("/main/mainscene/communityblock/CommunityBlock.fxml").toExternalForm());
			SearchScreenTagsController tag = loader.getController();
			tag.setTag(s);
			
			serverTags.getChildren().add(block);
		}
    }
    
    private void addMembers(List<User> members) throws IOException
    {
		for(User user : members)
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/community/info/Member.fxml"));

			Parent block = loader.load();
			block.getStylesheets().add(getClass().getResource("/main/mainscene/community/info/Member.css").toExternalForm());
			MemberController controller = loader.getController();
			controller.getMemberButton().setText(user.username);
			controller.setUser(user);
			
			serverMembersList.getChildren().add(block);
		}
    }
  
    @FXML
    public void QuitServer() throws JsonProcessingException
    {
    	if(CommunityAPIClient.quitCommunity(conversationId))
    	{
    		mainSceneController.CloseChat();
    	}
    }

	public void setMainSceneController(MainSceneController mainSceneController) 
	{
		this.mainSceneController = mainSceneController;
	}
}