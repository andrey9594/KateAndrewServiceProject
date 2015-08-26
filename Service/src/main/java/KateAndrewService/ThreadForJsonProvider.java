package KateAndrewService;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * <p>
 *
 * @author Ekaterina
 *         Thread For Json Provider
 */
public class ThreadForJsonProvider implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ThreadForJsonProvider.class);

    private Socket socket;
    private Producer producer;
    private Service service;

    public ThreadForJsonProvider(Socket socket, Producer producer, Service service) {
        this.socket = socket;
        this.producer = producer;
        this.service = service;
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
                producer.publish(providerPackage);
                service.cacheJson(providerPackage.getId(), providerPackage.getValue());
                log.info("Data from Json Provider with Value= {} is received", providerPackage.getValue());
            }

        } catch (SQLException e) {
            log.error("Error in connect BD");
            e.printStackTrace();
        } catch (IOException e1) {
            log.error("Error in receiving data from Json Provider");
            e1.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e1) {
                log.error("Error in socket closing");
                e1.printStackTrace();
            }
        }
    }
}


