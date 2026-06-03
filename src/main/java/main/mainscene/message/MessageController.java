package main.mainscene.message;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class MessageController {
	
	@FXML
	private Label messageText;
	@FXML
	private Text usernameText;
	
	public void setMessage(Message message) //This function probably will change once I add other content types
	{
		messageText.setText(message.getContent());
		
		if(message.getSenderId() != null)
		{
			usernameText.setText(MessageManager.getUsernameById(message.getSenderId()) + ": ");
		}
	}
	
	public void setUsernameText(String username)
	{
		usernameText.setText(username + ": ");
	}
}
