package KateAndrewService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * <p>
 *
 * @author Ekaterina
 *         Thread For Xml Provider
 */
public class ThreadForXmlProvider implements Runnable {

    @Override
    public void run() {

        Service service = new Service();

        try {
            Socket socket = new Socket(service.getIP(), service.getPORT_xml());
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

                        service.cacheXml(prov.getId(), prov.getValue());
                        xmlString = new StringBuilder();
                    } else {
                        xmlString.append((char) b);
                    }
                }
            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}