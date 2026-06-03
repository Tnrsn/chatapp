package main.mainscene.community.tags;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import main.mainscene.community.CreateCommunityController;

public class CommunityTagsController {

	@FXML
	private Text tagName;
	
	private String tag;
	
	private CreateCommunityController parentController;
	private Parent root;
	
	public Text getTagName()
	{
		return tagName;
	}
	
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public void setRoot(Parent parent)
	{
		root = parent;
	}
	
	public void setParentController(CreateCommunityController parentController)
	{
		this.parentController = parentController;
	}
	
	@FXML
	public void RemoveTag(ActionEvent event)
	{
		parentController.getTags().remove(tag);
		parentController.getTagContainer().getChildren().remove(root);
	}
}
