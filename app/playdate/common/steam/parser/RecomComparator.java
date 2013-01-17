package playdate.common.steam.parser;

import java.util.Comparator;

import models.SteamRecommendation;


public class RecomComparator implements Comparator<SteamRecommendation> {

	@Override
	public int compare(SteamRecommendation o1, SteamRecommendation o2) {
		int o1Size = 0;
		int o2Size = 0;
		if(o1.getFriends() != null) {
			o1Size = o1.getFriends().size();
		}
		if(o2.getFriends() != null) {
			o2Size = o2.getFriends().size();
		}
		return new Integer(o2Size).
				compareTo(o1Size);
	}

}
