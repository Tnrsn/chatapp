package main.mainscene.community;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.app.ServerManagement;

public class CommunityAPIClient {
	
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void createCommunity(String name, String description, boolean isPublic, List<String> tags) 
    {
        try {
            CommunityRequest request = new CommunityRequest();
            request.setName(name);
            request.setDescription(description);
            request.setPublic(isPublic);
            request.setTags(tags);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(ServerManagement.getAdress() + "/community/create?token=" + ServerManagement.getToken()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) 
            {
                System.out.println("Failed to create community: " + response.body());
            }
            else
            {
            	//open the community conversation from here
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean joinCommunity(UUID communityId) throws IOException, InterruptedException
    {
        HttpClient client = HttpClient.newHttpClient();
	    
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(communityId);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(ServerManagement.getAdress() + "/community/join?token=" + ServerManagement.getToken()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response;
	    try {
	    	response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
	    }catch (Exception e) {
			System.out.println("No connection to the server...");
			return false;
		}

        if (response.statusCode() != 200) 
        {
            System.out.println("Failed to join community: " + response.body());
        }
        else
        {
        	return true;
        }
        
        return false;
    }
    
    public static boolean quitCommunity(UUID conversationId) throws JsonProcessingException
    {
        HttpClient client = HttpClient.newHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(conversationId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ServerManagement.getAdress() + "/community/leave?token=" + ServerManagement.getToken()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response;

        try
        {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e)
        {
            System.out.println("No connection to the server...");
            return false;
        }

        if (response.statusCode() != 200)
        {
            System.out.println("Failed to leave community: " + response.body());
        }
        else
        {
        	return true;
        }
        
		return false;
    }
}
