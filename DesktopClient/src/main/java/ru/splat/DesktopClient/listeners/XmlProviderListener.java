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
 *         Makes a request for data coming from XmlProvider.
 */
public class XmlProviderListener implements SelectionListener {
    private static final Logger log = LoggerFactory.getLogger(XmlProviderListener.class);

    private Shell shell;
    private Label lblXmlprovider;
    private int providerId;

    public XmlProviderListener(Shell shlDesktopClient, Label lblXmlprovider, int provider) {
        this.shell = shlDesktopClient;
        this.lblXmlprovider = lblXmlprovider;
        this.providerId = provider;
    }

    @Override
    public void widgetSelected(SelectionEvent selectionEvent) {
        log.info("Item 'XmlProvider' pressed!");
        providerId = 0;
        log.info("Filed a request for information from Xml Provider!");
        lblXmlprovider.setText("                                         Data from Xml Provider");

    }

    @Override
    public void widgetDefaultSelected(SelectionEvent selectionEvent) {

    }
}
