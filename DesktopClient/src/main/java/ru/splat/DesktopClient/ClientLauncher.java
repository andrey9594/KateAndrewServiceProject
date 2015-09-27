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
public class ClientLauncher
{
    private static final Logger log = LoggerFactory.getLogger(ClientLauncher.class);


    /**
     * Main method, witch create ant start DesktopClient
     *
     * @param args
     */
    public static void main(String[] args)
    {

        log.info("Client is creating...");
        Client client = Client.getInstance();
        try
        {
            client.start();
            log.info("Client is started");
        }
        catch (IOException | TimeoutException e)
        {
            log.error("Can't create an Client!", e);
            e.printStackTrace();
        }

    }
}
