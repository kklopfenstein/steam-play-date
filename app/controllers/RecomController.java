package controllers;

import java.util.List;

import models.SteamRecommendation;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import playdate.common.steam.parser.SteamComUtils;

public class RecomController extends Controller {
	
	public static Result index() {
		return ok(views.html.welcome.render(null, form(SteamIdForm.class)));
	}
	
	/**
	 * 
	 * @return
	 */
	public static Result getRecommendations() {
		Form<SteamIdForm> form = form(SteamIdForm.class).bindFromRequest();
		
		if(form.hasErrors()) {
			return badRequest(views.html.welcome.render(
					"Invalid",form));
		}
		SteamIdForm steamIdForm = form.get();
		
		List<SteamRecommendation> recoms = SteamComUtils.getRecommendations(steamIdForm.steamId, false, 3);
		//Logger.info("User: " + user);
		Logger.info(recoms.toString());
		Logger.info("Number of recommendations: " + recoms.size());
		return ok(views.html.recom.render(null, recoms));
	}
}
