package controllers;

import models.PDUser;
import models.PlayDate;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import com.avaje.ebean.Ebean;

public class Application extends Controller {
	
    public static Result register() {
    	return ok(views.html.reg.render(null,form(PDUser.class)));
    }
    
	public static Result index() {
		String user = session("user");
		return ok(index.render("test", user));
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
		return ok(views.html.playdate.render(null,form(PlayDate.class)));
	}
}