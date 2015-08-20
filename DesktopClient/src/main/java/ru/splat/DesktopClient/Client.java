package ru.splat.DesktopClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * <p/>
 *
 * @author Ekaterina
 *         Desktop client
 *         Take data from Service and make desktop window
 *         in which output "id" and "value" in two forms(table and graph)
 */

public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private static final String IP_BROKER = "localhost";
    private static final String EXCHANGE_NAME = "packages";

    private boolean isRunning;

    private static volatile Client instance;

    private Client() {
    }

    public static Client getInstance() {
        if (instance == null) {
            synchronized (Client.class) {
                if (instance == null)
                    instance = new Client();
            }
        }
        return instance;
    }

    public void start() throws IOException, TimeoutException {
        if (isRunning)
            return;
        createAndStartGUI();
        createAndStartConsumer();
        isRunning = true;
    }

    private void createAndStartConsumer() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP_BROKER);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        Consumer consumer = new DefaultConsumer(channel) {
            private Gson gson = new Gson();

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String jsonString = new String(body, "UTF-8");
                ProviderPackage providerPackage = gson.fromJson(jsonString, ProviderPackage.class);
                processPackage(providerPackage);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    private void processPackage(ProviderPackage providerPackage) {
        // Write your code here! ;)
    }

    private void createAndStartGUI() {
        Display display = new Display();
        log.info("Display was created");

        Shell shlDesktopClient = new Shell(display);
        shlDesktopClient.setText("Desktop Client on SWT");
        shlDesktopClient.setLayout(new GridLayout());

        Label lblXmlprovider = new Label(shlDesktopClient, SWT.NONE);
        lblXmlprovider.setText("                                         Data from Xml Provider");
        Table table = new Table(shlDesktopClient, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 300;
        data.widthHint = 300;
        table.setLayoutData(data);
        String[] titles = {"id", "value"};
        for (int i = 0; i < titles.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(titles[i]);
            System.out.print(i);
        }

        //the output values
        int count = 100;
        for (int i = 0; i < count; i++) {
            TableItem item = new TableItem(table, SWT.NONE);
            //take data

            item.setText("id"); // output id
            item.setText("value"); //output value

        }
        for (int i = 0; i < titles.length; i++) {
            table.getColumn(i).pack();
        }
        shlDesktopClient.pack();

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
        shlDesktopClient.open();
        while (!shlDesktopClient.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
        log.info("Display was disposed");
    }
//		shlDesktopClient.addListener(SWT.KeyDown, new Listener() {
//			@Override
//			public void handleEvent(Event event) {
//				if (event.type == SWT.KeyDown && event.character == SWT.ESC) {
//					shlDesktopClient.dispose();
//				}
//			}
//		});

}

