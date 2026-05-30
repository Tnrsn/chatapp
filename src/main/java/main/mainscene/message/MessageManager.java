package main.mainscene.message;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
	public static void setMainController(MainSceneController controller)
	{
	    mainController = controller;
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
        return mapper.readValue(response.body(), new TypeReference<Conversation>() {});
	}
	
	//I'll delete here if websocket wont work
//	public static Message sendMessage(UUID conversationId, String text) throws IOException, InterruptedException
//	{
//	    HttpClient client = HttpClient.newHttpClient();
//	    String json = """
//	    {
//	        "conversationId": "%s",
//	        "token": "%s",
//	        "content": "%s",
//	        "type": "TEXT"
//	    }
//	    """.formatted(conversationId, ServerManagement.getToken(), text);
//
//	    HttpRequest request = HttpRequest.newBuilder()
//	            .uri(URI.create(ServerManagement.getAdress() + "/messages/send"))
//	            .header("Content-Type", "application/json")
//	            .POST(HttpRequest.BodyPublishers.ofString(json))
//	            .build();
//
//	    client.send(request, HttpResponse.BodyHandlers.ofString());
//	    
//	    Message m = new Message();
//	    m.setConversationId(conversationId);
//	    m.setContent("TEXT");
//	    m.setContent(text);
//	    messageLL.add(m);
//	    
//	    return m;
//	}
	
	public static Message sendMessageWebSocket(UUID conversationId, String text)
	{
		Message message = new Message();
		message.setConversationId(conversationId);
		message.setMessageType("TEXT");
		message.setContent(text);
		messageLL.add(message);
		
		WebSocketClientManager.getSession().send("/app/conversation", new MessageRequest(message.getConversationId(), ServerManagement.getToken(),
				message.getContent(), message.getMessageType()));
		
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
	
	public static void subscribeToConversation(UUID conversationId)
	{
		System.out.println("SUB SESSION = " + WebSocketClientManager.getSession());
		StompSession.Subscription sub = WebSocketClientManager.getSession().subscribe("/topic/conversation/" + conversationId,
	        new StompFrameHandler() {
	    	
//	            @Override
//	            public Type getPayloadType(StompHeaders headers) 
//	            {
//	                return Message.class;
//	            }
//
//	            @Override
//	            public void handleFrame(StompHeaders headers, Object payload) 
//	            {
//	                Message msg = (Message) payload;
//	                System.out.println("MESSAGE RECEIVED");
//	                //
//	                Platform.runLater(() -> {
//	                    try {
//							mainController.receiveMessage(msg);
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//	                });	                
//	                //	                
//	            }
	            @Override
	            public Type getPayloadType(StompHeaders headers) 
	            {
	                return String.class;
	            }

	            @Override
	            public void handleFrame(StompHeaders headers, Object payload) //convertandsend should run here but it wont at the moment
	            {
	                System.out.println("MESSAGE RECEIVED");
	                //	                
	            }
	        }
	    );
	    System.out.println("Subscribed to = " + conversationId);
	}
}
