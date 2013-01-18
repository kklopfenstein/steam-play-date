package playdate.common.steam.parser;

import java.io.IOException;
import java.net.MalformedURLException;

import models.SteamUser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import play.Logger;
import playdate.common.util.Constants;

public class SteamUserParser extends SteamComXMLParser {
	
	// steam constants for parsing
	private static final String STEAM_ID_64 = "steamID64";
	private static final String STEAM_ID = "steamID";
	private static final String AVATAR_MEDIUM = "avatarMedium";
	private static final String PROFILE = "profile";
	
	private SteamUser steamUser;
	
	protected SteamUserParser(String steamId, Boolean profile) {
		super(steamId, profile);
	}
	
	public SteamUser parseSteamFriends() throws Exception {
		StringBuffer steamURL = new StringBuffer();
		if(profile) {
			steamURL.append(Constants.STEAM_PROFILE_COM_URL);
		}
		else {
			steamURL.append(Constants.STEAM_COM_URL);
		}
		steamURL.append(this.steamId);
		steamURL.append(Constants.STEAM_COM_USER_XML);
		
		boolean successfull = false;
		while(!successfull) {
			try {
				XMLReader steamReader = XMLReaderFactory.createXMLReader();
				steamReader.setContentHandler(this);
				/*steamReader.parse(new InputSource(new URL(steamURL.toString())
					.openStream()));*/
				steamReader.parse(new InputSource(SteamCache.getRequestStream(
						steamURL.toString())));
				successfull = true;
			}
			catch(SAXException e) {
				Logger.error("SAXException in method parseGameLibrary of " +
						"SteamComXMLParser");
				e.printStackTrace();
			}
			catch(MalformedURLException e) {
				throw new Exception(e);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		return steamUser;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase(PROFILE)) {
			steamUser = new SteamUser();
			Logger.info("New friend encountered.");
		}
	}
	
	@Override
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if(qName.equalsIgnoreCase(STEAM_ID_64)) {
			steamUser.steamId64 = tempVal;
		}
		else if(qName.equalsIgnoreCase(STEAM_ID)) {
			steamUser.steamId = tempVal;
		}
		else if(qName.equalsIgnoreCase(AVATAR_MEDIUM)) {
			steamUser.avatarMedium = tempVal;
		}
	}
}
