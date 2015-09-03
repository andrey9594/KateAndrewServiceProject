package ru.splat.DesktopClient;


import com.google.common.collect.*;
import com.google.gson.Gson;
import com.rabbitmq.client.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.splat.DesktopClient.listeners.GraphListener;
import ru.splat.DesktopClient.listeners.JsonProviderListener;
import ru.splat.DesktopClient.listeners.TableListener;
import ru.splat.DesktopClient.listeners.XmlProviderListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;

import java.util.Properties;
import java.util.concurrent.TimeoutException;


/**
 * <p/>
 *
 * @author Ekaterina
 *         Desktop client
 *         Take data from Service(by RabbitMQ) and make desktop window in which displays
 *         the history of changes in the state of object in two forms(table and graph)
 */

public class Client
{

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private static String IP_BROKER;

    private static String EXCHANGE_NAME;

    private static String TYPE_OF_EXCHANGE;

    private boolean isRunning;

    private static volatile Client instance;

    public com.google.common.collect.Table<Integer, Timestamp, Integer> weightedGraphXml = TreeBasedTable.create();

    public com.google.common.collect.Table<Integer, Timestamp, Integer> weightedGraphJson = TreeBasedTable.create();

    public int providerId = 0; // 0 - xml; 1 - json

    public Table table;

    private static Text text;


    public Client()
    {
    }


    /**
     * take parameters from config.ini file
     */
    public void config() throws IOException
    {
        log.info("Take parameters from config.ini file");
        Properties props = new Properties();

        props.load(new FileInputStream(new File("config.ini")));

        IP_BROKER = props.getProperty("IP_BROKER");
        EXCHANGE_NAME = props.getProperty("EXCHANGE_NAME");
        TYPE_OF_EXCHANGE = props.getProperty("TYPE_OF_EXCHANGE");
    }


    /**
     * Singleton Client
     */
    public static Client getInstance()
    {
        if (instance == null)
        {
            synchronized (Client.class)
            {
                if (instance == null)
                    instance = new Client();
            }
        }
        return instance;
    }


    /**
     * Start point
     */
    public void start() throws IOException, TimeoutException
    {
        if (isRunning)
            return;
        config();
        createAndStartConsumer();
        createAndStartGUI();
        isRunning = true;
    }


    /**
     * method create and start consumer
     */
    private void createAndStartConsumer() throws IOException, TimeoutException
    {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP_BROKER);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, TYPE_OF_EXCHANGE);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        Consumer consumer = new DefaultConsumer(channel)
        {
            private Gson gson = new Gson();


            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                    AMQP.BasicProperties properties, byte[] body)
                    throws IOException
            {
                String jsonString = new String(body, "UTF-8");
                ProviderPackage providerPackage = gson.fromJson(jsonString, ProviderPackage.class);
                processPackage(providerPackage);
            }
        };
        channel.basicConsume(queueName, true, consumer);
        log.info("Consumer have been created and started ");
    }


    /**
     * Updates data, when new packege come
     *
     * @param providerPackage latest packege, which we receved.
     */
    private void processPackage(ProviderPackage providerPackage)
    {
        updateData(providerPackage.getId(), providerPackage.getValue(), providerPackage.getProviderName());
        log.debug("Data from new packege have been updated");
    }


    /**
     * Update data in Table(Guava structure)
     *
     * @param idNew       id  identifier of Object, which we received
     * @param valueNew    value positive or negative value, which must be added to Object history
     * @param providerNew provider name ("providerxml" if data received from XMl provider or
     *                    "providerjson" if data received from Json provider)
     */
    private void updateData(int idNew, int valueNew, String providerNew)
    {
        if (providerNew.equals("providerxml"))
        {
            if (weightedGraphXml.containsRow(idNew))
            {
                weightedGraphXml.rowMap();
                weightedGraphXml.row(idNew).put(new java.sql.Timestamp(new java.util.Date().getTime()), valueNew);
                log.debug("Table 'weightedGraphXml' with id = {} is updated", idNew);
            }
            else
            {
                weightedGraphXml.put(idNew, new java.sql.Timestamp(new java.util.Date().getTime()), valueNew);
                log.debug("New record  with id = {} is created in Table 'weightedGraphXml'", idNew);
            }
        }
        else if (providerNew.equals("providerjson"))
        {
            if (weightedGraphJson.containsRow(idNew))
            {
                weightedGraphJson.rowMap();
                weightedGraphJson.row(idNew).put(new java.sql.Timestamp(new java.util.Date().getTime()), valueNew);
                log.debug("Table 'weightedGraphJson' with id = {} is updated", idNew);
            }
            else
            {
                weightedGraphJson.put(idNew, new java.sql.Timestamp(new java.util.Date().getTime()), valueNew);
                log.debug("New record  with id = {} is created in Table 'weightedGraphJson'", idNew);
            }
        }
    }


    /**
     * create and start GUI(draw new window with menu)
     */
    private void createAndStartGUI()
    {

        Display display = new Display();
        log.info("Display was created");

        Shell shlDesktopClient = new Shell(display);
        Image bgImg = new Image(display, "resources/BackgroundImage.jpg");
        shlDesktopClient.setText("Desktop Client on SWT");
        GridLayout gl_shlDesktopClient = new GridLayout();
        shlDesktopClient.setLayout(gl_shlDesktopClient);
        Image icon = new Image(display, "resources/icon.jpg");
        shlDesktopClient.setImage(icon);
        shlDesktopClient.setBackgroundImage(bgImg);
        shlDesktopClient.setBackgroundMode(SWT.INHERIT_FORCE);

        Label lblprovider = new Label(shlDesktopClient, SWT.NONE);
        lblprovider.setText("");

        Menu menu = new Menu(shlDesktopClient, SWT.BAR);
        shlDesktopClient.setMenuBar(menu);

        MenuItem mProvider = new MenuItem(menu, SWT.CASCADE);
        mProvider.setText("Provider");

        Menu menu_Provider = new Menu(mProvider);
        mProvider.setMenu(menu_Provider);

        MenuItem mXmlProvider = new MenuItem(menu_Provider, SWT.NONE);
        mXmlProvider.setText("Xml Provider");

        MenuItem mJsonProvider = new MenuItem(menu_Provider, SWT.NONE);
        mJsonProvider.setText("Json Provider");

        MenuItem mView = new MenuItem(menu, SWT.CASCADE);
        mView.setText("View");

        Menu menu_View = new Menu(mView);
        mView.setMenu(menu_View);

        MenuItem mTable = new MenuItem(menu_View, SWT.NONE);
        mTable.setText("Table");

        MenuItem mGraph = new MenuItem(menu_View, SWT.NONE);
        mGraph.setText("Graph");

        mGraph.addSelectionListener(new GraphListener(shlDesktopClient, providerId, this));
        mTable.addSelectionListener(new TableListener(shlDesktopClient, providerId, this));
        mXmlProvider.addSelectionListener(new XmlProviderListener(shlDesktopClient, lblprovider, this));
        mJsonProvider.addSelectionListener(new JsonProviderListener(shlDesktopClient, lblprovider, this));

        shlDesktopClient.open();
        log.info("Start window have been drawn");

        while (!shlDesktopClient.isDisposed())
        {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
        log.info("Display was disposed");
    }

}

