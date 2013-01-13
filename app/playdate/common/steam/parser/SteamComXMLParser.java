package playdate.common.steam.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import models.SteamGame;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import playdate.common.util.Constants;

public class SteamComXMLParser extends DefaultHandler {
	private final String steamId;
	private ArrayList<SteamGame> games = new ArrayList<SteamGame>();
	private static final String GAME = "game";
	
	private SteamGame steamGame;
	
	public SteamComXMLParser(String steamId) {
		this.steamId = steamId;
	}
	
	public void parseGameLibrary() {
		StringBuffer steamURL = new StringBuffer(Constants.STEAM_COM_URL);
		steamURL.append(this.steamId);
		steamURL.append(Constants.STEAM_COM_URL_XML);
		
		try {
			XMLReader steamReader = XMLReaderFactory.createXMLReader();
			steamReader.setContentHandler(this);
			steamReader.parse(new InputSource(new URL(steamURL.toString()).openStream()));
		}
		catch(SAXException e) {
			
		}
		catch(MalformedURLException e) {
			
		}
		catch(IOException e) {
			
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase(GAME)) {
			steamGame = new SteamGame();
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) 
			throws SAXException {
		
	}
}
