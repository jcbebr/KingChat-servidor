package control.command;

import control.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author José Carlos
 */
public class ContactListCommCommandTest extends TestCase {

    @Test
    public void testExecute() {
        Server server = new Server();
        server.start();
        try (Socket socket = new Socket("192.168.1.8", 56000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {

            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.CONTACTLIST.ordinal());
            json.put(CommandStatementName.CMM_CLIENT_LIST_AVALIABLE.getName(), true);
            Client client = CommCommand.getRandomClient();
            client.setId(1);
            CommCommand.setClientOnJSON(json, client);

            out.println(json.toString());
            json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            assertEquals(CommandStatementValue.CONTACTLIST_STATUS.ordinal(),
                    json.getInt(CommandStatementName.CMM_TYPE.getName()));
            assertTrue(json.getBoolean(CommandStatementName.CMM_STATUS.getName()));

        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            server.close();
        }

    }

    @Test
    public void testExecute2() {
        Server server = new Server();
        server.start();
        try (Socket socket = new Socket("192.168.1.8", 56000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {

            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.CONTACTLIST.ordinal());
            json.put(CommandStatementName.CMM_CLIENT_LIST_AVALIABLE.getName(), false);
            Client client = CommCommand.getRandomClient();
            client.setId(1);
            CommCommand.setClientOnJSON(json, client);

            out.println(json.toString());
            json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            assertEquals(CommandStatementValue.CONTACTLIST_STATUS.ordinal(),
                    json.getInt(CommandStatementName.CMM_TYPE.getName()));
            assertTrue(json.getBoolean(CommandStatementName.CMM_STATUS.getName()));

        } catch (IOException ex) {
            System.out.println(ex);
        }finally {
            server.close();
        }
    }

}
