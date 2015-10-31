package KateAndrewService;


import matchstatistic.MatchType;
import matchstatistic.Statistics;


/**
 * @author Andrey Class for updating statistics on desktop clients from a Service
 */
public class MatchStatisticsDelta
{
    private String team1Name;

    private String team2Name;

    private int matchid;

    private long timestamp;

    private MatchType sportType;

    private Statistics statistic;


    /**
     * That constructor takes info about some statistic
     * 
     * @param matchid
     * @param timestamp
     * @param sportType
     * @param statistic
     */
    public MatchStatisticsDelta(int matchid, long timestamp, MatchType sportType, Statistics statistic)
    {
        this.matchid = matchid;
        this.timestamp = timestamp;
        this.sportType = sportType;
        this.statistic = statistic;
    }


    /**
     * That constructor takes info about team1id and team2id
     * 
     * @param matchid
     * @param timestamp
     * @param sportType
     * @param team1Name
     * @param team2Name
     */
    public MatchStatisticsDelta(int matchid, long timestamp, MatchType sportType, String team1Name, String team2Name)
    {
        this.matchid = matchid;
        this.timestamp = timestamp;
        this.sportType = sportType;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
    }


    public void setMatchid(int matchid)
    {
        this.matchid = matchid;
    }


    public int getMatchid()
    {
        return matchid;
    }


    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }


    public long getTimestamp()
    {
        return timestamp;
    }


    public String getTeam1Name()
    {
        return team1Name;
    }


    public void setTeam1Name(String team1Name)
    {
        this.team1Name = team1Name;
    }


    public String getTeam2Name()
    {
        return team2Name;
    }


    public void setTeam2Name(String team2Name)
    {
        this.team2Name = team2Name;
    }


    public void setSportType(MatchType sportType)
    {
        this.sportType = sportType;
    }


    public MatchType getSportType()
    {
        return sportType;
    }


    public void setStatistic(Statistics statistic)
    {
        this.statistic = statistic;
    }


    public Statistics getStatistic()
    {
        return statistic;
    }


    /**
     * Overriding method for gson library (Object -> json, json -> Object)
     */
    @Override
    public String toString()
    {
        return "DataObject [matchid=" + matchid + ", timestamp=" + timestamp + ", team1Name=" + team1Name
                + ", team2Name=" + team2Name + ", sportType=" + sportType + ", statistic=" + statistic + "]";
    }
}
