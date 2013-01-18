package models;

import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

public class PlayDateInvite extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1965028190278633999L;

	@Id
	Long id;
	
	@Required
	Long playDateId;
	
	@Required
	String steamId;
}
