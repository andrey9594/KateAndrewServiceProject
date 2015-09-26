package ru.splat.DesktopClient.controllers;


import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.splat.DesktopClient.Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * <p/>
 *
 * @author Ekaterina
 *         Listener of menu item "Graph"
 *         Draw graph Value(t) by SWT lib
 */
public class GraphController implements SelectionListener
{
    private static final Logger log = LoggerFactory.getLogger(GraphController.class);

    private int providerId;

    private Shell shell;

    private GC gc;

    private Client client;

    private static Long MINUTE;

    private static Long DELTA;

    private static int LEBELX;

    private static int LEBELY;


    /**
     * Constructor of GraphController
     *
     * @param shlDesktopClient shell of DC
     * @param provider         value of provider (0 - XmlProvider; 1 - JsonProvider)
     * @param client           object Client
     */
    public GraphController(Shell shlDesktopClient, int provider, Client client)
    {
        this.shell = shlDesktopClient;
        this.providerId = provider;
        this.client = client;
    }


    /**
     * Converting 'time' and 'values' to the coordinate axes,
     * Draws a graph for values of given object identifier
     *
     * @param selectionEvent Pressing the menu item "Graph"
     */
    @Override public void widgetSelected(SelectionEvent selectionEvent)
    {
        shell.addPaintListener(new PaintListener()
                               {

                                   @Override public void paintControl(PaintEvent paintEvent)
                                   {
                                       try
                                       {
                                           GetConfigGraph();
                                           log.info("value has been converted");
                                       }
                                       catch (IOException e)
                                       {
                                           log.error("Error in reading file 'configGraph.ini'");
                                           e.printStackTrace();
                                       }
                                       paintEvent.gc.drawLine(20, 35, 20, 425);//Y(Value)
                                       paintEvent.gc.drawLine(20, 35, 25, 50);
                                       paintEvent.gc.drawLine(20, 35, 15, 50);
                                       paintEvent.gc.drawString("Value", 30, 33);

                                       paintEvent.gc.drawLine(20, 225, 490, 225);//X(t)
                                       paintEvent.gc.drawLine(473, 220, 490, 225);
                                       paintEvent.gc.drawLine(473, 230, 490, 225);
                                       paintEvent.gc.drawString("t(sec)", 433, 195);

                                       paintEvent.gc.drawLine(15, 65, 25, 65);
                                       paintEvent.gc.drawString("2^31", 30, 55);
                                       paintEvent.gc.drawLine(15, 385, 25, 385);
                                       paintEvent.gc.drawString("-2^31", 30, 375);
                                       paintEvent.gc.drawLine(440, 220, 440, 230);
                                       paintEvent.gc.drawString("60", 433, 235);

                                       paintEvent.gc.drawString("0", 5, 215);

                                       for (int i = 1; i < 60; i++)
                                       {
                                           paintEvent.gc.drawLine(20 + i * 7, 220, 20 + i * 7, 230);
                                       }

                                       for (int i = 1; i < 8; i++)
                                       {
                                           paintEvent.gc.drawLine(15, 65 + i * 40, 25, 65 + i * 40);
                                       }

                                       paintEvent.gc.setLineWidth(5);
                                       for (int i = 1; i < 7; i++)
                                       {
                                           paintEvent.gc.drawLine(20 + i * 70, 220, 20 + i * 70, 230);
                                       }

                                       for (int i = 0; i < 5; i++)
                                       {
                                           paintEvent.gc.drawLine(15, 65 + i * 80, 25, 65 + i * 80);
                                       }

                                       paintEvent.gc.drawString("2^30", 30, 135);
                                       paintEvent.gc.drawString("2^30", 30, 295);

                                       for (int i = 1; i < 6; i++)
                                       {
                                           paintEvent.gc.drawString(i + "0", 12 + i * 70, 235);
                                       }

                                       //Todo Make OO draw a Graph
                                       // converting 'time' and 'values' to the coordinate axes
                                       //                                       ArrayList<Integer> coordinatesArrayTimeXml = new ArrayList<Integer>();
                                       //                                       ArrayList<Integer> coordinatesArrayValueXml = new ArrayList<Integer>();
                                       //                                       ArrayList<Integer> coordinatesArrayTimeJson = new ArrayList<Integer>();
                                       //                                       ArrayList<Integer> coordinatesArrayValueJson = new ArrayList<Integer>();

                                       //                                       int c;
                                       //                                       if ((client.providerId == 0) && (client.weightedGraphXml.containsRow(client.id)))
                                       //                                       {
                                       //                                           for (Timestamp time : client.weightedGraphXml.row(client.id).keySet())
                                       //                                           {
                                       //                                               c = 20;
                                       //                                               for (Long i = 0L; i < MINUTE; i += DELTA)
                                       //                                               {
                                       //                                                   Timestamp ts = new Timestamp(
                                       //                                                           new java.util.Date().getTime() - MINUTE + i);
                                       //                                                   Timestamp ts1 = new Timestamp(
                                       //                                                           new java.util.Date().getTime() - (MINUTE - DELTA) + i);
                                       //                                                   if (ts.compareTo(time) == 0)
                                       //                                                   {
                                       //                                                       coordinatesArrayTimeXml.add(c);
                                       //                                                       coordinatesArrayValueXml.add(convertValueToCoordinates(
                                       //                                                               client.weightedGraphXml.row(client.id).get(time)));
                                       //
                                       //                                                   }
                                       //                                                   else if ((ts.compareTo(time) == -1) && (ts1.compareTo(time) == 1))
                                       //                                                   {
                                       //                                                       coordinatesArrayTimeXml.add(c + 7);
                                       //                                                       coordinatesArrayValueXml.add(convertValueToCoordinates(
                                       //                                                               client.weightedGraphXml.row(client.id).get(time)));
                                       //                                                   }
                                       //                                                   c += 14;
                                       //                                               }
                                       //                                           }
                                       //                                           log.info(
                                       //                                                   "'time' and 'values' from Xml Provider has been converted to the coordinate axes");
                                       //                                       }
                                       //                                       else if ((client.providerId == 1) && (client.weightedGraphJson
                                       //                                               .containsRow(client.id)))
                                       //                                       {
                                       //                                           for (Timestamp time : client.weightedGraphJson.row(client.id).keySet())
                                       //                                           {
                                       //                                               c = 20;
                                       //                                               for (Long i = 0L; i < MINUTE; i += DELTA)
                                       //                                               {
                                       //                                                   Timestamp ts = new Timestamp(
                                       //                                                           new java.util.Date().getTime() - MINUTE + i);
                                       //                                                   Timestamp ts1 = new Timestamp(
                                       //                                                           new java.util.Date().getTime() - (MINUTE - DELTA) + i);
                                       //                                                   if (ts.compareTo(time) == 0)
                                       //                                                   {
                                       //                                                       coordinatesArrayTimeJson.add(c);
                                       //                                                       coordinatesArrayValueJson.add(convertValueToCoordinates(
                                       //                                                               client.weightedGraphJson.row(client.id).get(time)));
                                       //                                                   }
                                       //                                                   else if ((ts.compareTo(time) == -1) && (ts1.compareTo(time) == 1))
                                       //                                                   {
                                       //                                                       coordinatesArrayTimeJson.add(c + 7);
                                       //                                                       coordinatesArrayValueJson.add(convertValueToCoordinates(
                                       //                                                               client.weightedGraphJson.row(client.id).get(time)));
                                       //                                                   }
                                       //                                                   c += 14;
                                       //                                               }
                                       //                                           }
                                       //                                           log.info(
                                       //                                                   "'time' and 'values' from Json Provider has been converted to the coordinate axes");
                                       //                                       }

                                       //Draw a graph
                                       //                                       paintEvent.gc.setLineWidth(5);
                                       //                                       if ((client.providerId == 0) && (coordinatesArrayTimeXml.size() >= 2))
                                       //                                       {
                                       //                                           for (int i = 0; i < coordinatesArrayTimeXml.size() - 1; i++)
                                       //                                           {
                                       //                                               paintEvent.gc.drawLine(coordinatesArrayTimeXml.get(i),
                                       //                                                       coordinatesArrayValueXml.get(i),
                                       //                                                       coordinatesArrayTimeXml.get(i + 1),
                                       //                                                       coordinatesArrayValueXml.get(i + 1));
                                       //                                           }
                                       //                                           paintEvent.gc.drawString("Data from Xml Provider", LEBELX, LEBELY);
                                       //                                           log.info("Graph with info from Xml Provider have been drawn");
                                       //                                       }
                                       //                                       else if ((client.providerId == 0) && (coordinatesArrayTimeXml.size() == 1))
                                       //                                       {
                                       //                                           paintEvent.gc.drawPoint(coordinatesArrayTimeXml.get(0),
                                       //                                                   coordinatesArrayValueXml.get(0));
                                       //                                           paintEvent.gc.drawString("Data from Xml Provider", LEBELX, LEBELY);
                                       //                                           log.info("Graph with info from Xml Provider have been drawn");
                                       //                                       }
                                       //                                       else if ((client.providerId == 1) && (coordinatesArrayTimeJson.size() >= 2))
                                       //                                       {
                                       //                                           for (int i = 0; i < coordinatesArrayTimeJson.size() - 1; i++)
                                       //                                           {
                                       //                                               paintEvent.gc.drawLine(coordinatesArrayTimeJson.get(i),
                                       //                                                       coordinatesArrayValueJson.get(i),
                                       //                                                       coordinatesArrayTimeJson.get(i + 1),
                                       //                                                       coordinatesArrayValueJson.get(i + 1));
                                       //                                           }
                                       //                                           paintEvent.gc.drawString("Data from Json Provider", LEBELX, LEBELY);
                                       //                                           log.info("Graph with info from Json Provider have been drawn");
                                       //                                       }
                                       //                                       else if ((client.providerId == 1) && (coordinatesArrayTimeJson.size() == 1))
                                       //                                       {
                                       //                                           paintEvent.gc.drawPoint(coordinatesArrayTimeJson.get(0),
                                       //                                                   coordinatesArrayValueJson.get(0));
                                       //                                           paintEvent.gc.drawString("Data from Json Provider", LEBELX, LEBELY);
                                       //                                           log.info("Graph with info from Json Provider have been drawn");
                                       //                                       }
                                       //                                       else
                                       //                                       {
                                       //                                           if (client.providerId == 0)
                                       //                                           {
                                       //                                               paintEvent.gc.drawString("Data from Xml Provider", LEBELX, LEBELY);
                                       //                                           }
                                       //                                           if (client.providerId == 1)
                                       //                                           {
                                       //                                               paintEvent.gc.drawString("Data from Json Provider", LEBELX, LEBELY);
                                       //                                           }
                                       //                                           paintEvent.gc
                                       //                                                   .drawString("History of Object with id = " + client.id + " is Empty",
                                       //                                                           160, 100);
                                       //                                           log.info("History of Object with id = {} is Empty", client.id);
                                       //                                       }

                                   }

                               }

        );
        shell.setSize(550, 520);
    }


    @Override public void widgetDefaultSelected(SelectionEvent selectionEvent)
    {
    }


    public void GetConfigGraph() throws IOException
    {
        log.info("Take parameters from configGraph.ini file");
        Properties props = new Properties();

        props.load(new FileInputStream(new File("configGraph.ini")));

        MINUTE = Long.valueOf(props.getProperty("MINUTE"));
        DELTA = Long.valueOf(props.getProperty("DELTA"));
        LEBELX = Integer.valueOf(props.getProperty("LEBELX"));
        LEBELY = Integer.valueOf(props.getProperty("LEBELY"));
    }


    /**
     * Convert Value to coordinates
     *
     * @param value positive or negative value, which must convert to coordinates
     * @return Value in Coordinates
     */
    private int convertValueToCoordinates(int value)
    {
        log.info("value is converting");
        return (value / (-13750000) + 255);
    }

}
