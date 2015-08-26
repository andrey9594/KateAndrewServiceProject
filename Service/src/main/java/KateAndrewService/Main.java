package KateAndrewService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>
 * Main method
 *
 * @author Ekaterina
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Service.class);

    public static void main(String[] args) {
        Service client = new Service();
        try {
            client.config();
        } catch (IOException e) {
            log.error("Error in reading of confifg file");
            e.printStackTrace();
        }
        client.connectBD();
        client.connect();
    }
}
