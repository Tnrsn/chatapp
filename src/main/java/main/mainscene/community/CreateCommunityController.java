package main.mainscene.community;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.mainscene.community.tags.CommunityTagsController;

public class CreateCommunityController {

	@FXML
	private ScrollPane tagsScroll;
	@FXML
	private VBox tagContainer;
	@FXML
	private TextField serverTagsField;
	
	@FXML
	private TextField serverName;
	@FXML
	private CheckBox isPublic;
	@FXML
	private TextArea serverDescription;
	
	private final List<String> tags = new ArrayList<>();
	
	@FXML
	public void initialize() {
	    serverTagsField.setOnAction(event -> {
	        try {
	            AddTag(null);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    });
	}
	
	@FXML
	public void AddTag(ActionEvent event) throws IOException
	{
	    String input = serverTagsField.getText();
	    if (input == null || input.isBlank()) return;

	    String[] splitTags = input.trim().split(",");

	    for (String rawTag : splitTags) 
	    {
	        if (tags.size() >= 3) //3 tag limit
	        {
	            break;
	        }
	        
	        String tag = rawTag.trim().toLowerCase();

	        if (tag.isEmpty()) continue;
	        if (tags.contains(tag)) continue;

	        tags.add(tag);

	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/community/tags/CommunityTags.fxml"));
	        
	        Parent block = loader.load();
	        block.getStylesheets().add(getClass().getResource("/main/mainscene/community/tags/CommunityTags.css").toExternalForm());

	        CommunityTagsController controller = loader.getController();
	        controller.getTagName().setText(tag);
	        controller.setRoot(block);
	        controller.setParentController(this);
	        controller.setTag(tag);

	        tagContainer.getChildren().add(block);
	    }

	    serverTagsField.clear();
	}
	
	@FXML
	public void CreateServer(ActionEvent event)
	{
	    CommunityAPIClient.createCommunity(serverName.getText(), serverDescription.getText(), isPublic.isSelected(), 
	    		new ArrayList<>(tags));
	}
	
	public VBox getTagContainer()
	{
		return tagContainer;
	}
	
	public List<String> getTags()
	{
		return tags;
	}
}
