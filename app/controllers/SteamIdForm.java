package controllers;

import play.data.validation.Constraints;

public class SteamIdForm {
	@Constraints.Required
	public String steamId;
}
