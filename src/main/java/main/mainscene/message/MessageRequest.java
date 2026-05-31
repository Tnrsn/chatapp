package main.mainscene.message;

import java.util.UUID;

public class MessageRequest {
    private UUID conversationId;
    private String token;
    private String content;
    private String type;
    
    public MessageRequest() {}
    public MessageRequest(UUID conversationId, String token, String content, String type)
    {
    	this.conversationId = conversationId;
    	this.token = token;
    	this.content = content;
    	this.type = type;
    }
    
    public UUID getConversationId()
    {
    	return conversationId;
    }
    
    public void setConversationId(UUID id)
    {
    	this.conversationId = id;
    }
    
    public String getToken()
    {
    	return token;
    }
    public void setToken(String token)
    {
    	this.token = token;
    }
    
    public String getContent()
    {
    	return content;
    }
	public void setContent(String content)
	{
		this.content = content;
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
