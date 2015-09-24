package ru.splat.DesktopClient.listeners;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.splat.DesktopClient.Client;

import java.sql.Timestamp;


/**
 * <p/>
 *
 * @author Ekaterina
 *         Listener of menu item "Table"
 *         Draw table time|Value by SWT lib
 */
public class TableListener implements SelectionListener
{
    private static final Logger log = LoggerFactory.getLogger(GraphListener.class);

    private Shell shell;

    private Client client;

    private int providerId;


    /**
     * Constructor of TableListener
     *
     * @param shlDesktopClient shell of DC
     * @param provider         value of provider (0 - XmlProvider; 1 - JsonProvider)
     * @param client           object Client
     */
    public TableListener(Shell shlDesktopClient, int provider, Client client)
    {
        this.shell = shlDesktopClient;
        this.providerId = provider;
        this.client = client;
    }


    /**
     * Draws a specific table values for a given object identifier
     *
     * @param selectionEvent Pressing the menu item "Table"
     */
    @Override public void widgetSelected(SelectionEvent selectionEvent)
    {
        client.table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        client.table.setLinesVisible(true);
        client.table.setHeaderVisible(true);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 300;
        data.widthHint = 400;
        client.table.setLayoutData(data);
        String[] titles = { "time", "value" };
        for (int i = 0; i < titles.length; i++)
        {
            TableColumn column = new TableColumn(client.table, SWT.NONE);
            column.setText(titles[i]);
            System.out.print(i);
        }

        //the output values

        TableItem item = new TableItem(client.table, SWT.NONE);
        for (Timestamp time : client.Model.row(providerId).keySet())
        {
            if (client.Model.row(providerId).get(time).getId() == client.id)
            {

                item.setText(0, "" + time);
                item.setText(1, "" + client.Model.row(providerId).get(time).getValue());
                log.info("Table with info from {} have been drawn", converProviderID(client.providerId));
            }
            else
            {

                item.setText(0, "" + new java.sql.Timestamp(new java.util.Date().getTime()));
                item.setText(1, "" + "History of Object with id = " + client.id + " is Empty");
                log.info("History of Object with id = {} is Empty", client.id);
            }
        }

        for (int i = 0; i < titles.length; i++)
        {
            client.table.getColumn(i).pack();
        }

        shell.pack();
    }


    @Override public void widgetDefaultSelected(SelectionEvent selectionEvent)
    {

    }


    /**
     * Convert number of Provider to name of Provider
     *
     * @param providerId number of provider
     * @return name of provider
     */
    public String converProviderID(int providerId)
    {
        if (providerId == 0)
            return "XML Provider";
        else if (providerId == 1)
            return "Json ProVider";
        else
            return "Null";
    }
}
