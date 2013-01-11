package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import com.avaje.ebeaninternal.util.DefaultExpressionList;

import controllers.LoginUser;

@Entity
public class PDUser extends Model {
	@Id
	@Constraints.Required
	public String name;
	
	@Constraints.Required
	public String email;
	
	@Constraints.Required
	public String passwd;

	public static Finder<String, PDUser> find = new Finder<String, PDUser>(
			String.class, PDUser.class);
	
	public static PDUser isValidLoginUser(LoginUser user) {
		PDUser result = null;
		List<PDUser> users = find.where()
				.eq("name", user.name).eq("passwd", user.passwd).findList();
		if(users != null && users.size() > 0) {
			result = users.get(0);
		}
		return result;
	}
}
