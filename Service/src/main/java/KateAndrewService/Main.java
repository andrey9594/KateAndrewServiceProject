package KateAndrewService;

/**
 * <p>
 * Main method
 *
 * @author Andrey & Ekaterina
 */
public class Main
{
    /**
     * Main method, which start a Service
     * @param args
     */
    public static void main(String[] args)
    {
        ServiceNoSQL service = new ServiceNoSQL();
        service.start();
    }
}
