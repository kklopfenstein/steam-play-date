package models;

import java.util.Date;

import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

public class PlayDate extends Model {
	@Id
	public Long id;
	
	@Constraints.Required
	public Date date;
}
