package controllers;

import play.data.Form;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
	
	/**
     * Describes the hello form.
     */
    public static class User {
        @Required public String name;
        @Required public String email;
        @Required public String passwd;
        @Required public String passwdCfrm;
    }
    
    public static Result register() {
    	return ok(views.html.reg.render(null,form(User.class)));
    }
    
	public static Result index() {
		return ok(index.render("test"));
	}
	
	public static Result registerUser() {
		Form<User> form = form(User.class).bindFromRequest();
		if(form.hasErrors()) {
			return badRequest(views.html.reg.render("Form was incorrect.", 
				form(User.class)));
		}
		else {
			User user = form.get();
			return ok(
						views.html.regSuccess.render(user.name, user.email)
					);
		}
	}
  
}