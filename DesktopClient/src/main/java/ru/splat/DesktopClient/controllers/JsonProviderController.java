package ru.splat.DesktopClient.controllers;


import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.splat.DesktopClient.Model;


/**
 * <p>
 *
 * @author Ekaterina
 *         Listener of menu item "JsonProvider"
 *         Makes a request for data coming from JsonProvider.
 */
public class JsonProviderController implements SelectionListener
{
    private static final Logger log = LoggerFactory.getLogger(JsonProviderController.class);

    private Shell shell;

    private Label lblJsonprovider;

    private Model model;

    /**
     * Constructor of JsonProviderController
     *
     * @param shlDesktopClient shell of DC
     * @param lblJsonprovider  label of DC, on which displays the name of the provider,
     *                         the data from which are displayed on the graph / table
     * @param model            object Model
     */
    public JsonProviderController(Shell shlDesktopClient, Label lblJsonprovider, Model model)
    {
        this.shell = shlDesktopClient;
        this.lblJsonprovider = lblJsonprovider;
        this.model = model;
    }

    /**
     * Method sets the value of "providerId" = 1 and displays the inscription "Data from Json Provider"
     *
     * @param selectionEvent Pressing the menu item "JsonProvider"
     */

    @Override public void widgetSelected(SelectionEvent selectionEvent)
    {
        log.info("Item 'JsonProvider' pressed!");
        model.setProviderId(1);
        log.info("Filed a request for information from Json Provider!");
        lblJsonprovider.setText("                                         Data from Json Provider");
    }


    @Override public void widgetDefaultSelected(SelectionEvent selectionEvent)
    {

    }
}
