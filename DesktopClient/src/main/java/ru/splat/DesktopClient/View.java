package ru.splat.DesktopClient;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
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
                        /**
                         * redraw only one line
                         */
                        viewTable.drawTable(matchid);
                    }
                });
            }
        }
        if (operation == OperationType.REMOVED)
        {
            /**
             * we don't need it yet
             */
        }
    }


    /**
     * Draws a table
     */
    public class ViewTable
    {
        private Map<Integer, TableItem> matchidItemMap = new HashMap<>();


        private void updateItem(int matchid, TableItem item)
        {
            item.setText(0, "" + matchid);
            item.setText(1, "" + model.getSportTypeForMatchid(matchid));
            String team1Name = model.getTeam1NameForMatchid(matchid);
            if (team1Name == null)
                team1Name = "?";
            String team2Name = model.getTeam2NameForMatchid(matchid);
            if (team2Name == null)
                team2Name = "?";
            item.setText(2, "" + team1Name);
            item.setText(3, "" + team2Name);
            item.setText(4, "?");
            item.setText(5, "?");
            matchidItemMap.put(matchid, item);
            log.info("Table with info have been drawn");
        }


        /**
         * Draws a specific table values for a given object identifier
         *
         * @param matchid Matchid for match or -1 for redrawing all the table
         */
        public void drawTable(int matchid)
        {
            if (table == null)
            {
                table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
                table.setLinesVisible(true);
                table.setHeaderVisible(true);

                GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
                // data.heightHint = 300;
                // data.widthHint = 400;
                table.setLayoutData(data);
                String[] titles = { "matchid", "sportType", "team1Name", "team2Name", "score", "isFinished" };
                for (int i = 0; i < titles.length; i++)
                {
                    TableColumn column = new TableColumn(table, SWT.NONE);
                    column.setText(titles[i]);
                }
                for (int i = 0; i < titles.length; i++)
                {
                    table.getColumn(i).pack();
                }
            }
            /**
             * if @matchid == -1 then redraw all the table else redraw only one line for match with that matchid
             */
            if (matchid == -1)
            {
                // com.google.common.collect.Table<Integer, Timestamp, ProviderPackage> modelTable =

                for (Integer matchId : model.getAllMatchid())
                {
                    TableItem item = new TableItem(table, SWT.NONE);
                    updateItem(matchId, item);
                }
            }
            else
            {
                TableItem item = null;
                if (!matchidItemMap.containsKey(matchid))
                {
                    item = new TableItem(table, SWT.NONE);
                    matchidItemMap.put(matchid, item);
                    log.info("Item with matchid = {} added", matchid);

                }
                else
                {
                    item = matchidItemMap.get(matchid);
                }

                updateItem(matchid, item);
                log.info("Item with matchid = {} updated", matchid);

            }
            shell.pack(); // ?????????????????????
        }
    }


    /**
     * Draws a graph
     */
    class ViewGraph
    {

    }
}
