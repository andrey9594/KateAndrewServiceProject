package KateAndrewService;

import com.google.gson.Gson;

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
    private Socket socket;
    private Producer producer;
    private Service service;

    public ThreadForJsonProvider(Socket socket, Producer producer, Service service) {
        this.socket = socket;
        this.producer = producer;
        this.service = service;
    }

    @Override
    public void run() {

        try {
            Gson gson = new Gson();
            Scanner scanner = new Scanner(socket.getInputStream());

            while (true) {
                String json = scanner.next();
                ProviderPackage providerPackage = gson.fromJson(json, ProviderPackage.class);
                producer.publish(providerPackage);
                //System.out.println(prov.getId() + ", " + prov.getValue());
                service.cacheJson(providerPackage.getId(), providerPackage.getValue());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}


