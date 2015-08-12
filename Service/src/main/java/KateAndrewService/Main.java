package KateAndrewService;

import java.io.IOException;

/**
 * <p>
 * Main method
 *
 * @author Ekaterina
 */
public class Main {

    public static void main(String[] args) {
        Service client = new Service();
        try {
            client.congig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.connectBD("root", "root");
        client.connect();
    }
}
