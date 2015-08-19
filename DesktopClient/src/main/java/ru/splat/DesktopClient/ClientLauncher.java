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

/**
 * <p>
 * Main class for launching a Desktop Client
 */
public class ClientLauncher {
	private static final Logger log = LoggerFactory.getLogger(ClientLauncher.class);
	
	public static void main(String[] args) {
		Client client = Client.getInstance();
		try {
			client.start();
		} catch (IOException | TimeoutException e) {
			log.error("Can't create an Client!", e);
			e.printStackTrace();
		}
	}
}
