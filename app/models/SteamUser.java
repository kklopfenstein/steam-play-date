package models;

import javax.persistence.Id;

import play.db.ebean.Model;

public class SteamUser extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = -349459642300559513L;

	@Id
	public long id;
	
	public String steamId;
	public String steamId64;
	public String avatarMedium;
}
