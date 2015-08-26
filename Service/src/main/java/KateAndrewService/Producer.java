package KateAndrewService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrew&Ekaterina
 *         Class producer for RabbitMQ
 */
public class Producer {
    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    private final String IP_BROKER = "localhost";
    private final String EXCHANGE_NAME = "packages";

    private final ConnectionFactory connectionFactory;
    private final Connection connection;
    private final Channel channel;

    private final Gson gson = new Gson();

    /**
     * Connect to the server RabbitMQ and get the connection object to work with him
     *
     * @throws IOException
     * @throws TimeoutException
     */
    Producer() throws IOException, TimeoutException {
        log.info("connect to the server RabbitMQ");
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP_BROKER);
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        log.info("Connection is successfully installed");
    }

    /**
     * Published object ProviderPackage in Json format in all queues
     *
     * @param providerPackage
     * @throws IOException
     */
    public void publish(ProviderPackage providerPackage) throws IOException {
        log.info("Publish a new object 'ProviderPackage'");
        String jsonString = gson.toJson(providerPackage);
        channel.basicPublish(EXCHANGE_NAME, "", null, jsonString.getBytes());
        log.info("Object 'ProviderPackage' successfully published");
    }
}
