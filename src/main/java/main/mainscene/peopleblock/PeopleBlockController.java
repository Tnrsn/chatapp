package main.mainscene.peopleblock;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.app.ServerManagement;
import main.mainscene.MainSceneController;

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
	
	//request
	@FXML
	private BorderPane rootFR;
	
	public void setName(String name)
	{
		nameLabel.setText(name);
	}
	
	public void setUserId(UUID userId)
	{
		this.searchUserId = userId;
	}
	
	// Request
	public void AddFriend(ActionEvent event)
	{
	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/friends/send?token=" + ServerManagement.getToken() + "&receiverId=" + searchUserId))
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
	
	public void AcceptFriendReq(ActionEvent event)
	{
	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/friends/accept?token=" + ServerManagement.getToken() + "&requesterId=" + searchUserId))
	            .POST(HttpRequest.BodyPublishers.noBody())
	            .build();

	    HttpResponse<String> response;
	    try { //Refresh friend list here later
	    	response = client.send(request, HttpResponse.BodyHandlers.ofString()); 
	    	
	    	
	    	((VBox) rootFR.getParent()).getChildren().remove(rootFR);
			System.out.println("RequestAccepted");
	    }catch (Exception e) {
			System.out.println("No connection to the server...");
			return;
		}
	}
	
	public void DeclineFriendReq(ActionEvent event) //This function also works for removing existing friends too
	{
	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/friends/decline?token=" + ServerManagement.getToken() + "&requesterId=" + searchUserId))
	            .POST(HttpRequest.BodyPublishers.noBody())
	            .build();

	    HttpResponse<String> response;
	    try {
	    	response = client.send(request, HttpResponse.BodyHandlers.ofString()); 
	    	
	    	((VBox) rootFR.getParent()).getChildren().remove(rootFR);
			System.out.println("Request Declined");
	    }catch (Exception e) {
			System.out.println("No connection to the server...");
			return;
		}
	}
	
	//Friends
	public void sendMessage(ActionEvent event)
	{
		MainSceneController.sendMessage(searchUserId);
	}
	
}
