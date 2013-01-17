package playdate.common.steam.parser;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SteamComXMLParser extends DefaultHandler {
	protected final String steamId;
	
	protected String tempVal;
	protected boolean profile = false;
	
	protected SteamComXMLParser(String steamId, Boolean profile) {
		this.steamId = steamId;
		this.profile = profile;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) 
			throws SAXException {
		tempVal = new String(ch, start, length);
	}
}
