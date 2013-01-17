package controllers;

import java.util.ArrayList;
import java.util.List;

import models.PDUser;
import models.PlayDate;
import models.SteamGame;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import playdate.common.steam.parser.SteamComUtils;
import playdate.common.steam.parser.recommendation.SteamRecommendation;
import playdate.common.util.Constants;
import views.html.index;

import com.avaje.ebean.Ebean;

public class Application extends Controller {
	
    public static Result register() {
    	return ok(views.html.reg.render(null,form(PDUser.class)));
    }
    
	public static Result index() {
		String user = session("user");
		
		List<PlayDate> dates = null;
		if(user != null && (!user.isEmpty())) {
			dates = PlayDate.getPlayDates(user);
		}
		
		return ok(index.render("test", user, dates));
	}
	
	public static Result registerUser() {
		Form<PDUser> form = form(PDUser.class).bindFromRequest();
		if(form.hasErrors()) {
			return badRequest(views.html.reg.render(
					"Oops! Something went wrong.", 
					form));
		}
		else {
			PDUser user = form.get();
			//boolean parseSuccessful = false;
			//int attempts = 0;
			/*SteamComXMLParser parser = new SteamComXMLParser(user.steamId);
			while(!parseSuccessful && attempts < 4) {
				try {
					ArrayList<SteamGame> games = parser.parseGameLibrary();
					for(SteamGame game : games) {
						game.user = user.name;
						Ebean.save(game);
					}
					parseSuccessful = true;
				} catch(Exception e) {
					Logger.error("Error parsing steam profile for user " 
							+ user.steamId);
					Logger.error("Exception was " + e.getMessage());
					e.printStackTrace();
				}
				attempts++;
				if(!parseSuccessful) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}*/
			Ebean.save(user);
			return ok(
						views.html.regSuccess.render(user.name, user.email)
					);
		}
	}
	
	public static Result startLogin() {
		return ok(views.html.login.render(null,form(LoginUser.class)));
	}
	
	public static Result login() {
		Form<LoginUser> form = form(LoginUser.class).bindFromRequest();
		if(form.hasErrors()) {
			return badRequest(views.html.login.render(
					"Oops! Something went wrong.", 
					form));
		}
		else {
			LoginUser user = form.get();
			PDUser pdUser = PDUser.isValidLoginUser(user);
			if(pdUser != null) {
				session("user", pdUser.name);
			}
			else {
				return badRequest(views.html.login.render(
						"Invalid username or password.", 
						form(LoginUser.class)));
			}
			return redirect("/");
		}
	}
	
	public static Result logout() {
		session().clear();
		return redirect("/");
	}
  
	public static Result newPlayDate() {
		String user = session("user");
		
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		//List<SteamGame> games = SteamGame.getGames(user);
		List<SteamGame> games = SteamComUtils.getSteamGamesForUser(user, false);
		List<String> gm = new ArrayList<String>();
		for(SteamGame game : games) {
			Logger.info("Game found for " + user + ".");
			gm.add(game.name);
		}
		return ok(views.html.playdate.render(null,form(PlayDate.class), gm, Constants.ADD));
	}
	
	public static Result createPlayDate() {
		
		String user = session("user");
		
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		List<SteamGame> games = SteamComUtils.getSteamGamesForUser(user, false);
		List<String> gm = new ArrayList<String>();
		for(SteamGame game : games) {
			Logger.info("Game found for " + user + ".");
			gm.add(game.name);
		}
		
		Form<PlayDate> form = form(PlayDate.class).bindFromRequest();
		if(form.hasErrors()) {
			return badRequest(views.html.playdate.render(
					"Oops! Something went wrong.", 
					form, gm, Constants.ADD));
		}
		else {
			PlayDate playDate = form.get();
			playDate.user = user;
			Ebean.save(playDate);
			return redirect("/");
		}
	}
	
	public static Result removePlayDate(Long id) {
		String user = session("user");
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		PlayDate.removePlayDate(user, id);

		return redirect("/");
 	}
	
	public static Result editPlayDate(Long id) {
		String user = session("user");
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		List<SteamGame> games = SteamComUtils.getSteamGamesForUser(user, false);
		List<String> gm = new ArrayList<String>();
		for(SteamGame g : games) {
			Logger.info("Game found for " + user + ".");
			gm.add(g.name);
		}
		
		PlayDate playDate = null;
		List<PlayDate> result = PlayDate.getPlayDates(user, id);
		if(result != null && result.size() > 0) {
			playDate = result.get(0);
		}

		return ok(views.html.playdate.render(null,form(PlayDate.class).fill(playDate), gm, Constants.EDIT));
 	}
	
	public static Result doEditPlayDate() {
		String user = session("user");
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		List<SteamGame> games = SteamComUtils.getSteamGamesForUser(user, false);
		List<String> gm = new ArrayList<String>();
		for(SteamGame g : games) {
			Logger.info("Game found for " + user + ".");
			gm.add(g.name);
		}
		
		Form<PlayDate> form = form(PlayDate.class).bindFromRequest();
		
		if(form.hasErrors()) {
			return badRequest(views.html.playdate.render(
					"Oops! Something went wrong.", 
					form, gm, Constants.EDIT));
		}
		else {
			PlayDate pd = form.get();
			List<PlayDate> pds = PlayDate.getPlayDates(user, pd.id);
			PlayDate pdUpd = pds.get(0);
			pdUpd.date = pd.date;
			pdUpd.game = pd.game;
			pdUpd.time = pd.time;
			pdUpd.update();
			return redirect("/");
		}
	}
	
	public static Result getRecommendations() {
		String user = session("user");
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		List<SteamRecommendation> recoms = SteamComUtils.getRecommendations(user, false);
		return ok(recom.render(user, recoms));
	}
	
}