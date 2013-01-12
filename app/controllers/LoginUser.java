package controllers;

import play.data.validation.Constraints.Required;

public class LoginUser {
	@Required
	public String name;
	@Required
	public String passwd;
}
