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
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author José Carlos
 */
public class LoginCommCommandTest extends TestCase {

    @Test
    public void testExecute() {
        Server server = new Server();
        server.start();
        
        try (Socket socket = new Socket("192.168.1.8", 56000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {
            
            JSONObject json = new JSONObject();
            json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.LOGIN.ordinal());
            json.put(CommandStatementName.CMM_NICK.getName(), "Cliente teste_6428");
            json.put(CommandStatementName.CMM_PASS.getName(), "pass_6428");
            out.println(json.toString());
            json = new JSONObject(in.readLine());
            System.out.println(json.toString());
            assertEquals(CommandStatementValue.LOGIN_STATUS.ordinal(),
                    json.getInt(CommandStatementName.CMM_TYPE.getName()));
            assertTrue(json.getBoolean(CommandStatementName.CMM_STATUS.getName()));
            
        } catch (IOException ex) {
            Logger.getLogger(LoginCommCommandTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            server.close();
        }
            
    }

}
