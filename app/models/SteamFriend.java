package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class SteamFriend extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7443236582265958010L;

	@Id
	public Long id;
	
	public String user;
	public String friendSteamId;
	@Override
	public String toString() {
		return "SteamFriend [id=" + id + ", user=" + user + ", friendSteamId="
				+ friendSteamId + "]";
	}
	
}
