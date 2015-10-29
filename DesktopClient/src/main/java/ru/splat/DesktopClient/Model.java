package ru.splat.DesktopClient;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import matchstatistic.Match;
import matchstatistic.Statistics;
import matchstatistic.sportstatistictypes.StatisticType;


/**
 * <p>
 *
 * @author Andrey & Ekaterina
 *         <p>
 *         Model of MVC
 */
public class Model implements Subject
{
    private static final Logger log = LoggerFactory.getLogger(Model.class);

    private ArrayList<Observer> observers = new ArrayList<Observer>();

    // private com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> modelTable =

    /**
     * Don't use guava because we don't need to remove old match's statistics
     */
    private Map<Integer, Match> modelTable;


    public void setMatchForMatchid(int matchid, Match match)
    {
        log.info("Put match with matchid = " + matchid);
        modelTable.put(matchid, match);
    }


    public Statistics getStatisticForMatchid(int matchid, StatisticType statisticType)
    {
        return modelTable.get(matchid).getStatistic(statisticType);
    }


    public String getTeam1idForMatchid(int matchid)
    {
        return modelTable.get(matchid).getT1Name();
    }


    public String getTeam2idForMatchid(int matchid)
    {
        return modelTable.get(matchid).getT2Name();
    }


    public void addPackage(MatchStatisticsDelta matchStatisticDelta)
    {
        Match match = modelTable.get(matchStatisticDelta.getMatchid());
        if (match == null)
        {
            match = new Match(matchStatisticDelta.getSportType(), matchStatisticDelta.getTimestamp());
        }
        match.addStatistics(matchStatisticDelta.getStatistic());
        modelTable.put(matchStatisticDelta.getMatchid(), match);
        log.info("Put match with matchid = " + matchStatisticDelta.getMatchid());
    }


    @Override
    public void registerObserver(Observer o)
    {
        log.debug("added new observer");
        observers.add(o);
    }


    @Override
    public void removeObserver(Observer o)
    {
        log.debug("removed one of observers");
        observers.remove(o);
    }


    @Override
    public void notifyAllObserver(OperationType operation, int matchid)
    {
        log.info("Update all observers");
        for (Observer o : observers)
        {
            o.update(operation, matchid);
        }
    }

}
