package ru.splat.DesktopClient.controllers;


import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.splat.DesktopClient.Model;
import ru.splat.DesktopClient.ProviderType;


/**
 * <p>
 *
 * @author Ekaterina
 *         Listener of menu item "XmlProvider"
 *         Makes a request for data coming from XmlProvider.
 */
public class XmlProviderController implements SelectionListener
{
    private static final Logger log = LoggerFactory.getLogger(XmlProviderController.class);

    private Label lblXmlprovider;

    private Model model;


    /**
     * Constructor of XmlProviderController
     *
     * @param lblXmlprovider   label of DC, on which displays the name of the provider,
     *                         the data from which are displayed on the graph / table
     * @param model            object Model
     */
    public XmlProviderController(Label lblXmlprovider, Model model)
    {
        this.lblXmlprovider = lblXmlprovider;
        this.model = model;
    }


    /**
     * Method sets the value of "providerId" = 0 and displays the inscription "Data from Xml Provider"
     *
     * @param selectionEvent Pressing the menu item "XmlProvider"
     */
    @Override public void widgetSelected(SelectionEvent selectionEvent)
    {
        log.info("Item 'XmlProvider' pressed!");
        model.setProviderType(ProviderType.PROVIDER_XML);
        log.info("Filed a request for information from Xml Provider!");
        lblXmlprovider.setText("                                         Data from Xml Provider");
    }


    @Override public void widgetDefaultSelected(SelectionEvent selectionEvent)
    {

    }
}
