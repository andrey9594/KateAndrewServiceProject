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


/**
 * <p>
 *
 * @author Ekaterina
 *         Draw table Value|id by SWT lib
 */
public class TableListener implements SelectionListener
{
    private static final Logger log = LoggerFactory.getLogger(GraphListener.class);

    private Shell shell;

    Client client;

    private int providerId;


    public TableListener(Shell shlDesktopClient, int provider)
    {
        this.shell = shlDesktopClient;
        this.providerId = provider;
    }


    @Override public void widgetSelected(SelectionEvent selectionEvent)
    {
        Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 300;
        data.widthHint = 300;
        table.setLayoutData(data);
        String[] titles = { "id", "value" };
        for (int i = 0; i < titles.length; i++)
        {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(titles[i]);
            System.out.print(i);
        }

        //the output values
        int count = 100;
        if (providerId == 0)
        {
            for (int i = 0; i < count; i++)
            {
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, "" + i); // output id
                item.setText(1, "" /* + get value*/); //output value
            }
            log.info("Table with info from Xml Provider have been drawn");
        }
        else if (providerId == 1)
        {
            for (int i = 0; i < count; i++)
            {
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, "" + i); // output id
                item.setText(1, "" /* + get value*/); //output value
            }
            log.info("Table with info from Json Provider have been drawn");
        }
        for (int i = 0; i < titles.length; i++)
        {
            table.getColumn(i).pack();
        }
        shell.pack();
    }


    @Override public void widgetDefaultSelected(SelectionEvent selectionEvent)
    {

    }
}
