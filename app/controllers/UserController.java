package controllers;

import org.mindrot.jbcrypt.BCrypt;

import com.avaje.ebean.Ebean;

import models.PDUser;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * 
 * @author Kevin Klopfenstein
 *
 */
public class UserController extends Controller {
	
	/**
	 * 
	 * @return
	 */
	public static Result register() {
		return ok(views.html.reg.render(null,form(PDUser.class)));
	}
	
	/**
	 * 
	 * @return
	 */
	public static Result registerUser() {
		Form<PDUser> form = form(PDUser.class).bindFromRequest();
		if(form.hasErrors()) {
			return badRequest(views.html.reg.render(
					"Oops! Something went wrong.", 
					form));
		}
		else {
			PDUser user = form.get();
			user.passwd = BCrypt.hashpw(user.passwd, BCrypt.gensalt(12));
			Ebean.save(user);
			return ok(
						views.html.regSuccess.render(user.name, user.email)
					);
		}
	}
}
