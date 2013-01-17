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
import playdate.common.steam.parser.recommendation.SteamRecommendation;

public class SteamComUtils {
	public static List<SteamGame> getSteamGames(String steamId, Boolean profile) {
		List<SteamGame> result = null;
		SteamUserGameParser parser = new SteamUserGameParser(steamId, profile);
		try {
			result = parser.parseGameLibrary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<SteamGame> getSteamGamesForUser(String user, Boolean profile) {
		PDUser pdUser = PDUser.getUser(user);
		return getSteamGames(user, profile);
	}
	
	public static List<SteamFriend> getSteamFriends(String steamId, Boolean profile) {
		List<SteamFriend> result = null;
		SteamFriendParser parser = new SteamFriendParser(steamId, profile);
		try {
			result = parser.parseSteamFriends();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<SteamFriend> getSteamFriendsForUser(String user, Boolean profile) {
		PDUser pdUser = PDUser.getUser(user);
		return getSteamFriends(user, profile);
	}
	
	public static List<SteamRecommendation> getRecommendations(String user, Boolean profile) {
		List<SteamRecommendation> result = null;
		
		List<SteamGame> userGames = getSteamGames(user, profile);
		List<SteamFriend> userFriends = getSteamFriends(user, profile);
		HashMap<String,SteamRecommendation> gameCount = fillGameCountMap(userGames);
		
		for(SteamFriend friend : userFriends) {
			List<SteamGame> friendGames = getSteamGames(friend.friendSteamId, true);
			updateGameMap(gameCount, friendGames, friend);
		}
		
		Collection games = gameCount.values();
		List gs = new ArrayList(games);
		Collections.sort(gs, (Comparator<SteamRecommendation>)new RecomComparator());
		
		return gs;
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
			userGames, SteamFriend friend) {
		for(SteamGame g : userGames) {
			if(recommendations.containsKey(g.name)) {
				SteamRecommendation r = recommendations.get(g.name);
				r.addFriend(friend);
			}
		}
		return recommendations; 
	}
	
}