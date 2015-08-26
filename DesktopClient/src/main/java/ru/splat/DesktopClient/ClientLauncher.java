package ru.splat.DesktopClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 *
 * @author Ekaterina
 *         Main class for launching a Desktop Client
 */
public class ClientLauncher extends Client {
    private static final Logger log = LoggerFactory.getLogger(ClientLauncher.class);

    private ClientLauncher() {
        super();
    }

    public static void main(String[] args) {
        Client client = Client.getInstance();
        try {
            client.start();
        } catch (IOException | TimeoutException e) {
            log.error("Can't create an Client!", e);
            e.printStackTrace();
        }
    }
}
