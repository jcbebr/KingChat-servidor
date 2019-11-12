package control.command;

import model.CommandStatementName;
import model.CommandStatementValue;
import java.net.Socket;
import org.json.JSONObject;

/**
 *
 * @author José Carlos
 */
public class IdentifyCommandCommCommand extends CommCommand {

    public IdentifyCommandCommCommand(Socket socket) {
        super(socket);
        System.out.println("Conectou");
    }

    @Override
    public void execute() {
        try {
            JSONObject json = new JSONObject(this.read());
//            System.out.println(json.toString());
            switch (CommandStatementValue.values()[json.getInt(CommandStatementName.CMM_TYPE.getName())]) {
                case LOGIN:
                    new LoginCommCommand(socket, json).execute();
                    break;
                case SIGNIN:
                    new SignInCommCommand(socket, json).execute();
                    break;
                case CHANGEDATA:
                    new ChangeDataCommCommand(socket, json).execute();
                    break;
                case ADDCONTACT:
                    new AddContactCommCommand(socket, json).execute();
                    break;
                case REMOVECONTACT:
                    new RemoveContactCommCommand(socket, json).execute();
                    break;
                case CONTACTLIST:
                    new ContactListCommCommand(socket, json).execute();
                    break;
                default:
                    send("");
                    break;
            }
        } finally {
            close();
        }
    }

}
