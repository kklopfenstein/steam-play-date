package playdate.common.steam.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import models.PDUser;
import models.SteamFriend;
import models.SteamGame;
import models.SteamRecommendation;
import models.SteamUser;
import play.Logger;
import playdate.common.steam.parser.exception.SteamParserException;

public class SteamComUtils {
	
	private static int TRIES = 50;
	
	public static List<SteamGame> getSteamGames(String steamId, Boolean profile) 
			throws SteamParserException {
		List<SteamGame> result = null;
		SteamUserGameParser parser = new SteamUserGameParser(steamId, profile);
		try {
			result = parser.parseGameLibrary(TRIES);
		} catch (Exception e) {
			throw new SteamParserException(e);
		}
		return result;
	}
	
	public static List<SteamGame> getSteamGamesForUser(String user, Boolean profile) 
		throws SteamParserException {
		PDUser pdUser = PDUser.getUser(user);
		return getSteamGames(pdUser.steamId, profile);
	}
	
	public static List<SteamFriend> getSteamFriends(String steamId, Boolean profile) 
			throws SteamParserException {
		List<SteamFriend> result = null;
		SteamFriendParser parser = new SteamFriendParser(steamId, profile);
		try {
			result = parser.parseSteamFriends(TRIES);
		} catch (Exception e) {
			throw new SteamParserException(e);
		}
		return result;
	}
	
	public static LinkedList<SteamFriend> getSteamFriendsLinked(String steamId, Boolean profile) 
			throws SteamParserException {
		LinkedList<SteamFriend> result = new LinkedList<SteamFriend>();
		SteamFriendParser parser = new SteamFriendParser(steamId, profile);
		try {
			ArrayList<SteamFriend> r = parser.parseSteamFriends(TRIES);
			for(SteamFriend f : r) {
				result.add(f);
			}
		} catch (Exception e) {
			throw new SteamParserException(e);
		}
		return result;
	}
	
	public static SteamUser getSteamUser(String steamId, Boolean profile) 
			throws SteamParserException {
		SteamUser result = null;
		SteamUserParser parser = new SteamUserParser(steamId, profile);
		try {
			result = parser.parseSteamFriends(TRIES);
		} catch (Exception e) {
			throw new SteamParserException(e);
		}
		return result;
	}
	
	public static List<SteamFriend> getSteamFriendsForUser(String user, Boolean profile) 
		throws SteamParserException {
		PDUser pdUser = PDUser.getUser(user);
		return getSteamFriends(pdUser.steamId, profile);
	}
	
	public static List<SteamUser> getSteamUsersForUser(String user, Boolean profile) 
			throws SteamParserException {
		List<SteamFriend> friends = getSteamFriendsForUser(user, profile);
		List<SteamUser> users = new ArrayList<SteamUser>();
		for(SteamFriend friend : friends) {
			SteamUser u = getSteamUser(friend.friendSteamId, true);
			users.add(u);
		}
		return users;
	}
	
	public static List<SteamRecommendation> getRecommendationsForUser(String user, Boolean profile, int records) 
		throws SteamParserException {
		PDUser pdUser = PDUser.getUser(user);
		return getRecommendations(pdUser.steamId, profile, records);
	}
	
	public static List<SteamRecommendation> getRecommendations(String user, Boolean profile, int records) 
			throws SteamParserException {
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
		if(gs != null && gs.size() > 0) {
			for(int i=0;i<records;i++) {
				result.add(gs.get(i));
			}
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
	
	public static boolean isProfile(String str)  
	{  
		for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;  
	}
	
	public static List<SteamRecommendation> getRecommendationsAsynch(String user, Boolean profile, int records) 
			throws SteamParserException {
		List<SteamRecommendation> result = null;
		try {
			List<SteamGame> userGames = getSteamGames(user, profile);
			LinkedList<SteamFriend> userFriends = getSteamFriendsLinked(user, profile);
			final LinkedList<SteamUser> userUsers = new LinkedList<SteamUser>();
			
			int maxThreads = 15;
			ExecutorService executorService =
					new ThreadPoolExecutor(
						maxThreads, // core thread pool size
						maxThreads, // maximum thread pool size
						1, // time to wait before resizing the pool
						TimeUnit.MINUTES,
						new ArrayBlockingQueue<Runnable>(maxThreads, true),
						new ThreadPoolExecutor.CallerRunsPolicy());
	
			while(!userFriends.isEmpty()) {
				final SteamFriend fr = userFriends.pop();
				executorService.submit(new Runnable() {
					@Override
					public void run() {
						try {
							Logger.info("%--- getSteamUser worker thread started ---%");
							SteamUser user = getSteamUser(fr.friendSteamId, true);
							userUsers.add(user);
							Logger.info("%--- getSteamUser worker thread added " + user + " users is now of size " + userUsers.size() + "---%");
							Logger.info("%--- getSteamUser worker thread ended ---%");
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
	
			Logger.info("%--- Starting shutdown on executor service ---%");
			executorService.shutdown();
			Logger.info("%--- Shutdown on executor service terminated ---%");
			
			try {
				Logger.info("%--- Awaiting termination for 60 seconds --%");
				if(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
					Logger.info("%--- Shutdown now on Executor --%");
					executorService.shutdownNow();
				}
				
				Logger.info("%--- Awaiting termination for second 60 seconds --%");
				if(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
					Logger.info("%--- Not shutdown! ---%");
				}
			} catch(InterruptedException ex) {
				Logger.info("%--- InterruptedException waiting for executorService --%");
				executorService.shutdownNow();
				Thread.currentThread().interrupt();
			}
			
			Logger.info("%--- Ended building users, user count is " + userUsers.size() + " ---%");
			
			executorService =
					new ThreadPoolExecutor(
						maxThreads, // core thread pool size
						maxThreads, // maximum thread pool size
						1, // time to wait before resizing the pool
						TimeUnit.MINUTES,
						new ArrayBlockingQueue<Runnable>(maxThreads, true),
						new ThreadPoolExecutor.CallerRunsPolicy());
			
			final HashMap<String,SteamRecommendation> gameCount = fillGameCountMap(userGames);
			
			while(!userUsers.isEmpty()) {
				Logger.info("%--- Building first worker thread. Users size is " + userUsers.size() + " ---%");
				final SteamUser friend = userUsers.pop();
				executorService.submit(new Runnable() {
					@Override
					public void run() {
						try {
							Logger.info("%--- getSteamGames worker thread started ---%");
							List<SteamGame> friendGames = getSteamGames(friend.steamId64, true);
							updateGameMap(gameCount, friendGames, friend);
							Logger.info("%--- getSteamGames worker thread ended ---%");
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
	
			Logger.info("%--- Starting wait on executor service ---%");
			executorService.shutdown();
			Logger.info("%--- Wait on executor service terminated ---%");
			
			try {
				Logger.info("%--- Awaiting termination for 60 seconds --%");
				if(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
					Logger.info("%--- Shutdown now on Executor --%");
					executorService.shutdownNow();
				}
				
				Logger.info("%--- Awaiting termination for second 60 seconds --%");
				if(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
					Logger.info("%--- Not shutdown! ---%");
				}
			} catch(InterruptedException ex) {
				Logger.info("%--- InterruptedException waiting for executorService --%");
				executorService.shutdownNow();
				Thread.currentThread().interrupt();
			}
			
			Collection<SteamRecommendation> games = gameCount.values();
			List<SteamRecommendation> gs = new ArrayList<SteamRecommendation>(games);
			Collections.sort(gs, (Comparator<SteamRecommendation>)new RecomComparator());
			
			result = new ArrayList<SteamRecommendation>();
			if(gs != null && gs.size() > 0) {
				for(int i=0;i<records;i++) {
					result.add(gs.get(i));
				}
			}
		} catch(Exception e) {
			throw new SteamParserException(e);
		}
		
		return result;
	}
	
}