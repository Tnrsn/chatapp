package main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.TextField;

public class ServerManagement {

	//This part should change for security sake
	private String jdbcURL = "jdbc:postgresql://localhost:5432/ChatAppDB";
	private String username = "postgres";
	private String password = "12345";
	
	private static String adress = "http://localhost:8080";
	//*********
	
	private Connection connection;
	private static String usertoken;
	
	public void ConnectServer()
	{
		try {
			connection = DriverManager.getConnection(jdbcURL, username, password);
			
			System.out.println("Connection to the server was successful.");
		} catch (SQLException e) {
			System.out.println("Connection to the server failed");
			e.printStackTrace();
		}
	}
	
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
	    
//	    System.out.println(response.statusCode());
	    if(response.statusCode() == 200)
	    {
	    	//login successful
	    	getToken();
	    	return true;
	    }
	    else
	    {
	    	//login unsuccessful
	    	return false;
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
//****
}
