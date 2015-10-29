package ru.splat.DesktopClient;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import matchstatistic.sportstatistictypes.StatisticType;
import ru.splat.DesktopClient.controllers.ButtonOKController;
import ru.splat.DesktopClient.controllers.JsonProviderController;
import ru.splat.DesktopClient.controllers.ProcessPackageController;
import ru.splat.DesktopClient.controllers.TableController;
import ru.splat.DesktopClient.controllers.XmlProviderController;


/**
 * <p>
 *
 * @author Andrey & Ekaterina Desktop client Take data from Service(by RabbitMQ) and make desktop window in which
 *         displays the history of changes in the state of object in various forms(table, graph, etc)
 */

public class Client
{

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private static String IP_BROKER;

    private static String EXCHANGE_NAME;

    private static String TYPE_OF_EXCHANGE;

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private static volatile Client instance;

    private static Text text;

    private final Model model;

    private final View view;

    private final View.ViewTable viewTable;

    private final ProcessPackageController processPackageController;

    private Shell shlDesktopClient;

    private Display display;


    private Client()
    {
        log.debug("Creating model...");
        model = new Model();
        log.debug("Model was created");

        log.info("Creating display...");
        display = new Display();
        log.info("Display was created");
        shlDesktopClient = new Shell(display);

        log.debug("Creating view");
        view = new View(model, shlDesktopClient);

        log.debug("VIew was created");

        model.registerObserver(view);
        viewTable = view.getViewTable();

        log.debug("Creating an object of ProcessPackageController...");
        processPackageController = new ProcessPackageController();
        log.debug("The object of ProcessPackageController was created");
    }


    /**
     * take parameters from config.ini file
     */
    public void config() throws IOException
    {
        log.info("Take parameters from property file");
        Properties props = new Properties();

        props.load(new FileInputStream(new File("config.ini")));

        IP_BROKER = props.getProperty("IP_BROKER");
        EXCHANGE_NAME = props.getProperty("EXCHANGE_NAME");
        TYPE_OF_EXCHANGE = props.getProperty("TYPE_OF_EXCHANGE");
        log.info("Configuration was successfully loaded");
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
        if (!isRunning.get())
        {
            synchronized (this)
            {
                if (!isRunning.get())
                {
                    config();
                    createAndStartConsumer();
                    createAndStartGUI();
                    isRunning.set(true);
                }
            }
        }
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
            private Gson gson;


            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException
            {
                String jsonString = new String(body, "UTF-8");
                InstanceCreator<StatisticType> ic = new InstanceCreator<StatisticType>()
                {
                    
                    @Override
                    public StatisticType createInstance(Type arg0)
                    {
                        return new StatisticType()
                        {
                        };
                    }
                };
                gson = new GsonBuilder().registerTypeAdapter(StatisticType.class, ic).create();
                MatchStatisticsDelta matchStatisticsDelta = gson.fromJson(jsonString, MatchStatisticsDelta.class);
                processPackageController.processPackage(matchStatisticsDelta, model);
            }
        };
        channel.basicConsume(queueName, true, consumer);
        log.info("Consumer have been created and started ");
    }


    /**
     * create and start GUI(draw new window with menu)
     */
    private void createAndStartGUI()
    {
        Image bgImg = new Image(display, "resources/BackgroundImage.jpg");
        shlDesktopClient.setText("Desktop Client on SWT");
        GridLayout gl_shlDesktopClient = new GridLayout();
        shlDesktopClient.setLayout(gl_shlDesktopClient);
        Image icon = new Image(display, "resources/icon.jpg");
        shlDesktopClient.setImage(icon);
        shlDesktopClient.setBackgroundImage(bgImg);
        shlDesktopClient.setBackgroundMode(SWT.INHERIT_FORCE);

        gl_shlDesktopClient.numColumns = 3;
//        Label lblEnterIdWhich = new Label(shlDesktopClient, SWT.NONE);
//        lblEnterIdWhich.setText("Enter ID of Object, which history you want to see:");

        text = new Text(shlDesktopClient, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        //Button OK
//        Button btnOk = new Button(shlDesktopClient, SWT.NONE);
//        btnOk.setText("OK");
//        Color bgColor = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
//        btnOk.setBackground(bgColor);
//        Image buttonOK = new Image(display, "resources/buttonOK.png");
//        btnOk.setImage(buttonOK);

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

//        MenuItem mJsonProvider = new MenuItem(menu_Provider, SWT.NONE);
//        mJsonProvider.setText("Json Provider");

        MenuItem mView = new MenuItem(menu, SWT.CASCADE);
        mView.setText("View");

        Menu menu_View = new Menu(mView);
        mView.setMenu(menu_View);

        MenuItem mTable = new MenuItem(menu_View, SWT.NONE);
        mTable.setText("Table");

        MenuItem mGraph = new MenuItem(menu_View, SWT.NONE);
        mGraph.setText("Graph");

        // mGraph.addSelectionListener(new GraphController(shlDesktopClient, model.providerId, this));
        mTable.addSelectionListener(new TableController(viewTable, shlDesktopClient));
////        mXmlProvider.addSelectionListener(new XmlProviderController(lblprovider, model));
////        mJsonProvider.addSelectionListener(new JsonProviderController(lblprovider, model));
//        btnOk.addSelectionListener(new ButtonOKController(model, text, viewTable));

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
