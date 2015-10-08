package ru.splat.DesktopClient;


import java.sql.Timestamp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 *
 * @author Ekaterina
 *         <p>
 *         View of MVC
 */
public class View implements Observer
{
    private static final Logger log = LoggerFactory.getLogger(View.class);

    private Client client;
    
    private Shell shell;

    private Model model;
    
    private Table table;

    public ViewTable getViewTable()
    {
        return new ViewTable();
    }


    public View(Model model, Client client, Shell shell)
    {
        this.model = model;
        this.client = client;
        this.shell = shell;
    }


    @Override public void update()
    {
    	//????
       // view = model.getModel();
    	// repaint all?
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
        public void drawTable()
        {
        	if (table == null)
        		table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
            table.setLinesVisible(true);
            table.setHeaderVisible(true);
            table.clearAll();
            GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
            data.heightHint = 300;
            data.widthHint = 400;
            table.setLayoutData(data);
            String[] titles = { "time", "value" };
            for (int i = 0; i < titles.length; i++)
            {
                TableColumn column = new TableColumn(table, SWT.NONE);
                column.setText(titles[i]);
             //   System.out.print(i);
            }

            //the output values
            
            com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> modelTable = model.getModelTable();

            boolean found = false;
            for (Timestamp time : modelTable.row(model.getProviderType().ordinal()).keySet())
            {
                if (modelTable.row(model.getProviderType().ordinal()).get(time).getId() == model.getId())
                {
                	found = true;
                	TableItem item = new TableItem(table, SWT.NONE);
                    item.setText(0, "" + time);
                    item.setText(1, "" + modelTable.row(model.getProviderType().ordinal()).get(time).getValue());
                    log.info("Table with info from {} have been drawn", converProviderID(model.getProviderType().ordinal()));
                }
            }
            
            if (!found)
            {
            	TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, "" + new java.sql.Timestamp(new java.util.Date().getTime()));
                item.setText(1, "" + "History of Object with id = " + model.getId() + " is empty");
                log.info("History of Object with id = {} is Empty", model.getId());
            }

            for (int i = 0; i < titles.length; i++)
            {
                table.getColumn(i).pack();
            }

          //  client.setTable(table);
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
