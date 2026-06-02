package main.mainscene.community;

import java.util.List;
import java.util.UUID;

public class CommunityCreatedEvent {

    private UUID communityId;
    private UUID conversationId;
    private String name;
    private String description;
    private boolean isPublic;
    private List<String> tags;

    public CommunityCreatedEvent(UUID communityId, UUID conversationId, String name, String description, boolean isPublic, List<String> tags) 
    {
        this.setCommunityId(communityId);
        this.setConversationId(conversationId);
        this.setName(name);
        this.setDescription(description);
        this.setPublic(isPublic);
        this.setTags(tags);
    }

	public UUID getCommunityId() {
		return communityId;
	}

	public void setCommunityId(UUID communityId) {
		this.communityId = communityId;
	}

	public UUID getConversationId() {
		return conversationId;
	}

	public void setConversationId(UUID conversationId) {
		this.conversationId = conversationId;
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

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

    
}