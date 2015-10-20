package KateAndrewService;

import matchstatistic.MatchType;
import matchstatistic.sportstatistictypes.StatisticType;

/**
 * @author Andrey
 * Class for updating statistics on desktop clients from a Service 
 */
public class MatchStatisticsDelta {
	
	private long matchid;
	private MatchType sportType;
	private StatisticType statistic;
	private long newValue;
	
	public MatchStatisticsDelta(long matchid, MatchType sportType, StatisticType statistic, long newValue) {
		this.matchid = matchid;
		this.sportType = sportType;
		this.statistic = statistic;
		this.newValue = newValue;
	}
	
	public void setMatchid(long matchid) {
		this.matchid = matchid;
	}
	
	public long getMatchid() {
		return matchid;
	}
	
	public void setSportType(MatchType sportType) {
		this.sportType = sportType;
	}
	
	public MatchType getSportType() {
		return sportType;
	}
	
	public void setStatistic(StatisticType statistic) {
		this.statistic = statistic;
	}
	
	public StatisticType getStatistic() {
		return statistic;
	}
	
	public void setnewValue(long newValue) {
		this.newValue = newValue;
	}
	
	public long getnewValue() {
		return newValue;
	}
	
	/**
	 * Overriding method for gson library (Object -> json, json -> Object)
	 */
	@Override
	public String toString() {
		return "DataObject [matchid=" + matchid + ", sportType=" + sportType + ", statistic=" + statistic + ", newValue=" + newValue + "]";
	}
}
