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
import playdate.common.util.Constants;

public class SteamFriendParser extends SteamComXMLParser {
	private ArrayList<SteamFriend> friends = new ArrayList<SteamFriend>();
	
	// steam constants for parsing
	private static final String FRIEND = "friend";
	
	private SteamFriend friend;
	
	protected SteamFriendParser(String steamId, Boolean profile) {
		super(steamId, profile);
	}
	
	public ArrayList<SteamFriend> parseSteamFriends(int triesMax) throws SteamParserException {
		StringBuffer steamURL = new StringBuffer();
		if(profile) {
			steamURL.append(Constants.STEAM_PROFILE_COM_URL);
		}
		else {
			steamURL.append(Constants.STEAM_COM_URL);
		}
		steamURL.append(this.steamId);
		steamURL.append(Constants.STEAM_COM_FRIEND_XML);
		
		boolean successfull = false;
		int tries = 0;
		while(!successfull && tries < triesMax) {
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
				throw new SteamParserException(e);
			}
			catch(IOException e) {
				e.printStackTrace();
			} finally {
				tries++;
			}
		}
		
		if(!successfull) {
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
