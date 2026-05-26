package main.mainscene.peopleblock;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PeopleBlockController {
	
	@FXML
	private Text nameLabel;
	
	public void setName(String name)
	{
		nameLabel.setText(name);
	}
}
