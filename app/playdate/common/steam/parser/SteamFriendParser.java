package playdate.common.steam.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import models.SteamFriend;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import play.Logger;
import playdate.common.steam.parser.exception.SteamParserException;
import playdate.common.steam.parser.validation.ValidationResults;
import playdate.common.util.Constants;

public class SteamFriendParser extends SteamComXMLParser {
	private ArrayList<SteamFriend> friends = new ArrayList<SteamFriend>();
	
	// steam constants for parsing
	private static final String FRIEND = "friend";
	
	private SteamFriend friend;
	
	protected SteamFriendParser(String steamId, Boolean profile) {
		super(steamId, profile);
	}
	
	public ArrayList<SteamFriend> parseSteamFriends(int triesMax, ValidationResults results) throws SteamParserException {
		StringBuffer steamURL = new StringBuffer();
		if(profile) {
			steamURL.append(Constants.STEAM_PROFILE_COM_URL);
		}
		else {
			steamURL.append(Constants.STEAM_COM_URL);
		}
		steamURL.append(this.steamId);
		steamURL.append(Constants.STEAM_COM_FRIEND_XML);
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
					Logger.info("Refreshing cache!");
					source = new InputSource(SteamCache.getRefreshRequestStream(
							steamURL.toString()));
				}
				steamReader.parse(source);
				successfull = true;
			}
			catch(SAXException e) {
				Logger.error("SAXException in method parseSteamFriends of " +
						"SteamFriendParser");
				Logger.info("Steam URL was: " + steamURL.toString());
				//e.printStackTrace();
			}
			catch(MalformedURLException e) {
				Logger.error("MalformedURLException in method parseSteamFriends of " +
						"SteamFriendParser");
				Logger.info("Steam URL was: " + steamURL.toString());
				throw new SteamParserException(e);
			}
			catch(IOException e) {
				Logger.error("IOException in method parseSteamFriends of " +
						"SteamFriendParser");
				Logger.info("Steam URL was: " + steamURL.toString());
				//e.printStackTrace();
			} finally {
				tries++;
			}
		}
		
		if(!successfull) {
			results.addMessage("Could not parse friends for " + steamId);
			throw new SteamParserException("Exception after " + tries + " tries.");
		}
		
		return friends;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase(FRIEND)) {
			if(friend != null) {
				friends.add(friend);
			}
			friend = new SteamFriend();
			Logger.info("New friend encountered.");
		}
	}
	
	@Override
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if(qName.equalsIgnoreCase(FRIEND)) {
			friend.friendSteamId = tempVal;
		}
	}
}
