package ru.splat.DesktopClient.listeners;


import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 *
 * @author Ekaterina
 *         Makes a request for data coming from JsonProvider.
 */
public class JsonProviderListener implements SelectionListener
{
    private static final Logger log = LoggerFactory.getLogger(JsonProviderListener.class);

    private Shell shell;

    private Label lblJsonprovider;

    private int providerId;


    public JsonProviderListener(Shell shlDesktopClient, Label lblJsonprovider, int provider)
    {
        this.shell = shlDesktopClient;
        this.lblJsonprovider = lblJsonprovider;
        this.providerId = provider;
    }


    @Override public void widgetSelected(SelectionEvent selectionEvent)
    {
        log.info("Item 'JsonProvider' pressed!");
        providerId = 1;
        log.info("Filed a request for information from Json Provider!");
        lblJsonprovider.setText("                                         Data from Json Provider");
    }


    @Override public void widgetDefaultSelected(SelectionEvent selectionEvent)
    {

    }
}
