package ru.splat.DesktopClient.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.splat.DesktopClient.MatchStatisticsDelta;
import ru.splat.DesktopClient.Model;
import ru.splat.DesktopClient.OperationType;


/**
 * <p>
 *
 * @author Andrey & Ekaterina
 *         <p>
 *         Update Model, when new package come in
 */
public class ProcessPackageController
{
    private static final Logger log = LoggerFactory.getLogger(ProcessPackageController.class);


    /**
     * Writes data, when new package come in
     *
     * @param providerPackage the package, which we received.
     */
    public void processPackage(MatchStatisticsDelta matchStatiticsDelta, Model model)
    {
        log.info("Got new object of matchStatisticsDelta with matchid = " + matchStatiticsDelta.getMatchid());

        model.addPackage(matchStatiticsDelta);
        model.notifyAllObserver(OperationType.ADDED, matchStatiticsDelta.getMatchid());
    }
}
