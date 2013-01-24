package playdate.common.steam.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import models.PDUser;
import models.SteamFriend;
import models.SteamGame;
import models.SteamRecommendation;
import models.SteamUser;

public class SteamComUtils {
	
	private static int TRIES = 5;
	
	public static List<SteamGame> getSteamGames(String steamId, Boolean profile) {
		List<SteamGame> result = null;
		SteamUserGameParser parser = new SteamUserGameParser(steamId, profile);
		try {
			result = parser.parseGameLibrary(TRIES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<SteamGame> getSteamGamesForUser(String user, Boolean profile) {
		PDUser pdUser = PDUser.getUser(user);
		return getSteamGames(pdUser.steamId, profile);
	}
	
	public static List<SteamFriend> getSteamFriends(String steamId, Boolean profile) {
		List<SteamFriend> result = null;
		SteamFriendParser parser = new SteamFriendParser(steamId, profile);
		try {
			result = parser.parseSteamFriends(TRIES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static SteamUser getSteamUser(String steamId, Boolean profile) {
		SteamUser result = null;
		SteamUserParser parser = new SteamUserParser(steamId, profile);
		try {
			result = parser.parseSteamFriends(TRIES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<SteamFriend> getSteamFriendsForUser(String user, Boolean profile) {
		PDUser pdUser = PDUser.getUser(user);
		return getSteamFriends(pdUser.steamId, profile);
	}
	
	public static List<SteamUser> getSteamUsersForUser(String user, Boolean profile) {
		List<SteamFriend> friends = getSteamFriendsForUser(user, profile);
		List<SteamUser> users = new ArrayList<SteamUser>();
		for(SteamFriend friend : friends) {
			SteamUser u = getSteamUser(friend.friendSteamId, true);
			users.add(u);
		}
		return users;
	}
	
	public static List<SteamRecommendation> getRecommendationsForUser(String user, Boolean profile, int records) {
		PDUser pdUser = PDUser.getUser(user);
		return getRecommendations(pdUser.steamId, profile, records);
	}
	
	public static List<SteamRecommendation> getRecommendations(String user, Boolean profile, int records) {
		List<SteamRecommendation> result = null;
		
		List<SteamGame> userGames = getSteamGames(user, profile);
		List<SteamFriend> userFriends = getSteamFriends(user, profile);
		List<SteamUser> userUsers = new ArrayList<SteamUser>();
		
		for(SteamFriend friend : userFriends) {
			userUsers.add(getSteamUser(friend.friendSteamId, true));
		}
		
		HashMap<String,SteamRecommendation> gameCount = fillGameCountMap(userGames);
		
		for(SteamUser friend : userUsers) {
			List<SteamGame> friendGames = getSteamGames(friend.steamId64, true);
			updateGameMap(gameCount, friendGames, friend);
		}
		
		Collection<SteamRecommendation> games = gameCount.values();
		List<SteamRecommendation> gs = new ArrayList<SteamRecommendation>(games);
		Collections.sort(gs, (Comparator<SteamRecommendation>)new RecomComparator());
		
		result = new ArrayList<SteamRecommendation>();
		for(int i=0;i<records;i++) {
			result.add(gs.get(i));
		}
		
		return result;
	}
	
	private static HashMap<String,SteamRecommendation> fillGameCountMap(List<SteamGame> 
			userGames) {
		HashMap<String,SteamRecommendation> gameCount = new HashMap<String,SteamRecommendation>();
		for(SteamGame g : userGames) {
			gameCount.put(g.name, new SteamRecommendation(g));
		}
		return gameCount; 
	}
	
	
	private static HashMap<String,SteamRecommendation> updateGameMap(
			HashMap<String,SteamRecommendation> recommendations, 
			List<SteamGame> 
			userGames, SteamUser friend) {
		for(SteamGame g : userGames) {
			if(recommendations.containsKey(g.name)) {
				SteamRecommendation r = recommendations.get(g.name);
				r.addFriend(friend);
			}
		}
		return recommendations; 
	}
	
}