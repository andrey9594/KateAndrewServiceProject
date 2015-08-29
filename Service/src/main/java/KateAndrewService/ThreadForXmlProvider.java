package KateAndrewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * <p>
 *
 * @author Ekaterina
 *         Thread For Xml Provider
 */
public class ThreadForXmlProvider implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ThreadForXmlProvider.class);

    private Socket socket;
    private Producer producer;
    private ServiceSQL service;

    public ThreadForXmlProvider(Socket socket, Producer producer, ServiceSQL service) {
        this.socket = socket;
        this.producer = producer;
        this.service = service;
    }

    /**
     * Take and deserialize data from Xml Provider
     */
    @Override
    public void run() {

        try {
            Scanner scanner = new Scanner(socket.getInputStream());

            while (true) {
                String xmlString = scanner.nextLine();
                JAXBContext jaxbContext = JAXBContext.newInstance(ProviderPackage.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                ProviderPackage providerPackage = (ProviderPackage) unmarshaller.unmarshal(new StringBufferInputStream(xmlString));
                producer.publish(providerPackage);
                service.cacheXml(providerPackage.getId(), providerPackage.getValue());
                log.info("Data from  with Value= {} is received",providerPackage.getValue());
            }
        } catch (SQLException e) {
            log.error("Error in connect BD");
            e.printStackTrace();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            log.error("Error in receiving data from XmlProvider");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log.error("Error in socket closing");
                e.printStackTrace();
            }
        }
    }

}