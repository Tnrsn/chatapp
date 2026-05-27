package main.mainscene.peopleblock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class PeopleBlockController {
	
	@FXML
	private Text nameLabel;
	@FXML
	private Button addFriendButton;
	@FXML
	private Text reqSentText;
	
	public void setName(String name)
	{
		nameLabel.setText(name);
	}
	
	public void AddFriend(ActionEvent event)
	{
		addFriendButton.setVisible(false);
		reqSentText.setVisible(true);
	}
}
