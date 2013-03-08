package controllers;

import java.util.ArrayList;
import java.util.List;

import models.PlayDate;
import models.SteamGame;
import models.SteamUser;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import playdate.common.steam.parser.SteamComUtils;
import playdate.common.steam.parser.exception.SteamParserException;
import playdate.common.steam.parser.validation.ValidationResults;
import playdate.common.util.Constants;

import com.avaje.ebean.Ebean;

/**
 * 
 * @author Kevin Klopfenstein
 *
 */
public class PlayDateController extends Controller {
	
	/**
	 * 
	 * @return
	 */
	public static Result newPlayDate() {
		String user = session("user");
		
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		//List<SteamGame> games = SteamGame.getGames(user);
		List<SteamGame> games = null;
		List<SteamUser> users = null;
		ValidationResults results = new ValidationResults();
		try {
			games = SteamComUtils.getSteamGamesForUser(user, 
				false, results);
			users = SteamComUtils.getSteamUsersForUser(user, false, results);
		} catch(SteamParserException e) {
			e.printStackTrace();
		}
		List<String> gm = new ArrayList<String>();
		for(SteamGame game : games) {
			Logger.info("Game found for " + user + ".");
			gm.add(game.name);
		}
		return ok(views.html.playdate.render(null,form(PlayDate.class), gm, 
				Constants.ADD, users));
	}
	
	/**
	 * 
	 * @return
	 */
	public static Result createPlayDate() {
		
		String user = session("user");
		
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		List<SteamGame> games = null;
		List<SteamUser> users = null;
		ValidationResults results = new ValidationResults();
		try {
			games = SteamComUtils.getSteamGamesForUser(user, 
					false, results);
			users = SteamComUtils.getSteamUsersForUser(user, false, results);
		} catch(SteamParserException e) {
			e.printStackTrace();
		}
		List<String> gm = new ArrayList<String>();
		for(SteamGame game : games) {
			Logger.info("Game found for " + user + ".");
			gm.add(game.name);
		}
		
		Form<PlayDate> form = form(PlayDate.class).bindFromRequest();
		if(form.hasErrors()) {
			return badRequest(views.html.playdate.render(
					"Oops! Something went wrong.", 
					form, gm, Constants.ADD, users));
		}
		else {
			PlayDate playDate = form.get();
			playDate.user = user;
			Ebean.save(playDate);
			return redirect("/");
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Result removePlayDate(Long id) {
		String user = session("user");
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		PlayDate.removePlayDate(user, id);

		return redirect("/");
 	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Result editPlayDate(Long id) {
		String user = session("user");
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		List<SteamGame> games = null;
		List<SteamUser> users = null;
		ValidationResults results = new ValidationResults();
		try {
			games = SteamComUtils.getSteamGamesForUser(user, 
					false, results);
			users = SteamComUtils.getSteamUsersForUser(user, false, results);
		} catch(SteamParserException e) {
			e.printStackTrace();
		}
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

		return ok(views.html.playdate.render(null,form(PlayDate.class).
				fill(playDate), gm, Constants.EDIT, users));
 	}
	
	/**
	 * 
	 * @return
	 */
	public static Result doEditPlayDate() {
		String user = session("user");
		if(user == null || user.isEmpty()) {
			return redirect("/");
		}
		
		List<SteamGame> games = null;
		List<SteamUser> users = null;
		ValidationResults results = new ValidationResults();
		try {
			games = SteamComUtils.getSteamGamesForUser(user, 
					false, results);
			users = SteamComUtils.getSteamUsersForUser(user, false, results);
		} catch(SteamParserException e) {
			e.printStackTrace();
		}
		List<String> gm = new ArrayList<String>();
		for(SteamGame g : games) {
			Logger.info("Game found for " + user + ".");
			gm.add(g.name);
		}
		
		Form<PlayDate> form = form(PlayDate.class).bindFromRequest();
		
		if(form.hasErrors()) {
			return badRequest(views.html.playdate.render(
					"Oops! Something went wrong.", 
					form, gm, Constants.EDIT, users));
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
	
}
