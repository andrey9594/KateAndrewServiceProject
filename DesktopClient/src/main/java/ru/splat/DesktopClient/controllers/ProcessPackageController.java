package ru.splat.DesktopClient.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.splat.DesktopClient.Client;
import ru.splat.DesktopClient.Model;
import ru.splat.DesktopClient.ProviderPackage;


/**
 * <p>
 *
 * @author Ekaterina
 *         <p>
 *         Update Model, when new packege come
 */
public class ProcessPackageController
{
    static Model model;
    private static final Logger log = LoggerFactory.getLogger(ProcessPackageController.class);


    /**
     * Recordes data, when new packege come
     *
     * @param providerPackage latest packege, which we receved.
     */

    public static void processPackageController(ProviderPackage providerPackage)
    {
        if (providerPackage.getProviderName().equals("providerxml"))
        {
            model.Model.rowMap();
            model.Model.row(0).put(new java.sql.Timestamp(new java.util.Date().getTime()), providerPackage);
        }
        else if (providerPackage.getProviderName().equals("providerjson"))
        {
            model.Model.rowMap();
            model.Model.row(1).put(new java.sql.Timestamp(new java.util.Date().getTime()), providerPackage);
        }

        log.debug("Data from new packege have been record");
    }
}
