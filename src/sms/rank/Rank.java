package sms.rank;

import java.io.Serializable;

public class Rank implements Serializable{
	private int rank_id;
	private String rank_name;
	public int getRank_id() {
		return rank_id;
	}
	public void setRank_id(int rankId) {
		rank_id = rankId;
	}
	public String getRank_name() {
		return rank_name;
	}
	public void setRank_name(String rankName) {
		rank_name = rankName;
	}
	
}
