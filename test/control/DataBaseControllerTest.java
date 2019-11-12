package control;

import control.command.CommCommand;
import java.util.ArrayList;
import junit.framework.TestCase;
import model.Client;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author José Carlos
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataBaseControllerTest extends TestCase {

    private static Client client_test_0 = CommCommand.getRandomClient();
    private static Client client_test_1 = CommCommand.getRandomClient();

    @After
    public void tearDown() throws Exception {
        //Simulação do login / atualização das informações
        client_test_0 = DataBaseController.getInstance().searchClient(client_test_0);
        client_test_1 = DataBaseController.getInstance().searchClient(client_test_1);
    }

    @Test
    public void test_1_InsertClient() {
        System.out.println("--test--insertClient");

        //Simulação do cadastro
        assertEquals(true, DataBaseController.getInstance().insertClient(client_test_0));
        assertEquals(true, DataBaseController.getInstance().insertClient(client_test_1));
    }

    @Test
    public void test_2_UpdateClient() {
        System.out.println("--test--updateClient");
        client_test_0.setNick("updateClient");
        assertEquals(true, DataBaseController.getInstance().updateClient(client_test_0));
    }

    @Test
    public void test_3_SearchClient() {
        System.out.println("--test--searchClient");
        assertNotNull(DataBaseController.getInstance().searchClient(client_test_1));
    }

    @Test
    public void test_4_InsertRelationClients() {
        System.out.println("--test--insertRelationClients");
        assertEquals(true, DataBaseController.getInstance().insertRelationClients(client_test_0, client_test_1));
        assertEquals(true, DataBaseController.getInstance().insertRelationClients(client_test_1, client_test_0));
    }

    @Test
    public void test_5_RemoveRelationClients() {
        System.out.println("--test--removeRelationClients");
        assertEquals(true, DataBaseController.getInstance().removeRelationClients(client_test_0, client_test_1));
    }

    @Test
    public void test_6_ListRelationClients() {
        System.out.println("--test--listRelationClients");
        ArrayList<Client> listRelationClients = DataBaseController.getInstance().listRelationClients(client_test_1);
        assertNotNull(listRelationClients);
        assertEquals(0, listRelationClients.size());
    }

    @Test
    public void test_7_ListAvaliableRelationClients() {
        System.out.println("--test--listAvaliableRelationClients");
        ArrayList<Client> listRelationClients = DataBaseController.getInstance().listRelationClients(client_test_0);
        assertNotNull(listRelationClients);
        assertEquals(0, listRelationClients.size());
    }

}
