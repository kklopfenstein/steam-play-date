package models;

import java.util.ArrayList;
import java.util.List;


public class SteamRecommendation {
	private List<SteamFriend> friends;
	private SteamGame game;
	
	public SteamRecommendation(SteamGame game) {
		this.game = game;
	}
	
	public SteamRecommendation(List<SteamFriend> friends, SteamGame game) {
		this.friends = friends;
		this.game = game;
	}

	public List<SteamFriend> getFriends() {
		return friends;
	}

	public void setFriends(List<SteamFriend> friends) {
		this.friends = friends;
	}
	
	public void addFriend(SteamFriend friend) {
		if(friends == null) {
			friends = new ArrayList<SteamFriend>();
		}
		friends.add(friend);
	}

	public SteamGame getGame() {
		return game;
	}

	public void setGame(SteamGame game) {
		this.game = game;
	}

	@Override
	public String toString() {
		StringBuffer str =  new StringBuffer("SteamRecommendation [friends=");
		if(friends != null && friends.size() > 0) {
			for(SteamFriend friend : friends) {
				str.append(friend.toString());
			}
		}
		String str2 = ", game=" + game
				+ "]";
		return str.append(str2).toString();
	}
	
}
