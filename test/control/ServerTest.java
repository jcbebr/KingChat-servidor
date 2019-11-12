package control;

import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author José Carlos
 */
public class ServerTest extends TestCase{
    
    @Test
    public void testMain() {
        DataBaseController.getInstance().printAllClients();
    }

}
