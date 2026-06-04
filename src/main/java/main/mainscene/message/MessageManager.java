package main.mainscene.message;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javafx.application.Platform;
import main.app.ServerManagement;
import main.app.WebSocketClientManager;
import main.mainscene.MainSceneController;

public class MessageManager {

	public static MessageLL messageLL;
	private static MainSceneController mainController;
	private static List<StompSession.Subscription> subscriptions = new ArrayList<>();
	
	public static void setMainController(MainSceneController controller)
	{
	    mainController = controller;
	}
	
	public static List<StompSession.Subscription> getSubscriptions()
	{
		return subscriptions;
	}
	
	
	public static List<Message> getMessages(UUID conversationId, String userToken) throws IOException, InterruptedException
	{
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ServerManagement.getAdress() + "/messages/get?conversationId=" + conversationId + "&token=" + userToken))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(response.body(), new TypeReference<List<Message>>() {});
	}
	
	public static Conversation getConversation(UUID receiverId, String userToken) throws IOException, InterruptedException
	{
        HttpClient client = HttpClient.newHttpClient();

        String json = """
        	    {
        	        "token": "%s",
        	        "user2": "%s"
        	    }
        	    """.formatted(userToken, receiverId);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ServerManagement.getAdress() + "/conversations/dm?token=" + userToken + "&user2=" + receiverId))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper.readValue(response.body(), Conversation.class);
	}
	
	public static Conversation getCommunityConversation(UUID communityId, String userToken) throws IOException, InterruptedException
	{
        HttpClient client = HttpClient.newHttpClient();

        String json = """
        	    {
        	        "token": "%s",
        	        "communityId": "%s"
        	    }
        	    """.formatted(userToken, communityId);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ServerManagement.getAdress() + "/conversations/community?token=" + userToken + "&communityId=" + communityId))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.readValue(response.body(), Conversation.class);
	}
	
	public static Message sendMessageWebSocket(UUID conversationId, String text)
	{
		Message message = new Message();
		message.setConversationId(conversationId);
		message.setMessageType("TEXT");
		message.setContent(text);
		messageLL.add(message);
		
		WebSocketClientManager.getSession().send("/app/conversation.send", new MessageRequest(message.getConversationId(), ServerManagement.getToken(),
				message.getContent(), message.getMessageType()));
		
		System.out.println("Message sent");
	    return message;
	}
	
	public static String getUsernameById(UUID id)
	{
		HttpClient client = HttpClient.newHttpClient();
		
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(ServerManagement.getAdress() + "/messages/username?token=" + ServerManagement.getToken() + "&id=" + id))
	            .GET()
	            .build();
	    
	    HttpResponse<String> response;
	    try {
	    	response = client.send(request, HttpResponse.BodyHandlers.ofString());	    	
	    }catch (Exception e) {
			System.out.println("No connection to the server...");
			return null;
		}
	    
	    String username = response.body();
	    return username;
	}
	
	//Rather than subscribing to conversations separately, subscribing a channel like /user/queue/updates/userid could be a better idea
	public static void subscribeToConversation(UUID conversationId) 
	{
//		System.out.println("SUB SESSION = " + WebSocketClientManager.getSession());
		for(StompSession.Subscription sub : subscriptions)
		{
			sub.unsubscribe();
		}
		subscriptions.clear();
		
		StompSession.Subscription sub = WebSocketClientManager.getSession().subscribe("/topic/conversation/" + conversationId,
	        new StompFrameHandler() {
	    	
	            @Override
	            public Type getPayloadType(StompHeaders headers) 
	            {
	                return Message.class;
	            }

	            @Override
	            public void handleFrame(StompHeaders headers, Object payload) 
	            {
	            	Message msg = (Message) payload;
	            	
	                Platform.runLater(() -> {
	                    try 
	                    {
                    		mainController.addMessage(msg);
                    		System.out.println("Message Received");
	                    } 
	                    catch (IOException e) 
	                    {
	                        System.out.println("Something went wrong while adding a message");
	                        e.printStackTrace();
	                    }
	                });
	            }
	        }
	    );
		subscriptions.add(sub);
//	    System.out.println("Subscribed to = " + conversationId);
	}
}
