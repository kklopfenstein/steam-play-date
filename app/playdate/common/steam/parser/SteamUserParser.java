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
import playdate.common.steam.parser.exception.SteamParserException;
import playdate.common.steam.parser.validation.ValidationResults;
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
	
	public SteamUser parseSteamFriends(int triesMax, ValidationResults results) throws SteamParserException {
		StringBuffer steamURL = new StringBuffer();
		if(profile) {
			steamURL.append(Constants.STEAM_PROFILE_COM_URL);
		}
		else {
			steamURL.append(Constants.STEAM_COM_URL);
		}
		steamURL.append(this.steamId);
		steamURL.append(Constants.STEAM_COM_USER_XML);
		Logger.info(steamURL.toString());
		boolean successfull = false;
		int tries = 0;
		while(!successfull && tries < triesMax) {
			try {
				XMLReader steamReader = XMLReaderFactory.createXMLReader();
				steamReader.setContentHandler(this);
				InputSource source = null;
				if(tries == 0) {
					source = new InputSource(SteamCache.getRequestStream(
						steamURL.toString()));
				}
				else {
					source = new InputSource(SteamCache.getRefreshRequestStream(
							steamURL.toString()));
				}
				steamReader.parse(source);
				successfull = true;
			}
			catch(SAXException e) {
				Logger.error("SAXException in method parseSteamFriends of " +
						"SteamUserParser");
				Logger.info("Steam URL was: " + steamURL.toString());
				//e.printStackTrace();
			}
			catch(MalformedURLException e) {
				Logger.error("MalformedURLException in method parseSteamFriends of " +
						"SteamUserParser");
				Logger.info("Steam URL was: " + steamURL.toString());
				throw new SteamParserException(e);
			}
			catch(IOException e) {
				Logger.error("SAXException in method parseSteamFriends of " +
						"SteamUserParser");
				Logger.info("Steam URL was: " + steamURL.toString());
				//e.printStackTrace();
			} finally {
				tries++;
			}
		}
		
		if(!successfull) {
			results.addMessage("Could not resolve user " + steamId);
			throw new SteamParserException("Exception after " + tries + " tries.");
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
