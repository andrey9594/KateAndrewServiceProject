package ru.splat.DesktopClient;


import com.google.common.collect.TreeBasedTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;


/**
 * <p>
 *
 * @author Ekaterina
 *         <p>
 *         View of MVC
 */
public class View implements Observer
{
    private static final Logger log = LoggerFactory.getLogger(Model.class);


    public com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> view = TreeBasedTable.create();

    Client client;

    Model model = new Model();


    public View(Model model)
    {
        this.model = model;
    }


    public ViewTable getViewTable()
    {
        return new ViewTable();
    }


    public View(Model model, Client client)
    {

        this.client = client;
        this.model = model;
    }


    @Override public void update()
    {
        view = model.getModel();
    }


    /**
     * Draws a table
     */
    public class ViewTable
    {
        /**
         * Draws a specific table values for a given object identifier
         *
         * @param shell shell of DC
         */
        public void drawTable(Shell shell)
        {
        	
        	
            Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
            table.setLinesVisible(true);
            table.setHeaderVisible(true);
            GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
            data.heightHint = 300;
            data.widthHint = 400;
            table.setLayoutData(data);
            String[] titles = { "time", "value" };
            for (int i = 0; i < titles.length; i++)
            {
                TableColumn column = new TableColumn(table, SWT.NONE);
                column.setText(titles[i]);
                System.out.print(i);
            }

            //the output values

            TableItem item = new TableItem(table, SWT.NONE);
            for (Timestamp time : view.row(model.getProviderId()).keySet())
            {
                if (view.row(model.getProviderId()).get(time).getId() == model.getId())
                {

                    item.setText(0, "" + time);
                    item.setText(1, "" + view.row(model.getProviderId()).get(time).getValue());
                    log.info("Table with info from {} have been drawn", converProviderID(model.getProviderId()));
                }
                else
                {

                    item.setText(0, "" + new java.sql.Timestamp(new java.util.Date().getTime()));
                    item.setText(1, "" + "History of Object with id = " + model.getId() + " is Empty");
                    log.info("History of Object with id = {} is Empty", model.getId());
                }
            }

            for (int i = 0; i < titles.length; i++)
            {
                table.getColumn(i).pack();
            }

            client.setTable(table);
            shell.pack();
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


    /**
     * Draws a graph
     */
    class ViewGraph
    {

    }
}
