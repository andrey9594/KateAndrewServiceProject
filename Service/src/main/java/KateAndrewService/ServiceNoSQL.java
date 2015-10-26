package KateAndrewService;


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

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import matchstatistic.MatchType;
import ru.splat.kateandrewserviceprojectgenerated.EventEntryTCP;
import ru.splat.kateandrewserviceprojectgenerated.EventList;


/**
 * <p>
 *
 * @author Andrew & Ekaterina
 *         Connect with providers and give info in xml and json format,
 *         cache and save info in NoSQL DB MongoDB
 */
public class ServiceNoSQL
{
    private static final Logger log = LoggerFactory.getLogger(ServiceNoSQL.class);

    private final String configFilePath = "resources/config.ini";
    
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

    private Map<Integer, Integer> cache = new HashMap<>(); // TODO: cache <Integer(matchid), Match>

    private Map<Integer, ReentrantReadWriteLock> locks = new HashMap<>();
    

    public ServiceNoSQL() {
    	log.info("Take parameters from config file");

    	/**
    	 * path_to_matchid_list: .csv file with map: matchid in logs -> match sport name
    	 */
        Properties properties = new Properties();
        try
        {
            properties.load(new FileInputStream(new File(configFilePath)));
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
		} catch (FileNotFoundException e) 
        {
			log.error("File with map matchid->nameofsport not found in {}!",
					PATH_TO_MATCHID_FILE, e);
			e.printStackTrace();
		}
		matchidToSportName = reader.getSportNameForMatchidMap();

        log.info("Parameters were successfully loaded");
    }

    /**
     * Method for starting Service work
     */
    public void start()
    {
//        connectDB();
//        loadPackagesFromDBToCache();
        connectToProviders();
        log.info("Service was started");
    }

    /**
     * connect to DB
     */
//    private void connectDB()
//    {
//        log.info("Creating connection to MongoDB...");
//
//        try
//        {
//            mongo = new MongoClient(IP_DB, PORT_DB);
//        }
//        catch (UnknownHostException e)
//        {
//            log.error("Can't connect to MongoDB", e);
//            e.printStackTrace();
//        }
//        mongo.setWriteConcern(WriteConcern.JOURNALED);
//
//        db = mongo.getDB(DATABASE_NAME);
//
//        if (!db.collectionExists(COLLECTION_NAME_FOR_JSON))
//        {
//            db.createCollection(COLLECTION_NAME_FOR_JSON, null);
//            db.getCollection(COLLECTION_NAME_FOR_JSON).createIndex(new BasicDBObject("id", 1), null, true);
//        }
//        collectionForJson = db.getCollection(COLLECTION_NAME_FOR_JSON);
//
//        if (!db.collectionExists(COLLECTION_NAME_FOR_XML))
//        {
//            db.createCollection(COLLECTION_NAME_FOR_XML, null);
//            db.getCollection(COLLECTION_NAME_FOR_XML).createIndex(new BasicDBObject("id", 1), null, true);
//        }
//        collectionForXml = db.getCollection(COLLECTION_NAME_FOR_XML);
//
//        log.info("Connection to MongoDB was created");
//    }


    /**
     * Method for loading data from DB to cache
     */
//    private void loadPackagesFromDBToCache()
//    {
//        log.info("loading data from xml collection into cache");
//        DBCursor cursorForXmlCollection = collectionForXml.find();
//        try
//        {
//            while (cursorForXmlCollection.hasNext())
//            {
//                DBObject document = cursorForXmlCollection.next();
//                cache.put((Integer) document.get("id"), (Integer) document.get("value"));
//            }
//        }
//        finally
//        {
//            cursorForXmlCollection.close();
//        }
//        log.info("Data from xml collection was loaded into cache");
//
//        log.info("loading data from json collection into cache");
//        DBCursor cursorForJsonCollection = collectionForJson.find();
//        try
//        {
//            while (cursorForJsonCollection.hasNext())
//            {
//                DBObject document = cursorForJsonCollection.next();
//                cache.put((Integer) document.get("id"), (Integer) document.get("value"));
//            }
//        }
//        finally
//        {
//            cursorForJsonCollection.close();
//        }
//        log.info("Data from json collection was loaded into cache");
//    }


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

//        log.info("Creating thread for working with json provider...");
//        try
//        {
//            Socket socket = new Socket(IP_PROVIDER, PORT_JSON);
//            Thread threadjson = new Thread(new ThreadForJsonProvider(socket, new Producer()));
//            threadjson.start();
//        }
//        catch (IOException | TimeoutException e)
//        {
//            log.error("Error connecting with json provider", e);
//            e.printStackTrace();
//        }
//        log.info("Thread for working with json provider was created");
    }


    /**
     * update/insert value in xml MongoDB collection
     *
     * @param id    identifier of Object, which value we update/insert
     * @param value positive or negative value, which we update/insert
     */
//    private void upsertValueXml(int id, int value)
//    {
//        BasicDBObject query = new BasicDBObject("id", id);
//        BasicDBObject newDocument = new BasicDBObject("id", id).append("value", value);
//
//        boolean upsert = true;
//        boolean multiUpsert = false;
//        collectionForXml.update(query, newDocument, upsert, multiUpsert);
//
//        log.info("value in xml MongoDB collection updated");
//    }


    /**
     * update/insert value in json MongoDB collection
     *
     * @param id    identifier of Object, which value we update/insert
     * @param value positive or negative value, which we update/insert
     */
//    private void upsertValueJson(int id, int value)
//    {
//        BasicDBObject query = new BasicDBObject("id", id);
//        BasicDBObject newDocument = new BasicDBObject("id", id).append("value", value);
//
//        boolean upsert = true;
//        boolean multiUpsert = false;
//        collectionForJson.update(query, newDocument, upsert, multiUpsert);
//
//        log.info("value in json MongoDB collection updated");
//    }


    /**
     * cache info from provider, which send info in Xml format
     *
     * @param id    identifier of Object, which value we cache
     * @param value positive or negative value, which we cache
     */
//    private void cacheXml(int id, int value)
//    {
//        if (!locks.containsKey(id))
//        {
//            locks.put(id, new ReentrantReadWriteLock());
//        }
//        locks.get(id).writeLock().lock();
//        try
//        {
//            upsertValueXml(id, value);
//            cache.put(id, value);
//
//            log.info("Cache info from provider, which send info in xml format");
//        }
//        finally
//        {
//            locks.get(id).writeLock().unlock();
//        }
//    }


    /**
     * cache info from provider, which send info in Json format
     *
     * @param id    identifier of Object, which value we cache
     * @param value positive or negative value, which we cache
     */
//    private void cacheJson(int id, int value)
//    {
//        if (!locks.containsKey(id))
//        {
//            locks.put(id, new ReentrantReadWriteLock());
//        }
//        locks.get(id).writeLock().lock();
//        try
//        {
//            upsertValueJson(id, value);
//            cache.put(id, value);
//
//            log.info("Cache info from provider, which send info in json format");
//        }
//        finally
//        {
//            locks.get(id).writeLock().unlock();
//        }
//    }


    /**
     * <p>
     *
     * @author Andrey & Ekaterina
     *         inner class
     *         Thread For xml Provider
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
            try(Scanner scanner = new Scanner(socket.getInputStream())) 
            {
                while (true)
                {
                	String xml = scanner.nextLine();
                	
                    /**
                     * Parsing of xml here
                     */
                    EventList eventList = null;
					try {
						JAXBContext jaxbContext = JAXBContext.newInstance(EventList.class);
						Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
						eventList = (EventList) unmarshaller.unmarshal(new StringBufferInputStream(xml));
					} catch (JAXBException e) {
						log.error("Error parsing xml: {}", xml, e);
                    	e.printStackTrace();
                    }

					for (int j = 0; j < eventList.getEvent().size(); j++) {
						EventEntryTCP event = eventList.getEvent().get(j);
						int curMatchId = event.getMatchid();
						log.debug("Sport name of match with {} id is {}", curMatchId,
								matchidToSportName.get(curMatchId).name());
 
						// update Matches
						for (String statisticString : event.getStatistics()) {
							log.info("Statistic for matchid = " + curMatchId + ": " + statisticString);
							MatchStatisticsDelta statisticDelta = new MatchStatisticsDelta(
									event.getMatchid(),
									matchidToSportName.get(event.getMatchid()),
									null, -1);//////////////////////////

							// cacheXml(providerPackage.getId(), /////////////////////// TODO
							// providerPackage.getValue());
							producer.publish(statisticDelta);

							log.info(
									"Statistic from xml for match with matchid = {} is received",
									statisticDelta.getMatchid());
	
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
     * @author Ekaterina
     *         inner class
     *         Thread For Json Provider
     */
//    private class ThreadForJsonProvider implements Runnable
//    {
//
//        private final Socket socket;
//
//        private final Producer producer;
//
//
//        public ThreadForJsonProvider(Socket socket, Producer producer)
//        {
//            this.socket = socket;
//            this.producer = producer;
//        }
//
//
//        /**
//         * Take and deserialize data from Json Provider
//         */
//        @Override public void run()
//        {
//            try
//            {
//                Gson gson = new Gson();
//                Scanner scanner = new Scanner(socket.getInputStream());
//
//                while (true)
//                {
////                    String json = scanner.next();
////                    ProviderPackage providerPackage = gson.fromJson(json, ProviderPackage.class);
////
////                    cacheJson(providerPackage.getId(), providerPackage.getValue());
////                    producer.publish(providerPackage);
////
////                    log.info("Data from json provider with Value = {} is received", providerPackage.getValue());
//                	break;////////////////////////////////////////////////////////
//                }
//            }
//            catch (IOException e)
//            {
//                log.error("Error in receiving data from Json Provider", e);
//                e.printStackTrace();
//            }
//            finally
//            {
//                try
//                {
//                    socket.close();
//                }
//                catch (IOException e)
//                {
//                    log.error("Error in socket closing", e);
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}