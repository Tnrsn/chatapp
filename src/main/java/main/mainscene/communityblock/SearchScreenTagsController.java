package main.mainscene.communityblock;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SearchScreenTagsController {

	@FXML
	private Button tag;
	
	public void setTag(String tag)
	{
		this.tag.setText(tag);
	}
}
