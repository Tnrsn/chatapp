package main.mainscene;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.app.ServerManagement;
import main.app.WebSocketClientManager;
import main.app.WebSocketPing;
import main.app.WindowController;
import main.mainscene.community.Community;
import main.mainscene.community.CommunitySearchResults;
import main.mainscene.community.CreateCommunityController;
import main.mainscene.community.info.ServerInfoController;
import main.mainscene.communityblock.CommunityBlockController;
import main.mainscene.message.Conversation;
import main.mainscene.message.Message;
import main.mainscene.message.MessageController;
import main.mainscene.message.MessageLL;
import main.mainscene.message.MessageManager;
import main.mainscene.message.MessageNode;
import main.mainscene.peopleblock.PeopleBlockController;
import main.mainscene.peopleblock.SearchMode;
import main.mainscene.search.SearchManager;
import main.mainscene.sidebar.SBCommunityBlocksController;
import main.mainscene.sidebar.SBFriendBlocksController;
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
	
	@FXML 
	private Button btnCommunities;
	@FXML 
	private Button btnPeople;
	@FXML 
	private Button btnFriends;
	@FXML 
	private Button btnRequests;
	
	public void initialize()
	{
		//Sets the window draggable
	    Platform.runLater(() -> {
	        Stage stage = (Stage)toolBar.getScene().getWindow();

	        WindowController.setDraggable(toolBar, stage);
	        WindowController.setDraggable(sideDragBar, stage);
	    });
	    
	    MessageManager.setMainController(this);
	    WebSocketClientManager.connect(this);

	    //Sets username label on main scene load
	    usernameLabel.setText(ServerManagement.getUsername());
	    
	    //Unfocuses the search bar if clicked on anywhere else
	    root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
	        if (event.getTarget() != searchField) 
	        {
	            root.requestFocus();
	        }
	    });
	    
	    loadSideBarOnInit();
	    
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
	
	public String getUserId()
	{
		HttpClient client = HttpClient.newHttpClient();
		
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(ServerManagement.getAdress() + "/session/getid?token=" + ServerManagement.getToken()))
	            .header("Content-Type", "application/json")
	            .GET()
	            .build();
	    
	    HttpResponse<String> response;
	    try {
	    	response = client.send(request, HttpResponse.BodyHandlers.ofString());	    	
	    }catch (Exception e) {
			System.out.println("No connection to the server...");
			return null;
		}

	    String userId = response.body().replace("\"", "");
	    return userId;
	}
	
	public void subscribeCommunityCreateEvent(String userId)
	{
		WebSocketClientManager.getSession().subscribe("/topic/community/refresh/" + userId, new StompFrameHandler() {

		    @Override
		    public Type getPayloadType(StompHeaders headers) {
		        return WebSocketPing.class;
		    }

		    @Override
		    public void handleFrame(StompHeaders headers, Object payload) {
		    	Platform.runLater(() ->
		    	{
		    		System.out.println("AAAAAAAAAA");
					try {
						refreshCommunitySideBar();
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	});
		    }
		});
	}
	
	public void subscribeFriendsEvents(String userId)
	{
		System.out.println("/topic/friends/refresh/" + userId);
	    
	    WebSocketClientManager.getSession().subscribe(
	            "/topic/friends/refresh/" + userId,
	            new StompFrameHandler()
	            {
	                @Override
	                public Type getPayloadType(StompHeaders headers)
	                {
	                    return WebSocketPing.class;
	                }

	                @Override
	                public void handleFrame(StompHeaders headers, Object payload)
	                {
	                    Platform.runLater(() ->
	                    {
	                    	try {
	                    		System.out.println("Test");
	                    		friendsList.getChildren().clear();
								loadSidebarFriendBlocks();
							} catch (IOException | InterruptedException e) {
								e.printStackTrace();
							}
	                    });
	                }
	            }
	        );
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
	
// --------------------------Stack Pane (Pop ups)------------------------------------
	
	@FXML
	private StackPane mainStack;
	private EventHandler<KeyEvent> escHandler;
	@FXML
	private Community currentCommunity;
	
	@FXML
	public void openCreateServer() throws IOException
	{
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/community/CreateCommunity.fxml"));

        Parent popup = loader.load();
        popup.getStylesheets().add(getClass().getResource("/main/mainscene/community/CreateCommunity.css").toExternalForm());
        
        Region overlay = new Region();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        overlay.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        mainStack.getChildren().addAll(overlay, popup);

        StackPane.setAlignment(popup, Pos.CENTER);
        StackPane.setAlignment(popup, Pos.CENTER);
        
        CreateCommunityController controller = loader.getController();
        controller.setMainSceneController(this);
        controller.setOverlay(overlay);
        controller.setPopup(popup);
        
        escHandler = event -> 
        {
            if (event.getCode() == KeyCode.ESCAPE) {
                closePopup(overlay, popup);
            }
        };

        mainStack.getScene().addEventFilter(KeyEvent.KEY_PRESSED, escHandler);
	}

	public void closePopup(Node overlay, Node popup) {

	    mainStack.getChildren().removeAll(overlay, popup);

	    if (escHandler != null) {
	        mainStack.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, escHandler);
	        escHandler = null;
	    }
	}
	
	@FXML
    public void openConversationInfo() throws IOException, InterruptedException
    {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/community/info/ServerInfo.fxml"));

        Parent popup = loader.load();
        popup.getStylesheets().add(getClass().getResource("/main/mainscene/community/info/ServerInfo.css").toExternalForm());
        
        ServerInfoController controller = loader.getController();
        controller.setMainSceneController(this);
        
        controller.setInfos(SearchManager.getCommunityInfo(currentCommunity.getId()));
        
        Region overlay = new Region();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        overlay.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        mainStack.getChildren().addAll(overlay, popup);

        StackPane.setAlignment(popup, Pos.CENTER);
        StackPane.setAlignment(popup, Pos.CENTER);
        
        escHandler = event -> 
        {
            if (event.getCode() == KeyCode.ESCAPE) {
                closePopup(overlay, popup);
            }
        };

        mainStack.getScene().addEventFilter(KeyEvent.KEY_PRESSED, escHandler);
    }
	
// -------------------------SEARCH MENU---------------------------------------
	private SearchMode currentSearchMode = SearchMode.COMMUNITIES;
	
	@FXML
	private Button createCommunityButton;
	
	private void resetTabColors() 
	{
		if (SearchMode.COMMUNITIES != currentSearchMode) btnCommunities.setStyle("");
		if (SearchMode.PEOPLE != currentSearchMode) btnPeople.setStyle("");
		if (SearchMode.FRIENDS != currentSearchMode) btnFriends.setStyle("");
		if (SearchMode.REQUEST != currentSearchMode) btnRequests.setStyle("");
	}
	
	private void loadSearchBlocks(String FXMLName, List<User> users) throws IOException
	{
	    for(User user : users)
	    {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/peopleblock/" + FXMLName));
	         
	        Parent block = loader.load();
	        block.getStylesheets().add(
	        	    getClass()
	        	    .getResource("/main/mainscene/peopleblock/PeopleBlock.css")
	        	    .toExternalForm());
	        PeopleBlockController controller = loader.getController();
	        
	        controller.setMainController(this);
	        controller.setName(user.username);
	        controller.setUserId(user.id);
	        
	        searchResults.getChildren().add(block);
	    }
	}
	
	private void loadSearchCommunityBlocks(List<CommunitySearchResults> communities) throws IOException
	{
	    for(CommunitySearchResults result : communities)
	    {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/communityblock/CommunityBlock.fxml"));
	         
	        Parent block = loader.load();
	        block.getStylesheets().add(getClass().getResource("/main/mainscene/communityblock/CommunityBlock.css").toExternalForm());
	        
	        CommunityBlockController controller = loader.getController();
	        
	        controller.setMainController(this);
	        controller.setCommunity(result.getCommunity());
	        controller.setCommunityNameLabel(result.getCommunity().getName());
	        controller.setCommunityTagButtons(result.getTags());
	        controller.setCommunityDescriptionLabel(result.getCommunity().getDescription());
	        
	        searchResults.getChildren().add(block);
	    }
	}
	
	@FXML
	private void handleSearch(ActionEvent event) throws IOException, InterruptedException {
	    System.out.println("Searching...");
	    searchResults.getChildren().clear();
	    
	    messageScroll.setVisible(false);
	    searchMenu.setVisible(true);
	    
	    if(currentSearchMode == SearchMode.COMMUNITIES)
	    {
	    	List<CommunitySearchResults> communities = SearchManager.searchCommunities(searchField.getText());
	    	loadSearchCommunityBlocks(communities);
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
		if (btnCommunities != null) btnCommunities.setStyle("-fx-background-color: #548C2F; -fx-text-fill: #FFFFFF; -fx-border-color: transparent;");
		
		currentSearchMode = SearchMode.COMMUNITIES;
		handleSearch(event);
		resetTabColors();
	}
	
	@FXML
	public void searchPeople(ActionEvent event) throws IOException, InterruptedException
	{
		if (btnPeople != null) btnPeople.setStyle("-fx-background-color: #548C2F; -fx-text-fill: #FFFFFF; -fx-border-color: transparent;");
		
		currentSearchMode = SearchMode.PEOPLE;
		handleSearch(event);
		resetTabColors();
	}
	
	@FXML
	public void searchRequests(ActionEvent event) throws IOException, InterruptedException
	{
		if (btnRequests != null) btnRequests.setStyle("-fx-background-color: #548C2F; -fx-text-fill: #FFFFFF; -fx-border-color: transparent;");
		
		currentSearchMode = SearchMode.REQUEST;
		handleSearch(event);
		resetTabColors();
	}
	
	@FXML
	public void searchFriends(ActionEvent event) throws IOException, InterruptedException
	{
		if (btnFriends != null) btnFriends.setStyle("-fx-background-color: #548C2F; -fx-text-fill: #FFFFFF; -fx-border-color: transparent;");
		
		currentSearchMode = SearchMode.FRIENDS;
		handleSearch(event);
		resetTabColors();
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
	
	private void loadSideBarOnInit()
	{
		try {
			loadSidebarFriendBlocks();
			loadSidebarCommunityBlocks();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
    @FXML
    private void sidebarButtons(ActionEvent event) throws IOException, InterruptedException
    {
    	searchFriends(event);
    }
    
    public void addSidebarFriendBlock(User user) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/sidebar/SBFriend.fxml"));

        Parent block = loader.load();

        block.getStylesheets().add(
            getClass()
            .getResource("/main/mainscene/sidebar/SidebarButtons.css")
            .toExternalForm()
        );

        SBFriendBlocksController controller = loader.getController();

        controller.setUser(user);
        controller.setUsername(user.username);
        controller.setMainSceneController(this);

        friendsList.getChildren().add(block);
    }
    
	private void loadSidebarFriendBlocks() throws IOException, InterruptedException
	{
		List<User> users = SearchManager.getFriends();
		
	    if(!users.isEmpty())
	    {
	    	if (emptyFriendBox.getParent() != null) 
	        {
	            ((VBox) emptyFriendBox.getParent()).getChildren().remove(emptyFriendBox);
	        }
	    	
	        for(User user : users)
	        {
	            addSidebarFriendBlock(user);
	        }
	    }
	}
    
	private void loadSidebarCommunityBlocks() throws IOException, InterruptedException
	{
		List<Community> communities = SearchManager.getCommunities();
		
	    if(!communities.isEmpty())
	    {
	    	if (emptyServerBox.getParent() != null) 
	        {
	            ((VBox) emptyServerBox.getParent()).getChildren().remove(emptyServerBox);
	        }
	    	
	        for(Community community : communities)
	        {
	        	System.out.println("Community name; " + community.getName());
	            addSidebarCommunityBlock(community);
	        }
	    }
	}
	
	private void addSidebarCommunityBlock(Community community) throws IOException
	{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainscene/sidebar/SBCommunity.fxml"));

        Parent block = loader.load();

        block.getStylesheets().add(
            getClass()
            .getResource("/main/mainscene/sidebar/SidebarButtons.css")
            .toExternalForm()
        );

        SBCommunityBlocksController controller = loader.getController();

        controller.setCommunity(community);
        controller.setCommunityName(community.getName());
        controller.setMainSceneController(this);

        communityList.getChildren().add(block);
	}
	
	@FXML //I'll delete this function later, and connect Friend button to searchFriends() directly
	private void openFriendsMenu(ActionEvent event) throws IOException, InterruptedException
	{
		searchFriends(event);
	}
	
	private void refreshCommunitySideBar() throws IOException, InterruptedException
	{
		communityList.getChildren().clear();
		loadSidebarCommunityBlocks();
	}
//--------------------MESSAGES-------------
    @FXML
    private VBox messageVBox;
    @FXML
    private TextField messageField;
    private Conversation conversation;
    @FXML
    private Label channelNameText;
    @FXML
    private Button conversationInfo;
    
    public void openDMChat(UUID receiverId) throws IOException, InterruptedException
    {
    	messageScroll.setVisible(true);
    	searchMenu.setVisible(false);

		channelNameText.setText(MessageManager.getUsernameById(receiverId));    		
		channelNameText.setVisible(true);

    	conversation = MessageManager.getConversation(receiverId, ServerManagement.getToken());
    	loadChat(conversation.getId(), ServerManagement.getToken());
    	MessageManager.subscribeToConversation(conversation.getId());
    	
		Platform.runLater(() -> {
			conversationInfo.setVisible(false);
		    messageScroll.setVvalue(1.0);
		});
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
    
    public void openCommunityChat(Community community) throws IOException, InterruptedException
    {
    	messageScroll.setVisible(true);
    	searchMenu.setVisible(false);
    	
    	currentCommunity = community;
    	channelNameText.setVisible(true);
    	channelNameText.setText(community.getName());
    	conversation = MessageManager.getCommunityConversation(community.getId(), ServerManagement.getToken());
    	loadChat(conversation.getId(), ServerManagement.getToken());
    	MessageManager.subscribeToConversation(conversation.getId());
    	
		Platform.runLater(() -> {
			conversationInfo.setVisible(true);
		    messageScroll.setVvalue(1.0);
		});
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
    
    public void CloseChat()
    {
    	channelNameText.setVisible(false);
    	conversation = null;
    	messageVBox.getChildren().clear();
    	currentCommunity = null;
    	
		for(StompSession.Subscription sub : MessageManager.getSubscriptions())
		{
			sub.unsubscribe();
		}
    }
}