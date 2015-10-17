package ru.splat.kateandrewserviceproject;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * Class provider for transmitting data packages to the Service 
 * through a socket 
 * Possible only one connection at once
 */
public class Provider
{
    static private final Logger log = LoggerFactory.getLogger(Provider.class);

    private final int format;

    private static final int FORMAT_UNKNOWN = 0;

    private static final int FORMAT_XML = 1;

    private static final int FORMAT_JSON = 2;

    private final int PORT_NUM;

    private final int PERIOD_WAIT_TIME;
    
    private final String PATH_TO_LOGS;
    
    private final String LOG_FORMAT;


    /**
     * Constructor
     * 
     * @param propertiesPath Path to the config file
     */
    public Provider(String propertiesPath)
    {
        /**
         * properties: 
         * FORMAT = string: xml or json 
         * PORT_NUM = int 
         * PERIOD_TIME = int: send data every time interval. The time interval is random period: (0 ms, periodWaitTime ms] 
         * PATH_TO_LOGS = string: where logs are
         * LOG_FORMAT: logs extension
         */
        Properties properties = new Properties();
        log.info("Loading configuration from {}", propertiesPath);
        try
        {
            properties.load(new FileInputStream(new File(propertiesPath)));
        }
        catch (FileNotFoundException e)
        {
            log.error("Config file {} not found!", propertiesPath);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            log.error("Can't read config file {}", propertiesPath);
            e.printStackTrace();
        }
        String formatString = properties.getProperty("FORMAT");
        format = formatString.equals("xml") ? FORMAT_XML : formatString.equals("json") ? FORMAT_JSON : FORMAT_UNKNOWN;
        if (format == FORMAT_UNKNOWN)
        {
            log.error("Unknown format! Required xml or json");
            throw new IllegalArgumentException();
        }
        PORT_NUM = Integer.parseInt(properties.getProperty("PORT_NUM"));
        if (PORT_NUM < 1 || PORT_NUM > 65_535)
        {
            log.error("Illegal port id! Required number between 1 and 65535");
            throw new IllegalArgumentException();
        }
        PERIOD_WAIT_TIME = Integer.parseInt(properties.getProperty("PERIOD_TIME"));
        if (PERIOD_WAIT_TIME <= 0)
        {
            log.error("period_time must be positive number in milliseconds!");
            throw new IllegalArgumentException();
        }
        PATH_TO_LOGS = properties.getProperty("PATH_TO_LOGS");
        LOG_FORMAT = properties.getProperty("LOG_FORMAT");
        log.info("Configuration from {} has been successfully loaded", propertiesPath);
    }


    private void sendXmlObject(String xmlString, Socket socket)
    {
//        log.debug("Start sending package with id {} on the protocol xml", providerPackage.getId());
//        try
//        {
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            JAXBContext jaxbContext = JAXBContext.newInstance(ProviderPackage.class);
//            Marshaller marshaller = jaxbContext.createMarshaller();
//            StringWriter writer = new StringWriter();
//            marshaller.marshal(providerPackage, writer);
//            out.println(writer.toString());
//            out.flush();
//        }
//        catch (JAXBException | IOException e)
//        {
//            log.error("Error sending package with id {} on the protocol xml", providerPackage.getId(), e);
//            e.printStackTrace();
//        }
//        log.debug("The package with id {} was successfully sent on the protocol xml", providerPackage.getId());
    }


    private void sendJsonObject(String jsonString, Socket socket)
    {
//        log.debug("Start sending package with id {} on the protocol json", providerPackage.getId());
//        Gson gson = new Gson();
//        String json = gson.toJson(providerPackage);
//        try
//        {
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            out.println(json);
//            out.flush();
//        }
//        catch (IOException e)
//        {
//            log.error("Error sending package with id {} on the protocol json", providerPackage.getId(), e);
//            e.printStackTrace();
//        }
//        log.debug("The package with id {} was successfully sent on the protocol json", providerPackage.getId());
    }


    /**
     * Main method to start an object of Provider
     */
    public void start()
    {
        try
        {
            log.info("ServerSocket is creating...");
            ServerSocket serverSocket = new ServerSocket(PORT_NUM);
            log.info("ServerSocket was successfully created");
            try
            {
                while (true)
                {
                    log.info("Server is waiting for client connections");
                    Socket socket = serverSocket.accept();
                    log.info("Client {} connected", socket.getInetAddress());
                    try
                    {
                        Random random = new Random();
//                        int currentPosInIdList = 0;
                        while (!Thread.currentThread().isInterrupted())
                        {
                        	
                            int T = random.nextInt(PERIOD_WAIT_TIME) + 1;
                            try
                            {
                                Thread.sleep(T);
                            }
                            catch (InterruptedException e)
                            {
                                log.warn("Provider was interrupted when it was sleeping");
                                return;
                            }
//                            ProviderPackage providerPackage = new ProviderPackage(idList[currentPosInIdList],
//                                    random.nextInt(), providerName);
//                            currentPosInIdList = (currentPosInIdList + 1) % idList.length;
                            if (format == FORMAT_XML)
                                sendXmlObject(null, socket);
                            else
                                sendJsonObject(null, socket);
                        }
                    }
                    finally
                    {
                        socket.close();
                    }
                }
            }
            finally
            {
                serverSocket.close();
                log.info("ServerSocket was closed");
            }
        }
        catch (IOException e)
        {
            log.error("Error in ServerSocket creating", e);
            e.printStackTrace();
        }
    }
}
