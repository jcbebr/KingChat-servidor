package control.command;

import control.DataBaseController;
import java.net.Socket;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONObject;


public class SignInCommCommand extends CommCommand {

    private JSONObject json;

    public SignInCommCommand(Socket socket, JSONObject json) {
        super(socket);
        this.json = json;
    }
    
    @Override
    public void execute() {
        Client client = getClientFromJSON(json);
        
        boolean insertClient = DataBaseController.getInstance().insertClient(client);
        
        JSONObject json = new JSONObject();
        json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.SIGNIN_STATUS.ordinal());
        json.put(CommandStatementName.CMM_STATUS.getName(), insertClient);
        this.send(json.toString());
    }
    
}
