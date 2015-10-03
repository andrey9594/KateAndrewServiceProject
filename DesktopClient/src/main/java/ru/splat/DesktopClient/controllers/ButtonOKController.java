package ru.splat.DesktopClient.controllers;


import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.splat.DesktopClient.Model;


/**
 * <p>
 *
 * @author Ekaterina
 *         <p>
 *         Listener of "OK" button that reads and save the entered value of the object identifier
 */
public class ButtonOKController implements SelectionListener
{
    private static final Logger log = LoggerFactory.getLogger(JsonProviderController.class);

    private Model model;

    private Text text;


    /**
     * Constructor of ButtonOKController
     *
     * @param model object Model
     * @param text  text field on DC
     */
    public ButtonOKController(Model model, Text text)
    {
        this.model = model;
        this.text = text;
    }


    /**
     * Method reads and save the entered value of the object identifier
     *
     * @param selectionEvent Pressing the button "OK"
     */
    @Override public void widgetSelected(SelectionEvent selectionEvent)
    {
        if (!text.getText().isEmpty())
            model.setId(Integer.parseInt(text.getText()));
        log.info("The entered value of the object identifier has been read and saved");
    }


    @Override public void widgetDefaultSelected(SelectionEvent selectionEvent)
    {

    }
}
