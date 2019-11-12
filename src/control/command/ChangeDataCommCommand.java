package control.command;

import control.DataBaseController;
import static control.command.CommCommand.getClientFromJSON;
import java.net.Socket;
import model.Client;
import model.CommandStatementName;
import model.CommandStatementValue;
import org.json.JSONObject;

public class ChangeDataCommCommand extends CommCommand {

    private JSONObject json;

    public ChangeDataCommCommand(Socket socket, JSONObject json) {
        super(socket);
        this.json = json;
    }

    @Override
    public void execute() {
        Client client = getClientFromJSON(json);

        boolean updateClient = DataBaseController.getInstance().updateClient(client);

        JSONObject json = new JSONObject();
        json.put(CommandStatementName.CMM_TYPE.getName(), CommandStatementValue.CHANGEDATA_STATUS.ordinal());
        json.put(CommandStatementName.CMM_STATUS.getName(), updateClient);
        this.send(json.toString());
    }

}
