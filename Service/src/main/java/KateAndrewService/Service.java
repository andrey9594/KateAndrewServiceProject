package KateAndrewService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * <p>
 *
 * @author Ekaterina
 *         Connect with provider and give info in xml format,
 *         cache and save info in BD MySql
 */
public class Service {
    private static final int PORT = 12345;
    private static final String PORT_BD = "3306";
    private static final String IP = "localhost";
    private Connection connection = null;
    private Map<Integer, Integer> cache = new HashMap<>();
    private Map<Integer, ReentrantReadWriteLock> locks = new HashMap<>();

    public void connect() {
        try {
            Socket socket = new Socket(IP, PORT);
            try {
                InputStream input = socket.getInputStream();
                StringBuilder xmlString = new StringBuilder();
                while (true) {
                    int b = input.read();
                    if (b == -1) {
                        break;
                    } else if (b == 0) {
                        JAXBContext jaxbContext = JAXBContext.newInstance(ProviderPackage.class);
                        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                        ProviderPackage prov = (ProviderPackage) unmarshaller.unmarshal(new StringBufferInputStream(xmlString.toString()));
                        //System.out.println(prov.getId() + "\n" + prov.getValue());
                        cache(prov.getId(), prov.getValue());
                        xmlString = new StringBuilder();
                    } else {
                        xmlString.append((char) b);
                    }
                }
            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private void updateValue(int id, int value) throws SQLException {

        String query = "UPDATE providerxml SET value = '" + value + "' WHERE id ='" + id + "'";

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);

    }

    public void cache(int id, int value) throws SQLException {
        if (!locks.containsKey(id)) {
            locks.put(id, new ReentrantReadWriteLock());
        }
        locks.get(id).writeLock().lock();

        updateValue(id, value);

        cache.put(id, value);
        locks.get(id).writeLock().unlock();
    }

}
