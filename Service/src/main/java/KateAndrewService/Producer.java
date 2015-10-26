package KateAndrewService;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Andrey & Ekaterina
 *         Class producer for RabbitMQ
 */
public class Producer
{
    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    private final String configProducerFilePath = "resources/configProducer.ini";
    
    private static String IP_BROKER;

    private static String EXCHANGE_NAME;

    private static String TYPE_OF_EXCHANGE;

    private final ConnectionFactory connectionFactory;

    private final Connection connection;

    private final Channel channel;

    private final Gson gson = new Gson();


    /**
     * take parameters from configProducer.ini file
     */
    public void configProducer() throws IOException
    {
        log.info("Take parameters from configProduser.ini file");
        Properties props = new Properties();

        props.load(new FileInputStream(new File(configProducerFilePath)));

        IP_BROKER = props.getProperty("IP_BROKER");
        EXCHANGE_NAME = props.getProperty("EXCHANGE_NAME");
        TYPE_OF_EXCHANGE = props.getProperty("TYPE_OF_EXCHANGE");
    }


    /**
     * Connect to the server RabbitMQ and get the connection object to work with him
     *
     * @throws IOException
     * @throws TimeoutException
     */
    Producer() throws IOException, TimeoutException
    {
        log.info("connect to the server RabbitMQ");
        configProducer();
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP_BROKER);
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, TYPE_OF_EXCHANGE);
        log.info("Connection is successfully installed");
    }


    /**
     * Published an object in Json format in all queues
     *
     * @param providerPackage
     * @throws IOException
     */
    public void publish(MatchStatisticsDelta statisticDelta) throws IOException
    {
        log.info("Publish a new object of " + statisticDelta.getClass());
        String jsonString = gson.toJson(statisticDelta);
        channel.basicPublish(EXCHANGE_NAME, "", null, jsonString.getBytes());
        log.info("An object " + statisticDelta.getClass() + " successfully published");
    }
}
