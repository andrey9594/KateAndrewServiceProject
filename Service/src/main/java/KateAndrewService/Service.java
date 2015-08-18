package KateAndrewService;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 *
 * @author Ekaterina
 *         Connect with provider and give info in xml and json format,
 *         cache and save info in BD MySql
 */
public class Service {
    static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Service.class);
    private static int PORT_xml;
    private static int PORT_json;
    private static String PORT_BD;
    private static String IP;
    private Connection connection = null;
    private Map<Integer, Integer> cache = new HashMap<>();
    private Map<Integer, ReentrantReadWriteLock> locks = new HashMap<>();
    Producer producer;

    public static String getIP() {
        return IP;
    }

    public static int getPORT_xml() {
        return PORT_xml;
    }

    public static int getPORT_json() {
        return PORT_json;
    }

    /**
     * take parameters from config file
     */
    public void congig() throws IOException {
        Properties props = new Properties();

        props.load(new FileInputStream(new File("config.ini")));

        PORT_xml = Integer.valueOf(props.getProperty("PORT_xml"));
        PORT_json = Integer.valueOf(props.getProperty("PORT_json"));
        PORT_BD = props.getProperty("PORT_BD");
        IP = props.getProperty("IP");
    }


    /**
     * take info from providers in xml and json format
     */
    public void connect() {
        try {
            Socket socket = new Socket(IP, PORT_xml);
            Thread threadxml = new Thread(new ThreadForXmlProvider(socket, producer, this));
            threadxml.start();
        } catch (IOException e) {
            e.printStackTrace();

        }
        try {
            Socket socket = new Socket(IP, PORT_json);
            Thread threadjson = new Thread(new ThreadForJsonProvider(socket, producer, this));
            threadjson.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * connect with BD
     *
     * @param userName
     * @param userPass
     */
    public void connectBD(String userName, String userPass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + IP + ":" + PORT_BD + "/service",
                    userName, userPass);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * update Value in table "providerxml"
     *
     * @param id
     * @param value
     */
    private void updateValueXml(int id, int value) throws SQLException {

        String query = "UPDATE providerxml SET value = '" + value + "' WHERE id ='" + id + "'";

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);

    }

    /**
     * update Value in table "providerjson"
     *
     * @param id
     * @param value
     */
    private void updateValueJson(int id, int value) throws SQLException {

        String query = "UPDATE providerjson SET value = '" + value + "' WHERE id ='" + id + "'";

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);

    }

    /**
     * cache info from provider, which send info in Xml format
     *
     * @param id
     * @param value
     */
    public void cacheXml(int id, int value) throws SQLException {
        if (!locks.containsKey(id)) {
            locks.put(id, new ReentrantReadWriteLock());
        }
        locks.get(id).writeLock().lock();

        updateValueXml(id, value);

        cache.put(id, value);
        locks.get(id).writeLock().unlock();
    }

    /**
     * cache info from provider, which send info in Json format
     *
     * @param id
     * @param value
     */
    public void cacheJson(int id, int value) throws SQLException {
        if (!locks.containsKey(id)) {
            locks.put(id, new ReentrantReadWriteLock());
        }
        locks.get(id).writeLock().lock();

        updateValueJson(id, value);

        cache.put(id, value);
        locks.get(id).writeLock().unlock();
    }

}