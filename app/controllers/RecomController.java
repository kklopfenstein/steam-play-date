package controllers;

import java.util.List;

import models.SteamRecommendation;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import playdate.common.steam.parser.SteamComUtils;

public class RecomController extends Controller {
	
	public class SteamIdForm {
		@Constraints.Required
		public String steamId;
	}
	
	public static Result index() {
		return ok(views.html.welcome.render());
	}
	
	/**
	 * 
	 * @return
	 */
	public static Result getRecommendations() {
		String user = session("user");
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		Form<SteamIdForm> form = form(SteamIdForm.class).bindFromRequest();
		
		if(form.hasErrors()) {
			return badRequest(views.html.index.render(
					null,null,null));
		}
		SteamIdForm steamIdForm = form.get();
		
		List<SteamRecommendation> recoms = SteamComUtils.getRecommendations(steamIdForm.steamId, false, 3);
		Logger.info("User: " + user);
		Logger.info(recoms.toString());
		Logger.info("Number of recommendations: " + recoms.size());
		return ok(views.html.recom.render(user, recoms));
	}
}
