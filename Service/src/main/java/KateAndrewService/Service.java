package KateAndrewService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.Socket;


/**
 * <p>
 *
 * @author Ekaterina
 * Connect with provider and give info in xml format
 */
public class Service {
    private static final int PORT = 12345;
    private static final String IP = "localhost";

    public void connect() {
        try {
            Socket socket = new Socket(IP, PORT);
            try {
                InputStream input = socket.getInputStream();
                StringBuilder xmlString = new StringBuilder();
                while (true) {
                    int b = input.read();
                    if (b == -1) {
                        break;
                    } else if (b == 0) {
                        JAXBContext jaxbContext = JAXBContext.newInstance(ProviderPackage.class);
                        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                        ProviderPackage prov = (ProviderPackage) unmarshaller.unmarshal(new StringBufferInputStream(xmlString.toString()));
                        System.out.println(prov.getId() + "\n" + prov.getValue());
                        xmlString = new StringBuilder();
                    } else {
                        xmlString.append((char) b);
                    }
                }
            } catch (JAXBException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
