package KateAndrewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * <p>
 *
 * @author Ekaterina
 *         Connect with provider and give info in xml and json format,
 *         cache and save info in BD MySql
 */
public class ServiceSQL {
    private static final Logger log = LoggerFactory.getLogger(ServiceSQL.class);

    private static int PORT_xml;
    private static int PORT_json;
    private static String PORT_BD;
    private static String IP;
    private static String Login;
    private static String Password;
    private Connection connection = null;
    private Map<Integer, Integer> cache = new HashMap<>();
    private Map<Integer, ReentrantReadWriteLock> locks = new HashMap<>();

    /**
     * take parameters from config file
     */

    public void config() throws IOException {
        log.info("Take parameters from config file");
        Properties props = new Properties();

        props.load(new FileInputStream(new File("config.ini")));

        PORT_xml = Integer.valueOf(props.getProperty("PORT_xml"));
        PORT_json = Integer.valueOf(props.getProperty("PORT_json"));
        PORT_BD = props.getProperty("PORT_BD");
        IP = props.getProperty("IP");
        Login = props.getProperty("Login");
        Password = props.getProperty("Password");
    }

    /**
     * take info from providers in xml and json format
     */
    public void connect() {
        try {
            Socket socket = new Socket(IP, PORT_xml);
            log.info("Connect with xml provider");
      //      Thread threadxml = new Thread(new ThreadForXmlProvider(socket, new Producer(), this));
      //      threadxml.start();
        } catch (IOException e) {
            log.error("Error in connect with xml provider ");
            e.printStackTrace();
        } /*(catch (TimeoutException e) {
            log.error("Time-out at 'threadxml' Thread");
            e.printStackTrace();
        }*/
        try {
            Socket socket = new Socket(IP, PORT_json);
            log.info("Connect with json provider");
       //     Thread threadjson = new Thread(new ThreadForJsonProvider(socket, new Producer(), this));
       //     threadjson.start();
        } catch (UnknownHostException e) {
            log.error("Unknown Host");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Error in connect with json provider ");
            e.printStackTrace();
        }/* catch (TimeoutException e) {
            log.error("Time-out at 'threadjson' Thread");
            e.printStackTrace();
        }*/
    }

    /**
     * connect with BD
     */
    public void connectBD() {
        try {
            log.info("Connect was created");
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + IP + ":" + PORT_BD + "/service",
                    Login, Password);
        } catch (SQLException e) {
            log.error("Error in connect with BD");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.error("Class 'com.mysql.jdbc.Driver' Not Found");
            e.printStackTrace();
        }
        try {
            clearProviderjsonTable();
            clearProviderxmlTable();
        } catch (SQLException e) {
            log.error("Error in connect with BD");
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
        log.info("Value in providerxml table update");

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
        log.info("Value in providerjson table update");
    }

    /**
     * insert Value in table "providerxml"
     *
     * @param id
     * @param value
     */
    private void insertValueXml(int id, int value) throws SQLException {
        String query = "INSERT INTO providerxml" + "(id, value) VALUES ('" + id + "','" + value + "')";

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
        log.debug("Value: {} is insert in 'providerxml' table", value);
    }

    /**
     * insert Value in table "providerjson"
     *
     * @param id
     * @param value
     */
    private void insertValueJson(int id, int value) throws SQLException {
        String query = "INSERT INTO providerjson" + "(id, value) VALUES ('" + id + "','" + value + "')";

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
        log.debug("Value: {} is insert in 'providerjson' table", value);
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
            insertValueXml(id, value);
        }
        locks.get(id).writeLock().lock();

        updateValueXml(id, value);

        cache.put(id, value);
        log.info("Cache info from provider, which send info in Xml format");
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

        if (!cache.containsKey(id)) {
            insertValueJson(id, value);
        } else updateValueJson(id, value);

        cache.put(id, value);
        log.info("Cache info from provider, which send info in Json format");
        locks.get(id).writeLock().unlock();
    }

    /**
     * Clear "providerxml" table
     */
    public void clearProviderxmlTable() throws SQLException {
        String query = "TRUNCATE TABLE providerxml";

        Statement stmt = connection.createStatement();

        stmt.executeUpdate(query);
        log.info("'providerxml' table has been cleared");
    }

    /**
     * Clear "providerjson" table
     */
    public void clearProviderjsonTable() throws SQLException {
        String query = "TRUNCATE TABLE providerjson";

        Statement stmt = connection.createStatement();

        stmt.executeUpdate(query);
        log.info("'providerjson' table has been cleared");
    }

}