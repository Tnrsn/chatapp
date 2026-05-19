package main;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.TextField;

public class DBManagement {

	private String jdbcURL = "jdbc:postgresql://localhost:5432/ChatAppDB";
	private String username = "postgres";
	private String password = "12345";
	
	private Connection connection;
	
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
	            .uri(URI.create("http://localhost:8080/auth/register"))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(json))
	            .build();

	    HttpResponse<String> response =
	            client.send(request, HttpResponse.BodyHandlers.ofString());

	    System.out.println("Response: " + response.statusCode());
	    System.out.println(response.body());
	}
	
	public boolean SignIn(TextField usernameField, TextField passwordField) throws IOException, InterruptedException {
		String username = usernameField.getText();
		String password = passwordField.getText();
		
		HttpClient client = HttpClient.newHttpClient();

	    String json = """
	    {
	        "username": "%s",
	        "password": "%s"
	    }
	    """.formatted(username, password);

	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create("http://localhost:8080/auth/login"))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(json))
	            .build();

	    HttpResponse<String> response =
	            client.send(request, HttpResponse.BodyHandlers.ofString());
	    
//	    System.out.println("Response: " + response.statusCode());
	    if(response.body() == "User not found" || response.body() == "Wrong Password")
	    {
	    	return false;
	    }
	    else if(response.body() == "Login success")
	    {
	    	return true;
	    }
	    
	    return false;
//	    System.out.println(response.body());
	}
}
