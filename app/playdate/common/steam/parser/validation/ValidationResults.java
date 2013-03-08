package playdate.common.steam.parser.validation;

import java.util.ArrayList;

/**
 * 
 * @author kevin
 *
 */
public class ValidationResults {
	private ArrayList<String> messages;
	/**
	 * 
	 */
	public ValidationResults() {
		messages = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void addMessage(String msg) {
		messages.add(msg);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getMessages() {
		return messages;
	}
}
