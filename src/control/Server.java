package control;

import control.command.IdentifyCommandCommCommand;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Carlos
 */
public class Server extends Thread {

    public static void main(String[] args) {
        new Server().start();
    }

    private boolean alive;
    private ServerSocket server;

    public Server() {
        this.alive = true;
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(Defs.getInstance().getPort());
            server.setReuseAddress(true);
            while (alive) {
                System.out.println("Aguardando conexao de cliente...");
                new IdentifyCommandCommCommand(server.accept()).execute();
            }
        } catch (IOException ex) {
            if (alive) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void close() {
        this.alive = false;
        try {
            this.server.close();
            System.out.println("Fechou");
        } catch (IOException ex) {
//            System.out.println(ex);
        }
    }

}
