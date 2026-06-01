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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.app.ServerManagement;
import main.app.WebSocketClientManager;
import main.app.WindowController;
import main.mainscene.message.Conversation;
import main.mainscene.message.Message;
import main.mainscene.message.MessageController;
import main.mainscene.message.MessageLL;
import main.mainscene.message.MessageManager;
import main.mainscene.message.MessageNode;
import main.mainscene.message.MessageRequest;
import main.mainscene.peopleblock.PeopleBlockController;
import main.mainscene.peopleblock.SearchMode;
import main.mainscene.search.SearchManager;
import main.mainscene.sidebar.SidebarController;
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
	    
	    MessageManager.setMainController(this);
	    WebSocketClientManager.connect();

	    //Sets username label on main scene load
	    usernameLabel.setText(ServerManagement.getUsername());
	    
	    //Unfocuses the search bar if clicked on anywhere else
	    root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
	        if (event.getTarget() != searchField) 
	        {
	            root.requestFocus();
	        }
	    });
	    
	    try {
			loadSidebarBlocks(SearchManager.getFriends());
		} catch (IOException e) {
			System.out.println("Problem occured while loading friends");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Problem occured while loading friends");
			e.printStackTrace();
		}
	    
	    messageField.setOnKeyPressed(event -> {
	        if (event.getCode() == KeyCode.ENTER) {
	            try {
	                SendMessage(null);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
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
	
	@FXML
	private Button createCommunityButton;
	
	private void loadSearchBlocks(String FXMLName, List<User> users) throws IOException
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
	    	createCommunityButton.setVisible(true);
	    }
	    else if(currentSearchMode == SearchMode.PEOPLE)
	    {
	    	createCommunityButton.setVisible(false);
	    	
		    List<User> users = SearchManager.searchPeople(searchField.getText());
		    loadSearchBlocks("PeopleBlock.fxml", users);
	    }
	    else if(currentSearchMode == SearchMode.REQUEST)
	    {
	    	createCommunityButton.setVisible(false);
	    	
		    List<User> users = SearchManager.searchRequests();
		    loadSearchBlocks("FriendRequest.fxml", users);
	    }
	    else if(currentSearchMode == SearchMode.FRIENDS) 
	    {
	    	createCommunityButton.setVisible(false);
	    	
		    List<User> users = SearchManager.getFriends();
		    loadSearchBlocks("FriendBlock.fxml", users);
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
	private VBox friendsList;
	@FXML
	private VBox emptyFriendBox;
	@FXML
	private VBox emptyServerBox;
	@FXML
	private VBox communityList;
	
    @FXML
    private void sidebarButtons(ActionEvent event) throws IOException, InterruptedException
    {
    	searchFriends(event);
    }
    
    public void addSidebarFriendBlock(User user) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/main/mainscene/sidebar/SBFriend.fxml")
            );

            Parent block = loader.load();

            block.getStylesheets().add(
                getClass()
                .getResource("/main/mainscene/sidebar/SidebarButtons.css")
                .toExternalForm()
            );

            SidebarController controller = loader.getController();

            controller.setUser(user);
            controller.setUsername(user.username);
            controller.setMainSceneController(this);

            friendsList.getChildren().add(block);
    }
    
	private void loadSidebarBlocks(List<User> users) throws IOException
	{
	    if(!users.isEmpty())
	    {
	        ((VBox) emptyFriendBox.getParent()).getChildren().remove(emptyFriendBox);

	        for(User user : users)
	        {
	            addSidebarFriendBlock(user);
	        }
	    }
//		else if(!servers.isEmpty)
//		{
//			emptyServerBox.getParent();
//			((VBox) emptyServerBox.getParent()).getChildren().remove(emptyServerBox);
//			
//			for(Server server : servers).... Don't delete above when adding servers			
//		}
	}
    
//--------------------MESSAGES-------------
    @FXML
    private VBox messageVBox;
    @FXML
    private TextField messageField;
    private Conversation conversation;
    @FXML
    private Label channelNameText;
    
    public void openChat(UUID receiverId) throws IOException, InterruptedException
    {
    	messageScroll.setVisible(true);
    	searchMenu.setVisible(false);
    	
    	channelNameText.setText(MessageManager.getUsernameById(receiverId));
    	
    	conversation = MessageManager.getConversation(receiverId, ServerManagement.getToken());
    	loadChat(conversation.getId(), ServerManagement.getToken());
    	MessageManager.subscribeToConversation(conversation.getId());
    }
    
    //This is for WEBSOCKET realtime messaging.
    public void receiveMessage(Message message) throws IOException
    {
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
    
	public void SendMessage(ActionEvent event) throws IOException, InterruptedException
	{
		if(conversation == null) return;
		
		MessageManager.sendMessageWebSocket(conversation.getId(), messageField.getText());
		Platform.runLater(() -> {
			messageField.requestFocus();
		    messageField.setText("");
		});
	}
	
	public void addMessage(Message message) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/message/MessageBlock.fxml"));
		
		Parent node = loader.load();
		node.getStylesheets().add(
        	    getClass()
        	    .getResource("/main/mainscene/message/Message.css")
        	    .toExternalForm()
        	);
		
		MessageController controller = loader.getController();
		controller.setMessage(message);
		
		messageVBox.getChildren().add(node);
		
		Platform.runLater(() -> {
		    messageScroll.setVvalue(1.0);
		});
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
