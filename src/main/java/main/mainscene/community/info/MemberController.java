package main.mainscene.community.info;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.mainscene.user.User;

public class MemberController {

	@FXML 
	private Button memberButton;
	
	private User user;
	
	public Button getMemberButton()
	{
		return memberButton;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
