package controllers;

import play.data.validation.Constraints.Required;

/**
 * 
 * @author Kevin Klopfenstein
 *
 */
public class LoginUser {
	@Required
	public String name;
	@Required
	public String passwd;
}
