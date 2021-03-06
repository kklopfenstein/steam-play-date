package playdate.common.steam.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import models.SteamGame;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import play.Logger;
import playdate.common.steam.parser.exception.SteamParserException;
import playdate.common.steam.parser.validation.ValidationResults;
import playdate.common.util.Constants;

public class SteamUserGameParser extends SteamComXMLParser {
	private ArrayList<SteamGame> games = new ArrayList<SteamGame>();
	
	// steam constants for parsing
	private static final String GAME = "game";
	private static final String APP_ID = "appID";
	private static final String NAME = "name";
	private static final String LOGO = "logo";
	private static final String STORE_LINK = "storeLink";
	private static final String HOURS_LAST_TWO_WEEKS = "hoursLast2Weeks";
	private static final String HOURS_ON_RECORD = "hoursOnRecord";
	
	private SteamGame steamGame;
	protected SteamUserGameParser(String steamId, Boolean profile) {
		super(steamId, profile);
	}
	
	public ArrayList<SteamGame> parseGameLibrary(int triesMax, ValidationResults results) throws SteamParserException {
		StringBuffer steamURL = new StringBuffer();
		if(profile) {
			steamURL.append(Constants.STEAM_PROFILE_COM_URL);
		}
		else {
			steamURL.append(Constants.STEAM_COM_URL);
		}
		steamURL.append(this.steamId);
		steamURL.append(Constants.STEAM_COM_URL_XML);
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
				Logger.error("SAXException in method parseGameLibrary of " +
						"SteamUserGameParser");
				Logger.info("Steam URL was: " + steamURL.toString());
				//e.printStackTrace();
			}
			catch(MalformedURLException e) {
				Logger.error("MalformedURLException in method parseGameLibrary of " +
						"SteamUserGameParser");
				Logger.info("Steam URL was: " + steamURL.toString());
				throw new SteamParserException(e);
			}
			catch(IOException e) {
				Logger.error("IOException in method parseGameLibrary of " +
						"SteamUserGameParser");
				Logger.info("Steam URL was: " + steamURL.toString());
				//e.printStackTrace();
			} finally {
				tries++;
			}
		}
		
		if(!successfull) {
			results.addMessage("Could not parse games for " + steamId);
			throw new SteamParserException("Exception after " + tries + " tries.");
		}
		
		return games;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase(GAME)) {
			if(steamGame != null) {
				games.add(steamGame);
			}
			steamGame = new SteamGame();
			Logger.info("New game encountered.");
		}
	}
	
	@Override
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if(qName.equalsIgnoreCase(APP_ID)) {
			steamGame.appId = Long.valueOf(tempVal);
		}
		else if(qName.equalsIgnoreCase(NAME)) {
			steamGame.name = tempVal;
		}
		else if(qName.equalsIgnoreCase(STORE_LINK)) {
			steamGame.storeLink = tempVal;
		}
		else if(qName.equalsIgnoreCase(LOGO)) {
			steamGame.logo = tempVal;
		}
		else if(qName.equalsIgnoreCase(HOURS_LAST_TWO_WEEKS)) {
			steamGame.playTwoWeeks = tempVal;
		}
		else if(qName.equalsIgnoreCase(HOURS_ON_RECORD)) {
			steamGame.playTime = tempVal;
		}
	}
}
