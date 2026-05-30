package main.mainscene.message;

import java.util.UUID;

public class MessageRequest {
    public UUID conversationId;
    public String token;
    public String content;
    public String type;
    
    public MessageRequest(UUID conversationId, String token, String content, String type)
    {
    	this.conversationId = conversationId;
    	this.token = token;
    	this.content = content;
    	this.type = type;
    }
}
