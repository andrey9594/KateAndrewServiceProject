package KateAndrewService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
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
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;


/**
 * <p>
 *
 * @author Andrew&Ekaterina
 *         Connect with provider and give info in xml and json format,
 *         cache and save info in NoSQL DB MongoDB
 */
public class ServiceNoSQL {
    private static final Logger log = LoggerFactory.getLogger(ServiceNoSQL.class);

    private String IP_PROVIDER;
    private int PORT_XML;
    private int PORT_JSON;
    private int PORT_DB;
    private String IP_DB;
    private String DATABASE_NAME;
    private String COLLECTION_NAME_FOR_JSON;
    private String COLLECTION_NAME_FOR_XML;
    
    private MongoClient mongo;
    private DB db;
    private DBCollection collectionForJson;
    private DBCollection collectionForXml;
    
    private Map<Integer, Integer> cache = new HashMap<>();
    private Map<Integer, ReentrantReadWriteLock> locks = new HashMap<>();

    /**
     * Initial constructor
     * @throws IOException 
     */
    public void start() {
    	config();
		connectBD();
		loadPackagesFromDBToCache();
		connectToProviders();
	}
    
    /**
     * take parameters from config file
     */ 
    private void config() {
        log.info("Take parameters from config file");
        
        Properties properties = new Properties();
        try {
			properties.load(new FileInputStream(new File("config.ini")));
		} catch (IOException e) {
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
        
        log.info("Parameters were successfully loaded");
    }

    /**
     * connect with BD
     */
	public void connectBD() {
		log.info("Creating connection to MongoDB...");
		
		try {
			mongo = new MongoClient(IP_DB, PORT_DB);
		} catch (UnknownHostException e) {
			log.error("Can't connect to MongoDB", e);
			e.printStackTrace();
		}
		mongo.setWriteConcern(WriteConcern.JOURNALED);
		
		db = mongo.getDB(DATABASE_NAME);
		
		if (!db.collectionExists(COLLECTION_NAME_FOR_JSON)) {
			db.createCollection(COLLECTION_NAME_FOR_JSON, null);
			db.getCollection(COLLECTION_NAME_FOR_JSON).createIndex(new BasicDBObject("id", 1), null, true);
		}
		collectionForJson = db.getCollection(COLLECTION_NAME_FOR_JSON);
		
		if (!db.collectionExists(COLLECTION_NAME_FOR_XML)) {
			db.createCollection(COLLECTION_NAME_FOR_XML, null);
			db.getCollection(COLLECTION_NAME_FOR_XML).createIndex(new BasicDBObject("id", 1), null, true);
		}
		collectionForXml = db.getCollection(COLLECTION_NAME_FOR_XML);
		
		log.info("Connection to MongoDB was created");
	}
	
	private void loadPackagesFromDBToCache() {
		log.info("loading data from xml collection into cache");
		DBCursor cursorForXmlCollection = collectionForXml.find();
		try {
			while (cursorForXmlCollection.hasNext()) {
				DBObject document = cursorForXmlCollection.next();
				cache.put((Integer)document.get("id"), (Integer)document.get("value"));
			}
		} finally {
			cursorForXmlCollection.close();
		}
		log.info("Data from xml collection was loaded into cache");
		
		log.info("loading data from json collection into cache");
		DBCursor cursorForJsonCollection = collectionForJson.find();
		try {
			while (cursorForJsonCollection.hasNext()) {
				DBObject document = cursorForJsonCollection.next();
				cache.put((Integer)document.get("id"), (Integer)document.get("value"));
			}
		} finally {
			cursorForJsonCollection.close();
		}
		log.info("Data from json collection was loaded into cache");
	}
	
    /**
     * take info from providers in xml and json format
     */
    private void connectToProviders() {
    	log.info("Creating thread for working with xml provider...");
        try {
            Socket socket = new Socket(IP_PROVIDER, PORT_XML);
            Thread threadxml = new Thread(new ThreadForXmlProvider(socket, new Producer()));
            threadxml.start();
        } catch (IOException | TimeoutException e) {
            log.error("Error connecting with xml provider", e);
            e.printStackTrace();
        }
    	log.info("Thread for working with xml provider was created");
    	
    	log.info("Creating thread for working with json provider...");
        try {
            Socket socket = new Socket(IP_PROVIDER, PORT_JSON);
            Thread threadjson = new Thread(new ThreadForJsonProvider(socket, new Producer()));
            threadjson.start();
        } catch (IOException | TimeoutException e) {
            log.error("Error connecting with json provider", e);
            e.printStackTrace();
        } 
    	log.info("Thread for working with json provider was created");
    }

    /**
     * update/insert value in xml MongoDB collection
     *
     * @param id
     * @param value
     */
    private void upsertValueXml(int id, int value) {
    	BasicDBObject query = new BasicDBObject("id", id);
    	BasicDBObject newDocument = new BasicDBObject("id", id).append("value", value);
    	
    	boolean upsert = true;
    	boolean multiUpsert = false;
    	collectionForXml.update(query, newDocument, upsert, multiUpsert);	
    	
        log.info("value in xml MongoDB collection updated");

    }

    /**
     * update/insert value in json MongoDB collection
     *
     * @param id
     * @param value
     */
    private void upsertValueJson(int id, int value) {
    	BasicDBObject query = new BasicDBObject("id", id);
    	BasicDBObject newDocument = new BasicDBObject("id", id).append("value", value);
    	
    	boolean upsert = true;
    	boolean multiUpsert = false;
    	collectionForJson.update(query, newDocument, upsert, multiUpsert);	   	
    	
        log.info("value in json MongoDB collection updated");
    }

    /**
     * cache info from provider, which send info in Xml format
     *
     * @param id
     * @param value
     */

    private void cacheXml(int id, int value) {
		if (!locks.containsKey(id)) {
			locks.put(id, new ReentrantReadWriteLock());
		}
		locks.get(id).writeLock().lock();
		try {
			upsertValueXml(id, value);
			cache.put(id, value);

			log.info("Cache info from provider, which send info in xml format");
		} finally {
			locks.get(id).writeLock().unlock();
		}
    }

    /**
     * cache info from provider, which send info in Json format
     *
     * @param id
     * @param value
     */
    private void cacheJson(int id, int value) {
		if (!locks.containsKey(id)) {
			locks.put(id, new ReentrantReadWriteLock());
		}
		locks.get(id).writeLock().lock();
		try {
			upsertValueJson(id, value);
			cache.put(id, value);

			log.info("Cache info from provider, which send info in json format");
		} finally {
			locks.get(id).writeLock().unlock();
		}
    }
   
    /**
     * <p>
     *
     * @author Ekaterina
     *         Thread For Xml Provider
     */
    private class ThreadForXmlProvider implements Runnable {
  
        private final Socket socket;
        private final Producer producer;

        public ThreadForXmlProvider(Socket socket, Producer producer) {
            this.socket = socket;
            this.producer = producer;
        }

        /**
         * Take and deserialize data from Xml Provider
         */
        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(socket.getInputStream());

                while (true) {
                    String xmlString = scanner.nextLine();
                  
                    JAXBContext jaxbContext = JAXBContext.newInstance(ProviderPackage.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    ProviderPackage providerPackage = (ProviderPackage) unmarshaller.unmarshal(new StringBufferInputStream(xmlString));
                    
                    cacheXml(providerPackage.getId(), providerPackage.getValue());
                    producer.publish(providerPackage);
                    
                    log.info("Data from xml provider with Value = {} is received",providerPackage.getValue());
                }
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
                log.error("Error in receiving data from XmlProvider", e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
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
     *         Thread For Json Provider
     */
    private class ThreadForJsonProvider implements Runnable {

        private final Socket socket;
        private final Producer producer;

        public ThreadForJsonProvider(Socket socket, Producer producer) {
            this.socket = socket;
            this.producer = producer;
        }

        /**
         * Take and deserialize data from Json Provider
         */
        @Override
        public void run() {
            try {
                Gson gson = new Gson();
                Scanner scanner = new Scanner(socket.getInputStream());

                while (true) {
                    String json = scanner.next();
                    ProviderPackage providerPackage = gson.fromJson(json, ProviderPackage.class);
                    
                    cacheJson(providerPackage.getId(), providerPackage.getValue());
                    producer.publish(providerPackage);
                    
                    log.info("Data from json provider with Value = {} is received",providerPackage.getValue());
                }
            } catch (IOException e) {
                log.error("Error in receiving data from Json Provider", e);
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("Error in socket closing", e);
                    e.printStackTrace();
                }
            }
        }
    }
}