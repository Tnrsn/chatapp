package launcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}
