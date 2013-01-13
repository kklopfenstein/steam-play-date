package models;

import java.util.Date;

import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

public class PlayDate extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5532228083167047189L;

	@Id
	public Long id;
	
	@Constraints.Required
	public Date date;
	
	@Constraints.Required
	public String time;
	
	@Constraints.Required
	public String game;
}
