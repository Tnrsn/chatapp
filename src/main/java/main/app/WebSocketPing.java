package main.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketPing {
	private boolean Status = true;
	private String type;
	
	public boolean getStatus()
	{
		return Status;
	}
	public void setStatus(boolean status)
	{
		this.Status = status;
	}
	
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
}