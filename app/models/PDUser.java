package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class PDUser extends Model {
	@Id
	@Constraints.Required
	public String name;
	
	@Constraints.Required
	public String email;
	
	@Constraints.Required
	public String passwd;

}
