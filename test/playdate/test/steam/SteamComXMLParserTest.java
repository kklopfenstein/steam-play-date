package playdate.test.steam;

import java.util.List;

import models.SteamGame;
import models.SteamRecommendation;

import org.junit.Test;

import playdate.common.steam.parser.SteamComUtils;
import playdate.common.steam.parser.validation.ValidationResults;

public class SteamComXMLParserTest {
	
	public static final String STEAM_ID = "humbajoe";
	
	@Test
	public void testGameParser() {
		try {
			ValidationResults results = new ValidationResults();
			List<SteamGame> games = SteamComUtils.getSteamGames(STEAM_ID, false, results);
			assert(games.size() > 0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void xtestRecommendations() {
		try {
			ValidationResults results = new ValidationResults();
			List<SteamRecommendation> recoms = SteamComUtils.getRecommendations(STEAM_ID, false, 10, results);
			for(SteamRecommendation recom : recoms) {
				System.out.println(recom);
			}
			assert(recoms.size() > 0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRecommendations() {
		try {
			ValidationResults results = new ValidationResults();
			List<SteamRecommendation> recoms = SteamComUtils.getRecommendationsAsynch(STEAM_ID, false, 10, results);
			for(SteamRecommendation recom : recoms) {
				System.out.println(recom);
			}
			assert(recoms.size() > 0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
