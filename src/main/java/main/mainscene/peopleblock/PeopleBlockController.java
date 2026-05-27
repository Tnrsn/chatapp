package main.mainscene.peopleblock;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import main.app.ServerManagement;

public class PeopleBlockController {
	
	String adress = ServerManagement.getAdress();
	
	@FXML
	private UUID searchUserId;
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
	
	public void setUserId(UUID userId)
	{
		this.searchUserId = userId;
	}
	
	public void AddFriend(ActionEvent event)
	{
	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/friends/send?userToken=" + ServerManagement.getToken() + "&receiverId=" + searchUserId))
	            .POST(HttpRequest.BodyPublishers.noBody())
	            .build();

	    HttpResponse<String> response;
	    try {
	    	response = client.send(request, HttpResponse.BodyHandlers.ofString());
	    	
			addFriendButton.setVisible(false);
			reqSentText.setVisible(true);
			System.out.println("Request sent");
	    }catch (Exception e) {
			System.out.println("No connection to the server...");
			return;
		}
	}
}
