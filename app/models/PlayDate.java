package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class PlayDate extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5532228083167047189L;

	@Id
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
	
	public static List<PlayDate> getPlayDates(String user, String date, String time, String game) {
		List<PlayDate> dates = null;
		try {
			DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
			Date dt =  df.parse(date);  
			
			dates = find.where()
					.eq("user", user).eq("date", dt).eq("time", time)
					.eq("game", game).findList();
		} catch(ParseException e) {
			Logger.error("Parse exception in method getPlayDates of PlayDate");
		}
		return dates;
	}
	
	public static boolean removePlayDate(String user, String date, String time, String game) {
		boolean result = false;
		List<PlayDate> dates = getPlayDates(user, date, time, game);
		dates.get(0).delete();
		return result;
	}
}
