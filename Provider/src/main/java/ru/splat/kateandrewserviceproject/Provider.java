package ru.splat.kateandrewserviceproject;


import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * Class provider for transmitting data packages to the Service through a socket. Possible only one connection at once.
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

    private final File[] logFileList;

    private int currentFileInLogFileList = 0;


    /**
     * Constructor
     * 
     * @param propertiesPath Path to the config file
     */
    public Provider(String propertiesPath)
    {
        /**
         * properties: FORMAT = string: xml or json; PORT_NUM = int; PERIOD_TIME = int: send data every time interval;
         * The time interval will be in the random period: (0 ms, periodWaitTime ms]; PATH_TO_LOGS = string: where logs
         * are LOG_FORMAT: logs extension;
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

        logFileList = (new File(PATH_TO_LOGS)).listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.toString().endsWith("." + LOG_FORMAT);
            }
        });
        if (logFileList == null)
        {
            log.error("Directory with log files {1} was't found!", PATH_TO_LOGS);
            throw new NullPointerException();
        }
        if (logFileList.length == 0)
        {
            log.error("Log files was't found in {1}!", PATH_TO_LOGS);
            throw new NullPointerException();
        }

    }


    private void sendXmlObject(String xml, Socket socket)
    {
        log.debug("Got a xml {1} for sedning", xml);
        /**
         * xml starting with <tag attributes> or <tag>
         */
        StringBuilder s = new StringBuilder("");
        for (int j = 0; j < xml.length() && xml.charAt(j) != ' ' && xml.charAt(j) != '>'; j++)
            s.append(xml.charAt(j));
        /**
         * we only interested in event_list tag
         */
        if (!s.toString().equals("<event_list") && !s.toString().equals("<event_list>"))
            return;

        /**
         * sending here
         */
        try
        {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println(xml);
            pw.flush();
        }
        catch (IOException e)
        {
            log.error("Can't send xml {} on the xml protocol", xml, e);
            e.printStackTrace();
        }
        log.info("xml file {1} has been sent", xml);
    }


    private void sendJsonObject(String jsonString, Socket socket)
    {
        // log.debug("Start sending package with id {} on the protocol json", providerPackage.getId());
        // Gson gson = new Gson();
        // String json = gson.toJson(providerPackage);
        // try
        // {
        // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        // out.println(json);
        // out.flush();
        // }
        // catch (IOException e)
        // {
        // log.error("Error sending package with id {} on the protocol json", providerPackage.getId(), e);
        // e.printStackTrace();
        // }
        // log.debug("The package with id {} was successfully sent on the protocol json", providerPackage.getId());
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

            LogFormatter formatter = null;
            if (format == FORMAT_XML)
            {
                formatter = new LogFormatter(logFileList[currentFileInLogFileList++]); // not null ever
            }
            else if (format == FORMAT_JSON)
            {
                // formater =
                /**
                 * polymorphism: it means LogFormatter should be an interface or an abstract class for JSONLogFormatter,
                 * XMLLogFormatter
                 */
            }
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
                        // int currentPosInIdList = 0;
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
                            // ProviderPackage providerPackage = new ProviderPackage(idList[currentPosInIdList],
                            // random.nextInt(), providerName);
                            // currentPosInIdList = (currentPosInIdList + 1) % idList.length;
                            if (format == FORMAT_XML)
                            {
                                String xml = formatter.nextXML();
                                while (xml == null)
                                {
                                    if (currentFileInLogFileList >= logFileList.length)
                                    {
                                        log.info("All log files was procesed");
                                        return; // close socket and setverSocket in finnaly
                                    }
                                    /**
                                     * not null if log files not changed in the process
                                     */
                                    formatter = new LogFormatter(logFileList[currentFileInLogFileList++]);
                                    xml = formatter.nextXML();
                                }
                                sendXmlObject(xml, socket);
                            }
                            else
                            {
                                // sendJsonObject(null, socket);
                            }
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
