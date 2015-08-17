package KateAndrewService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * Class producer for RabbitMQ
 *
 */
public class Producer {
	private final String IP_BROKER = "localhost";
	private final String EXCHANGE_NAME = "packages";
	
	private final ConnectionFactory connectionFactory;
	private final Connection connection;
	private final Channel channel;
	
	private final Gson gson = new Gson();
	
	Producer() throws IOException, TimeoutException {
		connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(IP_BROKER);
		connection = connectionFactory.newConnection();
		channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	}
	public void publish(ProviderPackage providerPackage) throws IOException {
		String jsonString = gson.toJson(providerPackage);
    	channel.basicPublish(EXCHANGE_NAME, "", null, jsonString.getBytes());
	}
}
