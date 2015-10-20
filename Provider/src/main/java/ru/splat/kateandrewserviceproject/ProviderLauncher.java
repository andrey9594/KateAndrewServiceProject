package ru.splat.kateandrewserviceproject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * Class for launching several Providers
 * 
 * @author andrey
 */
public class ProviderLauncher
{
    static private final Logger log = LoggerFactory.getLogger(ProviderLauncher.class);


    /**
     * <p>
     * Main method for launching several Providers
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        log.info("Starting xml provider...");
        new Thread()
        {
            public void run()
            {
                Provider xmlProvider = new Provider("resources/config_xml.properties");
                xmlProvider.start();
            }
        }.start();
        log.info("Xml provider has been started");

        // log.info("Starting json provider...");
        // new Thread()
        // {
        // public void run()
        // {
        // Provider jsonProvider = new Provider("resources/config_json.properties");
        // jsonProvider.start();
        // }
        // }.start();
        // log.info("Json provider has been started");
    }
}
