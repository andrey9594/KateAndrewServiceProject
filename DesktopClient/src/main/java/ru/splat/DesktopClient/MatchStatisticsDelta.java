package ru.splat.DesktopClient;


import matchstatistic.MatchType;
import matchstatistic.Statistics;


/**
 * @author Andrey Class for updating statistics on desktop clients from a Service
 */
public class MatchStatisticsDelta
{

    private long matchid;

    private long timestamp;

    private MatchType sportType;

    private Statistics statistic;


    public MatchStatisticsDelta(long matchid, long timestamp, MatchType sportType, Statistics statistic)
    {
        this.matchid = matchid;
        this.timestamp = timestamp;
        this.sportType = sportType;
        this.statistic = statistic;
    }


    public void setMatchid(long matchid)
    {
        this.matchid = matchid;
    }


    public long getMatchid()
    {
        return matchid;
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
        return "DataObject [matchid=" + matchid + ", timestamp=" + timestamp + ", sportType=" + sportType
                + ", statistic=" + statistic + "]";
    }
}
