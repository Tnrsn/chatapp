package main.app;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import org.springframework.web.socket.adapter.standard.StandardToWebSocketExtensionAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import main.mainscene.MainSceneController;

public class WebSocketClientManager {

	private static StompSession session;
	private static Runnable onConnected;
	private static MainSceneController mainSceneController;
	
	public static StompSession getSession()
	{
		return session;
	}
	
	private static void setSession(StompSession s)
	{
		session = s;
	}
	
	public static void connect(MainSceneController controller) 
	{
	    System.out.println("Connecting to the server...");
	    mainSceneController = controller;
	    
	    try 
	    {
	        StandardWebSocketClient client = new StandardWebSocketClient();

	        ObjectMapper mapper = new ObjectMapper();
	        mapper.registerModule(new JavaTimeModule());
	        
	        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	        converter.setObjectMapper(mapper);
	        
	        WebSocketStompClient stompClient = new WebSocketStompClient(client);
	        stompClient.setMessageConverter(converter);
	        String url = "ws://26.94.2.223:8080/ws";

	        session = stompClient.connectAsync(
	        	    url,
	        	    new StompSessionHandlerAdapter() {

	        	        @Override
	        	        public void handleException(StompSession session,StompCommand command, StompHeaders headers, byte[] payload,
	        	        		Throwable exception)
	        	        {
	        	            System.out.println("STOMP EXCEPTION");
	        	            exception.printStackTrace();
	        	        }

	        	        @Override
	        	        public void handleTransportError(StompSession session, Throwable exception) 
	        	        {
	        	            System.out.println("TRANSPORT ERROR");
	        	            exception.printStackTrace();
	        	        }

	        	        @Override
	        	        public void afterConnected(StompSession session, StompHeaders connectedHeaders)
	        	        {
	        	            System.out.println("CONNECTED");
	        	            
	        	            WebSocketClientManager.setSession(session);
	        	            
	        	            String userId = mainSceneController.getUserId();
	        	            mainSceneController.subscribeSocketQueueUpdates(userId);
	        	            mainSceneController.subscribeFriendsEvents(userId);
	        	        }
	        	    }
	        	).get();
	    } 
	    catch (Exception e) 
	    {
	        System.out.println("Connection Failed");
	        e.printStackTrace();
	    }
	}
	
	public static void setOnConnected(Runnable r)
	{
		onConnected = r;
	}
}
