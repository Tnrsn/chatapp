package main.mainscene.message;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversation {

    private UUID id;
    private String name; // null for DMs
    private String type; // type of the conversation (direct or community).
    private UUID createdBy;
    private LocalDateTime createdAt;
    
    public void setId(UUID id)
    {
    	this.id = id;
    }
    
    public UUID getId()
    {
    	return id;
    }
    
    public void setType(String type)
    {
    	this.type = type;
    }
    
    public void setCreatedBy(UUID userId)
    {
    	createdBy = userId;
    }
    
    public void setCreatedAt(LocalDateTime date)
    {
    	createdAt = date;
    }
}
