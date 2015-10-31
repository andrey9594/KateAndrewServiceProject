package ru.splat.DesktopClient;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import matchstatistic.Statistics;
import matchstatistic.sportstatistictypes.Football;
import matchstatistic.sportstatistictypes.StatisticType;


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

    private Display display;

    private Model model;

    private Table table;

    private ViewTable viewTable;


    public ViewTable getViewTable()
    {
        return viewTable = new ViewTable();
    }


    public View(Model model, Shell shell, Display display)
    {
        this.model = model;
        this.shell = shell;
        this.display = display;
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

        private TableItem selectedItem = null;

        private Shell subShell;


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

                table.addSelectionListener(new SelectionListener()
                {
                    @Override
                    public void widgetSelected(SelectionEvent e)
                    {
                        selectedItem = (TableItem) e.item;
                    }


                    @Override
                    public void widgetDefaultSelected(SelectionEvent e)
                    {

                    }
                });

                table.addListener(SWT.MouseDoubleClick, new Listener()
                {

                    @Override
                    public void handleEvent(Event event)
                    {
                        if (subShell == null)
                        {
                            subShell = new Shell(display);
                            subShell.addListener(SWT.Close, new Listener()
                            {
                                @Override
                                public void handleEvent(Event event)
                                {
                                    subShell.setVisible(false);
                                    event.doit = false;
                                }
                            });
                            subShell.open();
                        }
                        int currentLineMatchid = Integer.parseInt(selectedItem.getText(0));
                        Map <StatisticType, String> statisticValues = new HashMap<>();
                        switch (model.getSportTypeForMatchid(currentLineMatchid))
                        {
                            case FOOTBALL:
                                for (StatisticType st : Football.values()) {
                                    Statistics statistics = model.getStatisticForMatchid(currentLineMatchid, st);
                                    if (statistics != null)
                                    {
                                        String value1 = statistics.getValue1() == -1 ? "?" : "" + statistics.getValue1();
                                        String value2 = statistics.getValue2() == -1 ? "?" : "" + statistics.getValue2();
                                        statisticValues.put(st, value1 + "-" + value2);
                                    }
                                }

                                break;
                            case BASKETBALL:
                                
                                break;
                            case ICE_HOCKEY:
                                
                                break;
                            case VOLLEYBALL:
                                
                                break;
                            case HANDBALL:
                                
                                break;
                            default:
                                return;          
                        }
                        Table subTable = new Table(subShell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
                        subTable.setLinesVisible(true);
                        subTable.setHeaderVisible(true);
                        GridData subData = new GridData(SWT.FILL, SWT.FILL, true, true);
                        subTable.setLayoutData(subData);
                        for (StatisticType st : statisticValues.keySet())
                        {
                            TableColumn column = new TableColumn(subTable, SWT.NONE);
                            column.setText(statisticValues.get(st));
                        }
                        for (int i = 0; i < statisticValues.size(); i++)
                        {
                            subTable.getColumn(i).pack();
                        }
                        TableItem item = new TableItem(subTable, SWT.NONE);
                        item.setText(0, "hey!");
                        subShell.setVisible(true);
                        subShell.pack();
                    }
                });

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
