package ru.splat.DesktopClient.controllers;


import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.swt.widgets.Shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.splat.DesktopClient.View;


/**
 * <p>
 *
 * @author Ekaterina
 *         Listener of menu item "Table"
 *         Draw table time|Value by SWT lib
 */
public class TableController implements SelectionListener
{
    private static final Logger log = LoggerFactory.getLogger(GraphController.class);

    View.ViewTable viewTable;

    Shell shell;


    /**
     * Constructor of TableController
     *
     * @param view
     */
    public TableController(View.ViewTable view, Shell shell)
    {
        this.viewTable = view;
        this.shell = shell;
    }


    /**
     * Draws a specific table values for a given object identifier
     *
     * @param selectionEvent Pressing the menu item "Table"
     */
    @Override public void widgetSelected(SelectionEvent selectionEvent)
    {
        viewTable.drawTable(shell);
    }


    @Override public void widgetDefaultSelected(SelectionEvent selectionEvent)
    {

    }

}
