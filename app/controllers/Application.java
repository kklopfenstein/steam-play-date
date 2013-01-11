package controllers;

import models.PDUser;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
	
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
			return ok(
						views.html.regSuccess.render(user.name, user.email)
					);
		}
	}
  
}