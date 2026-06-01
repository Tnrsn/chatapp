package main.mainscene.sidebar;

import java.util.UUID;

public class FriendshipRequest {
	private UUID requesterId;
	private String userToken;
	
	public UUID getRequesterId()
	{
		return requesterId;
	}
	public void setRequesterId(UUID requesterId)
	{
		this.requesterId = requesterId;
	}
	
	public String getUserToken()
	{
		return userToken;
	}
	public void setUserToken(String userToken)
	{
		this.userToken = userToken;
	}
}
