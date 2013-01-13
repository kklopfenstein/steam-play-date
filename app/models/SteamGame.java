package models;

import javax.persistence.Id;

import play.db.ebean.Model;

public class SteamGame extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7997386408485238136L;

	@Id
	public String name;
	
	@Id
	public String user;
	
	public String playTime;
	public Long appId;
	public String storeLink;
	public String logo;
	public String playTwoWeeks;
}
