package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
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
	
	public static Finder<String, SteamGame> find = new Finder<String, SteamGame>(
			String.class, SteamGame.class);
	
	public static List<SteamGame> getGames(String user) {
		List<SteamGame> games = find.where()
				.eq("name", user).findList();
		return games;
	}
}
