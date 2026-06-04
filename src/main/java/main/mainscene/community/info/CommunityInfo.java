package main.mainscene.community.info;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import main.mainscene.community.CommunitySearchResults;
import main.mainscene.user.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityInfo {
	private CommunitySearchResults info;
	private List<User> members;
	
	public CommunityInfo() {}
	
	public CommunityInfo(CommunitySearchResults info, List<User> members)
	{
		this.info = info;
		this.members = members;
	}
	
	public CommunitySearchResults getInfo() {
		return info;
	}
	public void setInfo(CommunitySearchResults info) {
		this.info = info;
	}
	public List<User> getMembers() {
		return members;
	}
	public void setMembers(List<User> members) {
		this.members = members;
	}
}
