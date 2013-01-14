package playdate.common.steam.parser;

import java.util.List;

import models.PDUser;
import models.SteamGame;

public class SteamComUtils {
	public static List<SteamGame> getSteamGames(String user) {
		List<SteamGame> result = null;
		PDUser pdUser = PDUser.getUser(user);
		SteamComXMLParser parser = new SteamComXMLParser(pdUser.steamId);
		try {
			result = parser.parseGameLibrary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
