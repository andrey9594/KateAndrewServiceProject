package ru.splat.DesktopClient.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.splat.DesktopClient.*;


/**
 * <p>
 *
 * @author Ekaterina
 *         <p>
 *         Update Model, when new packege come
 */
public class ProcessPackageController
{
    private static final Logger log = LoggerFactory.getLogger(ProcessPackageController.class);

    private final String providerXMLName = "providerxml";
    private final String providerJSONName = "providerjson";

    /**
     * Write data, when new package come
     *
     * @param providerPackage latest package, which we received.
     */
    public void processPackage(ProviderPackage providerPackage, Model model)
    {
    	log.info("Got new package: {}" + providerPackage);
        if (providerPackage.getProviderName().equals(providerXMLName))
        {
            //model.modelTable.rowMap();
            model.getModelTable().row(0).put(new java.sql.Timestamp(new java.util.Date().getTime()), providerPackage);
        }
        else if (providerPackage.getProviderName().equals(providerJSONName))
        {
            //model.modelTable.rowMap();
            model.getModelTable().row(1).put(new java.sql.Timestamp(new java.util.Date().getTime()), providerPackage);
        }

        log.info("Data from new package have been written");

        model.notifyAllObserver();
    }
}
