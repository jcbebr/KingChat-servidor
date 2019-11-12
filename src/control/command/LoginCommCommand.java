package control.command;

import control.DataBaseController;
import model.CommandStatementName;
import java.net.Socket;
import model.Client;
import model.CommandStatementValue;
import org.json.JSONObject;

public class LoginCommCommand extends CommCommand {

    private JSONObject json;

    public LoginCommCommand(Socket socket, JSONObject json) {
        super(socket);
        this.json = json;
    }

    @Override
    public void execute() {
        //Verificar se o usuário tem acesso
        Client client = new Client();
        client.setNick(json.getString(CommandStatementName.CMM_NICK.getName()));
        client.setPass(json.getString(CommandStatementName.CMM_PASS.getName()));

        Client searchClient = DataBaseController.getInstance().searchClient(client);
        boolean found = searchClient != null;
        
        JSONObject json = new JSONObject();
        json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.LOGIN_STATUS.ordinal());
        json.put(CommandStatementName.CMM_STATUS.getName(), found);
        if (found) {
            this.setClientOnJSON(json, searchClient);
        }
        this.send(json.toString());
    }

}
