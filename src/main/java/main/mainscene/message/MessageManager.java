package main.mainscene.message;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import main.app.ServerManagement;

public class MessageManager {

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
	
	public static void sendMessage(UUID conversationId, String text) throws IOException, InterruptedException
	{
	    HttpClient client = HttpClient.newHttpClient();
	    String json = """
	    {
	        "conversationId": "%s",
	        "token": "%s",
	        "content": "%s",
	        "type": "TEXT"
	    }
	    """.formatted(conversationId, ServerManagement.getToken(), text);

	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(ServerManagement.getAdress() + "/messages/send"))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(json))
	            .build();

	    client.send(request, HttpResponse.BodyHandlers.ofString());
	}
}
