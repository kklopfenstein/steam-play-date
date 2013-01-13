package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class PlayDate extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5532228083167047189L;

	@Id
	@Constraints.Required
	public String user;
	
	@Id
	@Constraints.Required
	public Date date;
	
	@Id
	@Constraints.Required
	public String time;
	
	@Constraints.Required
	public String game;
	
	public static Finder<String, PlayDate> find = new Finder<String, PlayDate>(
			String.class, PlayDate.class);
	
	public static List<PlayDate> getPlayDates(String user) {
		List<PlayDate> dates = find.where()
				.eq("user", user).findList();
		return dates;
	}
}
