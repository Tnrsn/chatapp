package main.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.control.TextField;
import main.login.LoginResponse;

public class ServerManagement {
	
	//"http://x.x.x.x:8080"
	//"http://26.94.2.223:8080" for radminvpn
	//"http://localhost:8080"
	private static String adress = "http://26.94.2.223:8080"; //DO NOT FORGET TO CHANGE WEBSOCKET ADRESS TOO IF YOU CHANGE THIS
	
	private static String usertoken;
	private static String username;
	private static boolean connectionError = false;

	public static boolean isConnectionError() {
	    return connectionError;
	}
	
	public static String getAdress()
	{
		return adress;
	}
	
	// /auth/
	//This function is called from LoginController.java
	public void SignUp(TextField usernameField, TextField passwordField, TextField emailField) throws IOException, InterruptedException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(2))
                .build();

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

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            connectionError = false;
        } catch (Exception e) {
            System.out.println("No connection to the server...");
            connectionError = true;
            return;
        }

        System.out.println(response.body());
        try {
            ObjectMapper mapper = new ObjectMapper();
            LoginResponse data = mapper.readValue(response.body(), LoginResponse.class);
            usertoken = data.token;
            this.username = data.username;
            return;
        } catch (Exception e) {
            return;
        }
    }
	
	//This function is called from LoginController.java
	public boolean SignIn(TextField emailField, TextField passwordField) throws IOException, InterruptedException {
        String username = emailField.getText();
        String password = passwordField.getText();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(2))
                .build();

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

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            connectionError = false;
        } catch (Exception e) {
            System.out.println("No connection to the server...");
            connectionError = true;
            return false;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            LoginResponse data = mapper.readValue(response.body(), LoginResponse.class);

            usertoken = data.token;
            this.username = data.username;

            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	
//Session Management (/session/)
	public void generateToken() throws IOException, InterruptedException
	{
		HttpClient client = HttpClient.newHttpClient();
		
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/session/create"))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.noBody())
	            .build();
	    
	    HttpResponse<String> response;
	    try {
	    	response = client.send(request, HttpResponse.BodyHandlers.ofString());	    	
	    }catch (Exception e) {
			System.out.println("No connection to the server...");
			return;
		}
	    
	    usertoken = response.body();
//	    System.out.println("Token: " + usertoken);
	}
	
	public static boolean isTokenValid() throws IOException, InterruptedException
	{
		HttpClient client = HttpClient.newHttpClient();

	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(adress + "/session/validate?token=" + usertoken))
	            .GET()
	            .build();

	    HttpResponse<String> response;
	    try {
	    	response = client.send(request, HttpResponse.BodyHandlers.ofString());	    	
	    }catch (Exception e) {
			System.out.println("No connection to the server...");
			return false;
		}
	    
	    return Boolean.parseBoolean(response.body());
	}
	
	public static String getToken()
	{
		return usertoken;
	}
	
	public static String getUsername()
	{
		return username;
	}
}
