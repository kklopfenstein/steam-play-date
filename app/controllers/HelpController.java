package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class HelpController extends Controller {
	public static Result index() {
		return ok(views.html.help.render());
	}
}
