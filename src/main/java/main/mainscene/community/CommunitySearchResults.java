package main.mainscene.community;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunitySearchResults {
	private UUID id;
	
	private String name;
	private String description;
	private UUID ownerUserId;
	private UUID conversationId;
	private boolean isPublic;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UUID getOwnerUserId() {
		return ownerUserId;
	}
	public void setOwnerUserId(UUID ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	public UUID getConversationId() {
		return conversationId;
	}
	public void setConversationId(UUID conversationId) {
		this.conversationId = conversationId;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
}
