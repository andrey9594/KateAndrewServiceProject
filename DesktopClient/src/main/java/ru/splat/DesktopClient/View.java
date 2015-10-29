package ru.splat.DesktopClient;


import java.sql.Timestamp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 *
 * @author Andrey & Ekaterina
 *         <p>
 *         View of MVC
 */
public class View implements Observer
{
    private static final Logger log = LoggerFactory.getLogger(View.class);

    private Shell shell;

    private Model model;

    private Table table;

    private ViewTable viewTable;


    public ViewTable getViewTable()
    {
        return viewTable = new ViewTable();
    }


    public View(Model model, Shell shell)
    {
        this.model = model;
        this.shell = shell;
    }


    @Override
    public void update(OperationType operation, int matchid)
    {
        if (operation == OperationType.ADDED)
        {
            if (this.viewTable != null)
            {
                Display.getDefault().asyncExec(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        viewTable.drawTable(matchid);
                    }
                });
            }
        }
        if (operation == OperationType.REMOVED)
        {

        }
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
        public void drawTable(int matchid)
        {
            /**
             * Cделать чтобы не вся таблица перересоывалась а столбцы занеси в массив
             * 
             */
            if (matchid == -1) 
            {
                // рисуем всю таблицу тогда
            }
            
            if (table == null)
            {
                table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
                table.setLinesVisible(true);
                table.setHeaderVisible(true);
            }
            GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
            data.heightHint = 300;
            data.widthHint = 400;
            table.setLayoutData(data);
            String[] titles = { "matchid", "sportType" };
            for (int i = 0; i < titles.length; i++)
            {
                TableColumn column = new TableColumn(table, SWT.NONE);
                column.setText(titles[i]);

            }

            // com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> modelTable = model.getModelTable();

            for (Integer matchId : model.getAllMatchid())
            {

                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, "" + matchId);
                item.setText(1, "" + model.getSportTypeForMatchid(matchId));
                log.info("Table with info have been drawn");

            }

            // if (!found)
            // {
            // TableItem item = new TableItem(table, SWT.NONE);
            // item.setText(0, "" + new java.sql.Timestamp(new java.util.Date().getTime()));
            // item.setText(1, "" + "History of Object with id = " + model.getId() + " is empty");
            // log.info("History of Object with id = {} is Empty", model.getId());
            // }

            for (int i = 0; i < titles.length; i++)
            {
                table.getColumn(i).pack();
            }

            shell.pack();
        }
    }


    /**
     * Draws a graph
     */
    class ViewGraph
    {

    }
}
