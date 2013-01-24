package playdate.common.steam.parser.exception;

public class SteamParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4401423807584839268L;
	
	
	public SteamParserException() {
		super();
	}
	
	public SteamParserException(String msg) {
		super(msg);
	}
	
	public SteamParserException(Exception e) {
		super(e);
	}

}
