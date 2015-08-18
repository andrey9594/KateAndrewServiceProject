package KateAndrewService;

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
    private Socket socket;
    private Producer producer;
    private Service service;

    public ThreadForXmlProvider(Socket socket, Producer producer, Service service) {
        this.socket = socket;
        this.producer = producer;
        this.service = service;
    }

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
                //System.out.println("id = " + providerPackage.getId() + ", value = " + providerPackage.getValue());
                service.cacheXml(providerPackage.getId(), providerPackage.getValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}