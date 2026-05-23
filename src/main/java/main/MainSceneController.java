package main;

import java.io.IOException;

import javafx.event.ActionEvent;

public class MainSceneController {
	
	public void SendMessage(ActionEvent event) throws IOException, InterruptedException
	{
		if(ServerManagement.isTokenValid())
		{
			System.out.println("Valid");
		}
		else
		{
			System.out.println("Not Valid");
		}
	}
}
