package main.mainscene.message;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MessageController {
	
	@FXML
	private Text messageText;
	
	public void setMessage(Message message) //This function probably will change once I add other content types
	{
		messageText.setText(message.getContent());
	}
}
