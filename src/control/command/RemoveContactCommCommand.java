package control.command;

import control.DataBaseController;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONArray;
import org.json.JSONObject;


public class RemoveContactCommCommand extends CommCommand {

    private JSONObject json;
    
    public RemoveContactCommCommand(Socket socket, JSONObject json) {
        super(socket);
        this.json = json;
    }

    @Override
    public void execute() {
        JSONArray jsonClients = json.getJSONArray(CommandStatementName.CMM_CLIENT_ARRAY.getName());
        List<Client> clients = new ArrayList<>();
        for (Object jsonClient : jsonClients) {
            clients.add(CommCommand.getClientFromJSON(new JSONObject(jsonClient.toString())));
        }

        boolean removeContact = false;
        if (clients.size() == 2) {
            removeContact = DataBaseController.getInstance().removeRelationClients(clients.get(0), clients.get(1));
        }
        
        JSONObject json = new JSONObject();
        json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.REMOVECONTACT_STATUS.ordinal());
        json.put(CommandStatementName.CMM_STATUS.getName(), removeContact);
        this.send(json.toString());
    }
    
}
