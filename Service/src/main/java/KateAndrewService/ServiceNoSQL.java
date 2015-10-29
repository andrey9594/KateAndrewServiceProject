package KateAndrewService;


import java.beans.Customizer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import matchstatistic.Match;
import matchstatistic.MatchType;
import matchstatistic.Statistics;
import matchstatistic.sportstatistictypes.StatisticType;
import ru.splat.kateandrewserviceprojectgenerated.EventEntryTCP;
import ru.splat.kateandrewserviceprojectgenerated.EventList;
import ru.splat.kateandrewserviceprojectgenerated.MatchEntryTCP;
import ru.splat.kateandrewserviceprojectgenerated.MatchList;


/**
 * <p>
 *
 * @author Andrew & Ekaterina Connect with providers and give info in xml and json format, cache and save info in NoSQL
 *         DB MongoDB
 */
public class ServiceNoSQL
{
    private static final Logger log = LoggerFactory.getLogger(ServiceNoSQL.class);

    private final String PATH_TO_CONFIG_FILE = "resources/config.ini";

    private final String PATH_TO_STATISTIC_TYPE = "resources/statistic_type.csv";

    private final String IP_PROVIDER;

    private final int PORT_XML;

    private final int PORT_JSON;

    private final int PORT_DB;

    private final String IP_DB;

    private final String DATABASE_NAME;

    private final String COLLECTION_NAME_FOR_JSON;

    private final String COLLECTION_NAME_FOR_XML;

    private final String PATH_TO_MATCHID_FILE;

    private final Map<Integer, MatchType> matchidToSportName;

    private MongoClient mongo;

    private DB db;

    private DBCollection collectionForJson;

    private DBCollection collectionForXml;

    private Map<Integer, Match> cache = new HashMap<>(); // TODO: cache <Integer(matchid), Match>

    private Map<MatchType, Map<Integer, StatisticType>> codeToStatistic = new HashMap<>(); // <Soccer, <1026, ATTACK>>

    private Map<Integer, ReentrantReadWriteLock> locks = new HashMap<>();


    public ServiceNoSQL()
    {
        log.info("Take parameters from config file");

        /**
         * path_to_matchid_list: .csv file with map: matchid in logs -> match sport name
         */
        Properties properties = new Properties();
        try
        {
            properties.load(new FileInputStream(new File(PATH_TO_CONFIG_FILE)));
        }
        catch (IOException e)
        {
            log.error("Can't read config.ini! Does it exist?");
            e.printStackTrace();
        }

        IP_PROVIDER = properties.getProperty("IP_PROVIDER");
        PORT_XML = Integer.valueOf(properties.getProperty("PORT_XML"));
        PORT_JSON = Integer.valueOf(properties.getProperty("PORT_JSON"));
        PORT_DB = Integer.valueOf(properties.getProperty("PORT_DB"));
        IP_DB = properties.getProperty("IP_DB");
        DATABASE_NAME = properties.getProperty("DATABASE_NAME");
        COLLECTION_NAME_FOR_JSON = properties.getProperty("COLLECTION_NAME_FOR_JSON");
        COLLECTION_NAME_FOR_XML = properties.getProperty("COLLECTION_NAME_FOR_XML");
        PATH_TO_MATCHID_FILE = properties.getProperty("PATH_TO_MATCHID_LIST");

        MatchidAndNameOfSportReader reader = null;
        try
        {
            reader = new MatchidAndNameOfSportReader(PATH_TO_MATCHID_FILE);
        }
        catch (FileNotFoundException e)
        {
            log.error("File with map matchid->nameofsport not found in {}!", PATH_TO_MATCHID_FILE, e);
            e.printStackTrace();
        }
        matchidToSportName = reader.getSportNameForMatchidMap();

        StatisticOfCodeReader statisticReader = null;
        try
        {
            statisticReader = new StatisticOfCodeReader(PATH_TO_STATISTIC_TYPE);
        }
        catch (FileNotFoundException e)
        {
            log.error("File with map event_code_id->statistic_type not found in {}!", PATH_TO_STATISTIC_TYPE, e);
            e.printStackTrace();
        }
        codeToStatistic = statisticReader.getStatisticForCodeMap();

        log.info("Parameters were successfully loaded");
    }


    /**
     * Method for starting Service work
     */
    public void start()
    {
        // connectDB();
        // loadPackagesFromDBToCache();
        connectToProviders();
        log.info("Service was started");
    }

    /**
     * connect to DB
     */
    // private void connectDB()
    // {
    // log.info("Creating connection to MongoDB...");
    //
    // try
    // {
    // mongo = new MongoClient(IP_DB, PORT_DB);
    // }
    // catch (UnknownHostException e)
    // {
    // log.error("Can't connect to MongoDB", e);
    // e.printStackTrace();
    // }
    // mongo.setWriteConcern(WriteConcern.JOURNALED);
    //
    // db = mongo.getDB(DATABASE_NAME);
    //
    // if (!db.collectionExists(COLLECTION_NAME_FOR_JSON))
    // {
    // db.createCollection(COLLECTION_NAME_FOR_JSON, null);
    // db.getCollection(COLLECTION_NAME_FOR_JSON).createIndex(new BasicDBObject("id", 1), null, true);
    // }
    // collectionForJson = db.getCollection(COLLECTION_NAME_FOR_JSON);
    //
    // if (!db.collectionExists(COLLECTION_NAME_FOR_XML))
    // {
    // db.createCollection(COLLECTION_NAME_FOR_XML, null);
    // db.getCollection(COLLECTION_NAME_FOR_XML).createIndex(new BasicDBObject("id", 1), null, true);
    // }
    // collectionForXml = db.getCollection(COLLECTION_NAME_FOR_XML);
    //
    // log.info("Connection to MongoDB was created");
    // }


    /**
     * Method for loading data from DB to cache
     */
    // private void loadPackagesFromDBToCache()
    // {
    // log.info("loading data from xml collection into cache");
    // DBCursor cursorForXmlCollection = collectionForXml.find();
    // try
    // {
    // while (cursorForXmlCollection.hasNext())
    // {
    // DBObject document = cursorForXmlCollection.next();
    // cache.put((Integer) document.get("id"), (Integer) document.get("value"));
    // }
    // }
    // finally
    // {
    // cursorForXmlCollection.close();
    // }
    // log.info("Data from xml collection was loaded into cache");
    //
    // log.info("loading data from json collection into cache");
    // DBCursor cursorForJsonCollection = collectionForJson.find();
    // try
    // {
    // while (cursorForJsonCollection.hasNext())
    // {
    // DBObject document = cursorForJsonCollection.next();
    // cache.put((Integer) document.get("id"), (Integer) document.get("value"));
    // }
    // }
    // finally
    // {
    // cursorForJsonCollection.close();
    // }
    // log.info("Data from json collection was loaded into cache");
    // }

    /**
     * take info from providers in xml and json format
     */
    private void connectToProviders()
    {
        log.info("Creating thread for working with xml provider...");
        try
        {
            Socket socket = new Socket(IP_PROVIDER, PORT_XML);
            Thread threadxml = new Thread(new ThreadForXmlProvider(socket, new Producer()));
            threadxml.start();
        }
        catch (IOException | TimeoutException e)
        {
            log.error("Error connecting with xml provider", e);
            e.printStackTrace();
        }
        log.info("Thread for working with xml provider was created");

        // log.info("Creating thread for working with json provider...");
        // try
        // {
        // Socket socket = new Socket(IP_PROVIDER, PORT_JSON);
        // Thread threadjson = new Thread(new ThreadForJsonProvider(socket, new Producer()));
        // threadjson.start();
        // }
        // catch (IOException | TimeoutException e)
        // {
        // log.error("Error connecting with json provider", e);
        // e.printStackTrace();
        // }
        // log.info("Thread for working with json provider was created");
    }

    /**
     * update/insert value in xml MongoDB collection
     *
     * @param id identifier of Object, which value we update/insert
     * @param value positive or negative value, which we update/insert
     */
    // private void upsertValueXml(int id, int value)
    // {
    // BasicDBObject query = new BasicDBObject("id", id);
    // BasicDBObject newDocument = new BasicDBObject("id", id).append("value", value);
    //
    // boolean upsert = true;
    // boolean multiUpsert = false;
    // collectionForXml.update(query, newDocument, upsert, multiUpsert);
    //
    // log.info("value in xml MongoDB collection updated");
    // }

    /**
     * update/insert value in json MongoDB collection
     *
     * @param id identifier of Object, which value we update/insert
     * @param value positive or negative value, which we update/insert
     */
    // private void upsertValueJson(int id, int value)
    // {
    // BasicDBObject query = new BasicDBObject("id", id);
    // BasicDBObject newDocument = new BasicDBObject("id", id).append("value", value);
    //
    // boolean upsert = true;
    // boolean multiUpsert = false;
    // collectionForJson.update(query, newDocument, upsert, multiUpsert);
    //
    // log.info("value in json MongoDB collection updated");
    // }


    /**
     * <p>
     *
     * @author Andrey & Ekaterina inner class Thread For xml Provider
     */
    private class ThreadForXmlProvider implements Runnable
    {
        private final Socket socket;

        private final Producer producer;


        public ThreadForXmlProvider(Socket socket, Producer producer)
        {
            this.socket = socket;
            this.producer = producer;
        }


        /**
         * Take and deserialize data from Xml Provider
         */
        @Override
        public void run()
        {
            try (Scanner scanner = new Scanner(socket.getInputStream()))
            {
                while (true)
                {
                    String xml = scanner.nextLine();
                    StringBuilder s = new StringBuilder("");
                    for (int j = 0; j < xml.length() && xml.charAt(j) != ' ' && xml.charAt(j) != '>'; j++)
                        s.append(xml.charAt(j));
                    if (s.toString().equals("<event_list") || s.toString().equals("<event_list>"))
                    {
                        /**
                         * Parsing eventList from xml here
                         */
                        EventList eventList = null;
                        try
                        {
                            JAXBContext jaxbContext = JAXBContext.newInstance(EventList.class);
                            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                            eventList = (EventList) unmarshaller.unmarshal(new StringBufferInputStream(xml));
                        }
                        catch (JAXBException e)
                        {
                            log.error("Error parsing xml: {}", xml, e);
                            e.printStackTrace();
                        }

                        for (int j = 0; j < eventList.getEvent().size(); j++)
                        {
                            EventEntryTCP event = eventList.getEvent().get(j);
                            int curMatchId = event.getMatchid();
                            log.debug("Sport name of match with {} id is {}", curMatchId,
                                    matchidToSportName.get(curMatchId).name());

                            // update Matches
                            for (String allStatisticString : event.getStatistics())
                            {
                                for (String statisticsString : allStatisticString.split(" "))
                                {
                                    String splitedStatistic[] = statisticsString.split("=");
                                    int statisticCode = Integer.parseInt(splitedStatistic[0]);
                                    int statisticValue = Integer.parseInt(splitedStatistic[1]);

                                    /*
                                     * get statisticType for statisticCode (from .cvs file) from map home 1xxx, away
                                     * 2xxx teams
                                     */
                                    MatchType sportType = matchidToSportName.get(curMatchId);
                                    Map<Integer, StatisticType> tempMap = codeToStatistic.get(sportType);
                                    if (tempMap == null)
                                    {
                                        /**
                                         * if we don't have that type of sport, we just do nothing
                                         */
                                        continue;
                                    }
                                    StatisticType statisticType = codeToStatistic.get(sportType).get(statisticCode);
                                    if (statisticType == null)
                                    {
                                        /**
                                         * if we don't have that code of event, we just do nothing
                                         */
                                        continue;
                                    }
                                    log.debug("Statistic for matchid = " + curMatchId + ": " + statisticsString + " = "
                                            + statisticType);
                                    /**
                                     * @isHomeStatistic = @isGuestStatictic = true if statistic is general for all teams
                                     */
                                    boolean isHomeStatistic = (statisticValue / 1000) == 1; //
                                    boolean isGuestStatistic = (statisticValue / 1000) == 2; //

                                    /**
                                     * clients work with cache but don't work with
                                     */
                                    if (!locks.containsKey(curMatchId))
                                        locks.put(curMatchId, new ReentrantReadWriteLock());

                                    Statistics statistic = null;
                                    locks.get(curMatchId).writeLock().lock();
                                    try
                                    {
                                        Match currentMatch = null;
                                        if (!cache.containsKey(curMatchId))
                                        {
                                            currentMatch = new Match(matchidToSportName.get(curMatchId),
                                                    event.getTimestamp());
                                        }
                                        else
                                        {
                                            currentMatch = cache.get(curMatchId);
                                        }
                                        statistic = currentMatch.getStatistic(statisticType);
                                        if (statistic == null)
                                        {
                                            statistic = new Statistics(statisticType);
                                        }
                                        if (isHomeStatistic)
                                        {
                                            statistic.setValue1(statisticValue);
                                        }
                                        if (isGuestStatistic)
                                        {
                                            statistic.setValue2(statisticValue);
                                        }
                                        currentMatch.addStatistics(statistic);

                                        cache.put(curMatchId, currentMatch);
                                    }
                                    finally
                                    {
                                        locks.get(curMatchId).writeLock().unlock();
                                    }

                                    MatchStatisticsDelta statisticDelta = new MatchStatisticsDelta(curMatchId,
                                            event.getTimestamp(), sportType, statistic);

                                    producer.publish(statisticDelta);

                                    log.info("Statistics from xml for match with matchid = {} is received",
                                            statisticDelta.getMatchid());
                                }
                            }
                        }
                    }
                    else
                    {
                        /**
                         * Parsing matchList from xml here
                         */
                        MatchList matchList = null;
                        try
                        {
                            JAXBContext jaxbContext = JAXBContext.newInstance(MatchList.class);
                            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                            matchList = (MatchList) unmarshaller.unmarshal(new StringBufferInputStream(xml));
                        }
                        catch (JAXBException e)
                        {
                            log.error("Error parsing xml: {}", xml, e);
                            e.printStackTrace();
                        }

                        for (int j = 0; j < matchList.getMatch().size(); j++)
                        {
                            MatchEntryTCP matchEntry = matchList.getMatch().get(j);
                            int curMatchId = matchEntry.getMatchid();
                            MatchType sportType = matchidToSportName.get(curMatchId);

                            log.debug("Sport name of match with {} id is {}", curMatchId, sportType.name());

                            if (!locks.containsKey(curMatchId))
                            {
                                locks.put(curMatchId, new ReentrantReadWriteLock());
                            }

                            locks.get(curMatchId).writeLock().lock();
                            try
                            {
                                Match currentMatch = null;
                                if (!cache.containsKey(curMatchId))
                                {
                                    currentMatch = new Match(matchidToSportName.get(curMatchId),
                                            matchEntry.getTimestamp());
                                }
                                else
                                {
                                    currentMatch = cache.get(curMatchId);
                                }
                                currentMatch.setT1Name(Integer.toString(matchEntry.getTeam1Id()));
                                currentMatch.setT2Name(Integer.toString(matchEntry.getTeam2Id()));
                                cache.put(curMatchId, currentMatch);
                            }
                            finally
                            {
                                locks.get(curMatchId).writeLock().unlock();
                            }
                            MatchStatisticsDelta matchStatisticsDelta = new MatchStatisticsDelta(curMatchId,
                                    matchEntry.getTimestamp(), matchEntry.getTeam1Id(), matchEntry.getTeam2Id());                           
                            producer.publish(matchStatisticsDelta);
                            log.info("Information about team's id is received");
                        }

                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                log.error("Error in receiving data from XmlProvider", e);
            }
            finally
            {
                try
                {
                    if (socket != null)
                        socket.close();
                }
                catch (IOException e)
                {
                    log.error("Error in socket closing", e);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * <p>
     *
     * @author Ekaterina inner class Thread For Json Provider
     */
    // private class ThreadForJsonProvider implements Runnable
    // {
    //
    // private final Socket socket;
    //
    // private final Producer producer;
    //
    //
    // public ThreadForJsonProvider(Socket socket, Producer producer)
    // {
    // this.socket = socket;
    // this.producer = producer;
    // }
    //
    //
    // /**
    // * Take and deserialize data from Json Provider
    // */
    // @Override public void run()
    // {
    // try
    // {
    // Gson gson = new Gson();
    // Scanner scanner = new Scanner(socket.getInputStream());
    //
    // while (true)
    // {
    //// String json = scanner.next();
    //// ProviderPackage providerPackage = gson.fromJson(json, ProviderPackage.class);
    ////
    //// cacheJson(providerPackage.getId(), providerPackage.getValue());
    //// producer.publish(providerPackage);
    ////
    //// log.info("Data from json provider with Value = {} is received", providerPackage.getValue());
    // break;////////////////////////////////////////////////////////
    // }
    // }
    // catch (IOException e)
    // {
    // log.error("Error in receiving data from Json Provider", e);
    // e.printStackTrace();
    // }
    // finally
    // {
    // try
    // {
    // socket.close();
    // }
    // catch (IOException e)
    // {
    // log.error("Error in socket closing", e);
    // e.printStackTrace();
    // }
    // }
    // }
    // }
}