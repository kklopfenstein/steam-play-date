package controllers;

import java.util.List;

import models.SteamRecommendation;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import playdate.common.steam.parser.SteamComUtils;
import playdate.common.steam.parser.exception.SteamParserException;
import playdate.common.steam.parser.validation.ValidationResults;

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
		List<SteamRecommendation> recoms = null;
		ValidationResults results = new ValidationResults();
		try {
			recoms = SteamComUtils.getRecommendationsAsynch(
								steamIdForm.steamId, 
								SteamComUtils.isProfile(steamIdForm.steamId),
								3,
								results
							);
		} catch(SteamParserException e) {
			e.printStackTrace();
			logMessages(results);
			return ok(views.html.recom.render(null, results.getMessages(), null));
		}
		//Logger.info("User: " + user);
		if(recoms != null) {
			Logger.info(recoms.toString());
			Logger.info("Number of recommendations: " + recoms.size());
		}
		logMessages(results);
		return ok(views.html.recom.render(null, results.getMessages(), recoms));
	}
	
	private static void logMessages(ValidationResults results) {
		for(String msg : results.getMessages()) {
			Logger.info("MESSAGE: " + msg);
		}
	}
}
