package ru.splat.DesktopClient;


import matchstatistic.MatchType;
import matchstatistic.Statistics;


/**
 * @author Andrey Class for updating statistics on desktop clients from a Service
 */
public class MatchStatisticsDelta
{
    private int team1id;

    private int team2id;

    private int matchid;

    private long timestamp;

    private MatchType sportType;

    private Statistics statistic;

    private MatchStatisticsDelta() { }

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
        this.team1id = this.team2id = -1;
    }


    /**
     * That constructor takes info about team1id and team2id
     * 
     * @param matchid
     * @param timestamp
     * @param team1id
     * @param team2id
     */
    public MatchStatisticsDelta(int matchid, long timestamp, int team1id, int team2id)
    {
        this.matchid = matchid;
        this.timestamp = timestamp;
        this.team1id = team1id;
        this.team2id = team2id;
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


    public int getTeam1id()
    {
        return team1id;
    }


    public void setTeam1id(int team1id)
    {
        this.team1id = team1id;
    }


    public int getTeam2id()
    {
        return team2id;
    }


    public void setTeam2id(int team2id)
    {
        this.team2id = team2id;
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
        return "DataObject [matchid=" + matchid + ", timestamp=" + timestamp + ", team1id=" + team1id + ", team2id="
                + team2id + ", sportType=" + sportType + ", statistic=" + statistic + "]";
    }
}
