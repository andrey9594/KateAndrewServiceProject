package ru.splat.DesktopClient.controllers;


import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.splat.DesktopClient.Model;
import ru.splat.DesktopClient.OperationType;
import ru.splat.DesktopClient.ProviderPackage;


/**
 * <p>
 *
 * @author Andrew & Ekaterina
 *         <p>
 *         Update Model, when new package come in
 */
public class ProcessPackageController
{
    private static final Logger log = LoggerFactory.getLogger(ProcessPackageController.class);

    private final int providerXMLID = 0;

    private final int providerJSONID = 1;

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
            model.addPackage(providerXMLID, new Timestamp(new java.util.Date().getTime()), providerPackage);
            model.notifyAllObserver(OperationType.ADDED, providerXMLID, providerPackage.getId());
        }
        else if (providerPackage.getProviderName().equals(providerJSONName))
        {
            model.addPackage(providerJSONID, new Timestamp(new java.util.Date().getTime()), providerPackage);
            model.notifyAllObserver(OperationType.ADDED, providerJSONID, providerPackage.getId());
        }


    }
}
