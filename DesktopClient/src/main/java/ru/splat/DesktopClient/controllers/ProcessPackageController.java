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
    Model model = new Model();

    private static final Logger log = LoggerFactory.getLogger(ProcessPackageController.class);


    /**
     * Recordes data, when new packege come
     *
     * @param providerPackage latest packege, which we receved.
     */

    public void processPackage(ProviderPackage providerPackage, Model model)
    {

        if (providerPackage.getProviderName().equals("providerxml"))
        {
            model.model.rowMap();
            model.model.row(0).put(new java.sql.Timestamp(new java.util.Date().getTime()), providerPackage);
        }
        else if (providerPackage.getProviderName().equals("providerjson"))
        {
            model.model.rowMap();
            model.model.row(1).put(new java.sql.Timestamp(new java.util.Date().getTime()), providerPackage);
        }

        log.debug("Data from new packege have been record");

        model.notifyAllObserver();
    }
}
