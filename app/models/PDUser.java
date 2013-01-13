package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Email;
import play.db.ebean.Model;
import controllers.LoginUser;

@Entity
public class PDUser extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5604961768770202406L;

	@Id
	@Constraints.Required
	public String name;
	
	@Constraints.Required
	@Email
	public String email;
	
	@Constraints.Required
	public String passwd;
	
	public String steamId;
	
	public String steamLongId;

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
