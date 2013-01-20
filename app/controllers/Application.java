package controllers;

import java.util.List;

import models.PDUser;
import models.PlayDate;

import org.mindrot.jbcrypt.BCrypt;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * 
 * @author Kevin Klopfenstein
 *
 */
public class Application extends Controller {
	
	/**
	 * 
	 * @return
	 */
	public static Result index() {
		String user = session("user");
		
		List<PlayDate> dates = null;
		if(user != null && (!user.isEmpty())) {
			dates = PlayDate.getPlayDates(user);
		}
		
		return ok(index.render("test", user, dates));
	}
	
	/**
	 * 
	 * @return
	 */
	public static Result startLogin() {
		return ok(views.html.login.render(null,form(LoginUser.class)));
	}
	
	/**
	 * 
	 * @return
	 */
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
			if(pdUser != null && BCrypt.checkpw(user.passwd, pdUser.passwd)) {
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
	
	/**
	 * 
	 * @return
	 */
	public static Result logout() {
		session().clear();
		return redirect("/");
	}
	
}