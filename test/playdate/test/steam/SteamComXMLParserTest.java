package playdate.test.steam;

import java.util.ArrayList;

import models.SteamGame;

import org.junit.Test;

import playdate.common.steam.parser.SteamComXMLParser;

public class SteamComXMLParserTest {
	
	public static final String STEAM_ID = "humbajoe";
	
	@Test
	public void testParser() {
		try {
			SteamComXMLParser parser = new SteamComXMLParser(STEAM_ID);
			ArrayList<SteamGame> games = parser.parseGameLibrary();
			for(SteamGame game : games) {
				System.out.println("Found game: " + game.name);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
