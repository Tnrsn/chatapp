package main.mainscene.message;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message {

    private UUID id;

    private UUID conversationId;
    private UUID senderId;
    private String username;

    private String content;

    private String messageType; // file, image etc

    private LocalDateTime createdAt;
    
    public Message() {}
	
    public void setId(UUID id) 
    {
        this.id = id;
    }
    
    public UUID getId()
    {
    	return id;
    }
    
    public void setConversationId(UUID conversationId) 
    {
        this.conversationId = conversationId;
    }
    
    public UUID getConversationId()
    {
    	return conversationId;
    }
    
    public void setSenderId(UUID senderId) 
    {
        this.senderId = senderId;
    }
    
    public UUID getSenderId()
    {
    	return senderId;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
	public String getContent()
	{
		return content;
	}
	
	public void setMessageType(String messageType) 
	{
	    this.messageType = messageType;
	}
	
	public String getMessageType()
	{
		return messageType;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) 
	{
	    this.createdAt = createdAt;
	}
	
	public LocalDateTime getCreatedAt()
	{
		return createdAt;
	}
}
