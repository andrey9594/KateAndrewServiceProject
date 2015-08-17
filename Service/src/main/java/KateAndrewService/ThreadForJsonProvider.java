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

    Service service = new Service();

    @Override
    public void run() {

        try {
            Socket socket = new Socket(service.getIP(), service.getPORT_json());
            try {
                Gson gson = new Gson();
                Scanner scanner = new Scanner(socket.getInputStream());
                while (true) {
                    String json = scanner.next();
                    ProviderPackage prov = gson.fromJson(json, ProviderPackage.class);
                    //System.out.println(prov.getId() + ", " + prov.getValue());
                    service.cacheJson(prov.getId(), prov.getValue());
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error here!");
            e.printStackTrace();
        }
    }
}

