package KateAndrewService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * Main method
 *
 * @author Ekaterina
 */
public class Main
{
    private static final Logger log = LoggerFactory.getLogger(ServiceSQL.class);


    /**
     * Main method, whith start Service
     * @param args
     */
    public static void main(String[] args)
    {
        ServiceNoSQL service = new ServiceNoSQL();
        service.start();
        log.info("Service is started");
    }
}
