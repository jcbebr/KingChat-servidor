package control.command;

import control.DataBaseController;
import static control.command.CommCommand.getClientFromJSON;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONArray;
import org.json.JSONObject;

public class ContactListCommCommand extends CommCommand {

    private JSONObject json;

    public ContactListCommCommand(Socket socket, JSONObject json) {
        super(socket);
        this.json = json;
    }

    @Override
    public void execute() {
        Client client = getClientFromJSON(json);

        List<Client> clients = new ArrayList<>();
        if (json.has(CommandStatementName.CMM_CLIENT_LIST_AVALIABLE.getName())) {
            if (json.getBoolean(CommandStatementName.CMM_CLIENT_LIST_AVALIABLE.getName())) {
                clients = DataBaseController.getInstance().listAvaliableRelationClients(client);
            } else {
                clients = DataBaseController.getInstance().listRelationClients(client);
            }
        }

        JSONObject json = new JSONObject();
        json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.CONTACTLIST_STATUS.ordinal());
        JSONArray optJSONArray = new JSONArray(clients);
        json.put(CommandStatementName.CMM_CLIENT_ARRAY.getName(), optJSONArray);
        json.put(CommandStatementName.CMM_STATUS.getName(), true);
        this.send(json.toString());
    }

}
