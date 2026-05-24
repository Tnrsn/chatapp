package app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.control.TextField;
import login.LoginResponse;

public class ServerManagement {
	
	//"http://x.x.x.x:8080"
	//"http://26.94.2.223:8080" for radminvpn
	//"http://localhost:8080"
	private static String adress = "http://localhost:8080";
	
	private static String usertoken;
	
	// /auth/
	//This function is called from LoginController.java
	public void SignUp(TextField usernameField, TextField passwordField, TextField emailField) throws IOException, InterruptedException
	{
	    String username = usernameField.getText();
	    String password = passwordField.getText();
	    String email = emailField.getText();

	    HttpClient client = HttpClient.newHttpClient();

	    String json = """
	    {
	        "username": "%s",
	        "password": "%s",
	        "email": "%s"
	    }
	    """.formatted(username, password, email);

	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/auth/register"))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(json))
	            .build();

	    HttpResponse<String> response =
	            client.send(request, HttpResponse.BodyHandlers.ofString());

	    System.out.println("Response: " + response.statusCode());
	    System.out.println(response.body());
	}
	
	//This function is called from LoginController.java
	public boolean SignIn(TextField emailField, TextField passwordField) throws IOException, InterruptedException {
		String username = emailField.getText();
		String password = passwordField.getText();
		
		HttpClient client = HttpClient.newHttpClient();

	    String json = """
	    {
	        "email": "%s",
	        "password": "%s"
	    }
	    """.formatted(username, password);

	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/auth/login"))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(json))
	            .build();

	    HttpResponse<String> response =
	            client.send(request, HttpResponse.BodyHandlers.ofString());
	    
	    System.out.println(response.body());
	    
	    try { //I'll fix here later, it returns username too and I'm not using it... but it works
	    	ObjectMapper mapper = new ObjectMapper();
	    	LoginResponse data = mapper.readValue(response.body(), LoginResponse.class);
	    	
		    usertoken = data.token;
	    	return true; //login successful
		} catch (Exception e) {
			return false; //login unsuccessful
		}
	}
	
	
//Session Management (/session/)
	public void getToken() throws IOException, InterruptedException
	{
		HttpClient client = HttpClient.newHttpClient();
		
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/session/create"))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.noBody())
	            .build();
	    
	    HttpResponse<String> response =
	            client.send(request, HttpResponse.BodyHandlers.ofString());
	    
	    usertoken = response.body();
	    System.out.println("Token: " + usertoken);
	}
	
	public static boolean isTokenValid() throws IOException, InterruptedException
	{
		HttpClient client = HttpClient.newHttpClient();

	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/session/validate?token=" + usertoken))
	            .GET()
	            .build();

	    HttpResponse<String> response =
	            client.send(request, HttpResponse.BodyHandlers.ofString());
	    
	    return Boolean.parseBoolean(response.body());
	}
	
	public static String getUsername() throws IOException, InterruptedException
	{
		HttpClient client = HttpClient.newHttpClient();
		System.out.println("AAAAAA" + usertoken);
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/auth/username?token=" + usertoken))
	            .GET()
	            .build();
	    
	    HttpResponse<String> response =
	            client.send(request, HttpResponse.BodyHandlers.ofString());
	    
	    String username = response.body();
	    System.out.println("Username " + username);
	    return username;
	}
//****
}
