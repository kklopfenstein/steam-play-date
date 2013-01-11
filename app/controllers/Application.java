package controllers;

import com.avaje.ebean.Ebean;

import models.PDUser;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
	
	public class LoginForm {
		@Required
		public String name;
		@Required
		public String passwd;
	}
	
    public static Result register() {
    	return ok(views.html.reg.render(null,form(PDUser.class)));
    }
    
	public static Result index() {
		return ok(index.render("test"));
	}
	
	public static Result registerUser() {
		Form<PDUser> form = form(PDUser.class).bindFromRequest();
		if(form.hasErrors()) {
			return badRequest(views.html.reg.render("Form was incorrect.", 
				form(PDUser.class)));
		}
		else {
			PDUser user = form.get();
			Ebean.save(user);
			return ok(
						views.html.regSuccess.render(user.name, user.email)
					);
		}
	}
	
	public static Result login() {
		Form<LoginForm> form = form(LoginForm.class).bindFromRequest();
		if(form.hasErrors()) {
			return badRequest(views.html.login.render("Form was incorrect.", 
				form(LoginForm.class)));
		}
		else {
			LoginForm user = form.get();
			Ebean.save(user);
			return redirect("/");
		}
	}
  
}