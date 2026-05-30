package main.app;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import org.springframework.web.socket.adapter.standard.StandardToWebSocketExtensionAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class WebSocketClientManager {

	private static StompSession session;
	
	public static StompSession getSession()
	{
		return session;
	}
	
	public static void connect() {
	    System.out.println("Connecting to the server...");

	    try 
	    {
	        StandardWebSocketClient client = new StandardWebSocketClient();

	        WebSocketStompClient stompClient = new WebSocketStompClient(client);
	        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

	        String url = "ws://localhost:8080/ws";

	        session = stompClient.connectAsync(url,
	            new StompSessionHandlerAdapter() 
	        	{
	                @Override
	                public void afterConnected(StompSession session, StompHeaders headers)
	                {
	                	System.out.println("SESSION ID = " + session);
	                    System.out.println("CONNECTED");
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
}
