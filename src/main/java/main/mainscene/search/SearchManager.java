package main.mainscene.search;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.app.ServerManagement;
import main.mainscene.user.User;

public class SearchManager {
	
	private static String adress = ServerManagement.getAdress();
	
	public static List<User> searchPeople(String search) throws IOException, InterruptedException
	{
		HttpClient client = HttpClient.newHttpClient();

	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/search/people?search=" + search))
	            .GET()
	            .build();

	    HttpResponse<String> response =
	            client.send(request, HttpResponse.BodyHandlers.ofString());
	    
	    ObjectMapper mapper = new ObjectMapper();
	    List<User> users = mapper.readValue(
	    	    response.body(),
	    	    new TypeReference<List<User>>() {}
	    	);
	    
	    for(User user : users)
	    {
	        System.out.println(user.username);
//	        System.out.println(user.email);
	        System.out.println(user.id);
	    }
	    
	    return null;
	}
}
