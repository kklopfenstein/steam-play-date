package playdate.common.steam.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import models.PDUser;
import models.SteamFriend;
import models.SteamGame;
import models.SteamRecommendation;
import models.SteamUser;
import play.Logger;
import playdate.common.steam.asynch.SteamAPIExecutorService;
import playdate.common.steam.asynch.SteamWorker;
import playdate.common.steam.parser.exception.SteamParserException;
import playdate.common.steam.parser.validation.ValidationResults;

public class SteamComUtils {
	
	private static int TRIES = 5;
	
	public static List<SteamGame> getSteamGames(String steamId, Boolean profile, ValidationResults results) 
			throws SteamParserException {
		List<SteamGame> result = null;
		SteamUserGameParser parser = new SteamUserGameParser(steamId, profile);
		try {
			result = parser.parseGameLibrary(TRIES, results);
		} catch (Exception e) {
			throw new SteamParserException(e);
		}
		return result;
	}
	
	public static List<SteamGame> getSteamGamesForUser(String user, Boolean profile, ValidationResults results) 
		throws SteamParserException {
		PDUser pdUser = PDUser.getUser(user);
		return getSteamGames(pdUser.steamId, profile, results);
	}
	
	public static List<SteamFriend> getSteamFriends(String steamId, Boolean profile, ValidationResults results) 
			throws SteamParserException {
		List<SteamFriend> result = null;
		SteamFriendParser parser = new SteamFriendParser(steamId, profile);
		try {
			result = parser.parseSteamFriends(TRIES, results);
		} catch (Exception e) {
			throw new SteamParserException(e);
		}
		return result;
	}
	
	public static LinkedList<SteamFriend> getSteamFriendsLinked(String steamId, Boolean profile, ValidationResults results) 
			throws SteamParserException {
		LinkedList<SteamFriend> result = new LinkedList<SteamFriend>();
		SteamFriendParser parser = new SteamFriendParser(steamId, profile);
		try {
			ArrayList<SteamFriend> r = parser.parseSteamFriends(TRIES, results);
			for(SteamFriend f : r) {
				result.add(f);
			}
		} catch (Exception e) {
			throw new SteamParserException(e);
		}
		return result;
	}
	
	public static SteamUser getSteamUser(String steamId, Boolean profile, ValidationResults results) 
			throws SteamParserException {
		SteamUser result = null;
		SteamUserParser parser = new SteamUserParser(steamId, profile);
		try {
			result = parser.parseSteamFriends(TRIES, results);
		} catch (Exception e) {
			throw new SteamParserException(e);
		}
		return result;
	}
	
	public static List<SteamFriend> getSteamFriendsForUser(String user, Boolean profile, ValidationResults results) 
		throws SteamParserException {
		PDUser pdUser = PDUser.getUser(user);
		return getSteamFriends(pdUser.steamId, profile, results);
	}
	
	public static List<SteamUser> getSteamUsersForUser(String user, Boolean profile, ValidationResults results) 
			throws SteamParserException {
		List<SteamFriend> friends = getSteamFriendsForUser(user, profile, results);
		List<SteamUser> users = new ArrayList<SteamUser>();
		for(SteamFriend friend : friends) {
			SteamUser u = getSteamUser(friend.friendSteamId, true, results);
			users.add(u);
		}
		return users;
	}
	
	public static List<SteamRecommendation> getRecommendationsForUser(String user, Boolean profile, int records, ValidationResults results) 
		throws SteamParserException {
		PDUser pdUser = PDUser.getUser(user);
		return getRecommendations(pdUser.steamId, profile, records, results);
	}
	
	public static List<SteamRecommendation> getRecommendations(String user, Boolean profile, int records, ValidationResults results) 
			throws SteamParserException {
		List<SteamRecommendation> result = null;
		
		List<SteamGame> userGames = getSteamGames(user, profile, results);
		List<SteamFriend> userFriends = getSteamFriends(user, profile, results);
		List<SteamUser> userUsers = new ArrayList<SteamUser>();
		
		for(SteamFriend friend : userFriends) {
			userUsers.add(getSteamUser(friend.friendSteamId, true, results));
		}
		
		HashMap<String,SteamRecommendation> gameCount = fillGameCountMap(userGames);
		
		for(SteamUser friend : userUsers) {
			List<SteamGame> friendGames = getSteamGames(friend.steamId64, true, results);
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
	
	/**
	 * Determine if any thread is running
	 * @param jobs
	 * @return
	 */
	private static boolean jobsOpen(ArrayList<SteamWorker> jobs) 
		throws SteamParserException {
		boolean result = false;
		try {
			for(SteamWorker job : jobs) {
				job.isFinished();
			}
		} catch(InterruptedException e) {
			throw new SteamParserException(e);
		}
		return result;
	}
	
	public static List<SteamRecommendation> getRecommendationsAsynch(String user, Boolean profile, int records,
			final ValidationResults results) 
			throws SteamParserException {
		List<SteamRecommendation> result = null;
		long startTime = System.currentTimeMillis();
		ArrayList<SteamWorker> userJobs = new ArrayList<SteamWorker>();
		ArrayList<SteamWorker> friendJobs = new ArrayList<SteamWorker>();
		try {
			List<SteamGame> userGames = getSteamGames(user, profile, results);
			LinkedList<SteamFriend> userFriends = getSteamFriendsLinked(user, profile, results);
			final LinkedList<SteamUser> userUsers = new LinkedList<SteamUser>();
			
			while(!userFriends.isEmpty()) {
				final SteamFriend fr = userFriends.pop();
				SteamWorker uR = new SteamWorker() {
					@Override
					public void personalizedRun() {
						try {
							Logger.info("%--- getSteamUser worker thread started ---%");
							SteamUser user = getSteamUser(fr.friendSteamId, true, results);
							userUsers.add(user);
							Logger.info("%--- getSteamUser worker thread add" +
									"ed " + user + " users is now of size " + 
									userUsers.size() + "---%");
							Logger.info("%--- getSteamUser worker thread ended ---%");
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				};
				userJobs.add(uR);
				SteamAPIExecutorService.getInstance().submit(uR);
			}
	
			// TODO: add this stuff to configuration
			int tries = 0;
			final int triesMax = 10*6000; 
			// see if job is finished until max number of tries
			while((jobsOpen(userJobs)) && (tries < triesMax)) {
				//Logger.info("%--- User job not done, sleeping for .5 seconds ---%");
				Thread.sleep(100);
				tries++;
			}
			if(tries == triesMax) {
				Logger.info("%--- User job not done, exiting... ---%");
				throw new SteamParserException("Friend job could not finish in allotted time.");
			}
			
			Logger.info("%--- Ended building users, user count is " + userUsers.size() + " ---%");
			
			final HashMap<String,SteamRecommendation> gameCount = fillGameCountMap(userGames);
			final List<SteamUser> completedFriends = new ArrayList<SteamUser>();
			while(!userUsers.isEmpty()) {
				Logger.info("%--- Building first worker thread. Users size is " + userUsers.size() + " ---%");
				final SteamUser friend = userUsers.pop();
				SteamWorker r = new SteamWorker() {
					@Override
					public void personalizedRun() {
						try {
							Logger.info("%--- getSteamGames worker thread started ---%");
							List<SteamGame> friendGames = getSteamGames(friend.steamId64, true, results);
							updateGameMap(gameCount, friendGames, friend);
							completedFriends.add(friend);
							Logger.info("%--- getSteamGames worker thread ended ---%");
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				};
				friendJobs.add(r);
				SteamAPIExecutorService.getInstance().submit(r);
			}
	
			// TODO: add this stuff to configuration
			// see if job is finished until max number of tries
			tries = 0;
			while((jobsOpen(friendJobs)) && (tries < triesMax)) {
				//Logger.info("%--- Friend job not done, sleeping for .5 seconds ---%");
				Thread.sleep(100);
				tries++;
			}
			if(tries == triesMax) {
				Logger.info("%--- Friend job not done, exiting... ---%");
				throw new SteamParserException("Friend job could not finish in allotted time.");
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
		} finally {
			for(Thread runningThread : userJobs) {
				runningThread.interrupt();
			}
			for(Thread runningThread : friendJobs) {
				runningThread.interrupt();
			}
		}
		long stopTime = System.currentTimeMillis();
		Logger.info("%--- Execution was " + (startTime - stopTime) + " ---%");
		results.addMessage((stopTime-startTime)/1000 + " seconds.");
		return result;
	}
	
}