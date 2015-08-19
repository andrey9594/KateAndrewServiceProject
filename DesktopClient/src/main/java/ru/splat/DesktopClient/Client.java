package ru.splat.DesktopClient;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
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

public class Client {
	private static final Logger log = LoggerFactory.getLogger(Client.class);
	
	private static final String IP_BROKER = "localhost";
	private static final String EXCHANGE_NAME = "packages";
	
	private boolean isRunning;
	
	private static Client instance;
	
	private Client() { }
	
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
		createAndStartConsumer();
		createAndStartGUI();
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
				System.out.println("Got: id = " + providerPackage.getId() + ", value = " + providerPackage.getValue());
			}
		};
		channel.basicConsume(queueName, true, consumer);
	}
	
	private void createAndStartGUI() {
		Display display = new Display(); 
        log.info("Display was created");
        
        Shell shell = new Shell(display);
		shell.addListener(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.KeyDown && event.character == SWT.ESC) {
					shell.dispose();
				}
			}
		});
		
        shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		log.info("Display was disposed");

	}
}
