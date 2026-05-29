package main.mainscene;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.app.ServerManagement;
import main.app.WindowController;
import main.mainscene.message.Conversation;
import main.mainscene.message.Message;
import main.mainscene.message.MessageController;
import main.mainscene.message.MessageLL;
import main.mainscene.message.MessageManager;
import main.mainscene.message.MessageNode;
import main.mainscene.peopleblock.PeopleBlockController;
import main.mainscene.peopleblock.SearchMode;
import main.mainscene.search.SearchManager;
import main.mainscene.user.User;

public class MainSceneController {
	
	@FXML
	private AnchorPane root;
	
	@FXML
	private HBox toolBar;
	@FXML
	private HBox sideDragBar;
	@FXML
	private Label usernameLabel;
	
	@FXML
	private ScrollPane messageScroll;
	@FXML
	private VBox searchMenu;
	@FXML
	private VBox searchResults;
	@FXML
	private TextField searchField;
	
	public void initialize()
	{
		//Sets the window draggable
	    Platform.runLater(() -> {
	        Stage stage = (Stage)toolBar.getScene().getWindow();

	        WindowController.setDraggable(toolBar, stage);
	        WindowController.setDraggable(sideDragBar, stage);
	    });
	    
	    //Sets username label on main scene load
	    usernameLabel.setText(ServerManagement.getUsername());
//	    try {
//			usernameLabel.setText(ServerManagement.getUsernameFromServer());
//		} catch (IOException e) {
//			System.out.println("Something went wrong");
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			System.out.println("Something went wrong");
//			e.printStackTrace();
//		} 
	    
	    //Unfocuses the search bar if clicked on anywhere else
	    root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
	        if (event.getTarget() != searchField) 
	        {
	            root.requestFocus();
	        }
	    });
	}
	
	public void MinimizeWindow(ActionEvent event)
	{
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setIconified(true);
	}
	
	public void FullscreenWindow(ActionEvent event)
	{
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setMaximized(!stage.isMaximized());
	}
	
	public void CloseApp(ActionEvent event)
	{
		Platform.exit();
	}
	
// -------------------------SEARCH MENU---------------------------------------
	private SearchMode currentSearchMode = SearchMode.PEOPLE;
	
	private void loadBlocks(String FXMLName, List<User> users) throws IOException
	{
	    for(User user : users)
	    {
            FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/main/mainscene/peopleblock/" + FXMLName)
	        );
	         
	        Parent block = loader.load();
	        block.getStylesheets().add(
	        	    getClass()
	        	    .getResource("/main/mainscene/peopleblock/PeopleBlock.css")
	        	    .toExternalForm()
	        	);
	        PeopleBlockController controller = loader.getController();
	        
	        controller.setMainController(this);
	        controller.setName(user.username);
	        controller.setUserId(user.id);
	        
	        searchResults.getChildren().add(block);
	    }
	}
	
	@FXML
	private void handleSearch(ActionEvent event) throws IOException, InterruptedException {
//	    System.out.println("Searching...");
	    searchResults.getChildren().clear();
	    
	    messageScroll.setVisible(false);
	    searchMenu.setVisible(true);
	    
	    if(currentSearchMode == SearchMode.COMMUNITIES)
	    {
	    	
	    }
	    else if(currentSearchMode == SearchMode.PEOPLE)
	    {
		    List<User> users = SearchManager.searchPeople(searchField.getText());
		    loadBlocks("PeopleBlock.fxml", users);
	    }
	    else if(currentSearchMode == SearchMode.REQUEST)
	    {
		    List<User> users = SearchManager.searchRequests();
		    loadBlocks("FriendRequest.fxml", users);
	    }
	    else if(currentSearchMode == SearchMode.FRIENDS) 
	    {
		    List<User> users = SearchManager.getFriends();
		    loadBlocks("FriendBlock.fxml", users);
	    }
	}
	
	@FXML
	public void searchCommunities(ActionEvent event) throws IOException, InterruptedException
	{
		currentSearchMode = SearchMode.COMMUNITIES;
		handleSearch(event);
	}
	@FXML
	public void searchPeople(ActionEvent event) throws IOException, InterruptedException
	{
		currentSearchMode = SearchMode.PEOPLE;
		handleSearch(event);
	}
	@FXML
	public void searchRequests(ActionEvent event) throws IOException, InterruptedException
	{
		currentSearchMode = SearchMode.REQUEST;
		handleSearch(event);
	}
	@FXML
	public void searchFriends(ActionEvent event) throws IOException, InterruptedException
	{
		currentSearchMode = SearchMode.FRIENDS;
		handleSearch(event);
	}
	
	@FXML
	public void searchBackButton(ActionEvent event)
	{
	    messageScroll.setVisible(true);
	    searchMenu.setVisible(false);
	}
	
//-----------------SIDEBAR----------------------
    @FXML
    private void sidebarButtons(ActionEvent event)
    {
        try
        {
            Desktop.getDesktop().browse(
                new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    
//--------------------MESSAGES-------------
    @FXML
    private VBox messageVBox;
    @FXML
    private TextField messageField;
    private Conversation conversation;
    @FXML
    private Label channelNameText;
    
//    public void addMessage
    
    public void openChat(UUID receiverId) throws IOException, InterruptedException
    {
    	messageScroll.setVisible(true);
    	searchMenu.setVisible(false);
    	channelNameText.setText(MessageManager.getUsernameById(receiverId));
    	
    	conversation = MessageManager.getConversation(receiverId, ServerManagement.getToken());
    	loadChat(conversation.getId(), ServerManagement.getToken());
    }
    
	public void SendMessage(ActionEvent event) throws IOException, InterruptedException
	{
		Message message = MessageManager.sendMessage(conversation.getId(), messageField.getText());
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/message/MessageBlock.fxml"));
		
		Parent node = loader.load();
		node.getStylesheets().add(
        	    getClass()
        	    .getResource("/main/mainscene/message/Message.css")
        	    .toExternalForm()
        	);
		
		MessageController controller = loader.getController();
		controller.setMessage(message);
		controller.setUsernameText(ServerManagement.getUsername());
		
		messageVBox.getChildren().add(node);
	}
    

	
    public void loadChat(UUID conversationId, String token) throws IOException, InterruptedException
    {
    	List<Message> messages = MessageManager.getMessages(conversationId, token);
    	MessageManager.messageLL = new MessageLL();
    	
    	for(Message m : messages)
    	{
    		MessageManager.messageLL.add(m);
    	}
    	
    	MessageNode current = MessageManager.messageLL.getHead();
    	messageVBox.getChildren().clear();
    	
    	while(current != null)
    	{
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/message/MessageBlock.fxml"));
    		
    		Parent node = loader.load();
    		node.getStylesheets().add(
	        	    getClass()
	        	    .getResource("/main/mainscene/message/Message.css")
	        	    .toExternalForm()
	        	);
    		
    		MessageController controller = loader.getController();
    		controller.setMessage(current.getMessage());
    		
    		messageVBox.getChildren().add(node);
    		current = current.getNext();
    	}
    }
}
