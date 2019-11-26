package control;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author José Carlos
 */
public class Defs {

    private static Defs instance;
    private int server_port;
    private int rf_interval;

    public Defs() {
        try (InputStream input = new FileInputStream("./defs.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            this.server_port = Integer.parseInt(prop.getProperty("server_port"));
            this.rf_interval = Integer.parseInt(prop.getProperty("rf_interval"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Defs getInstance() {
        if (instance == null) {
            instance = new Defs();
        }
        return instance;
    }

    public int getServer_port() {
        return server_port;
    }

    public int getRf_interval() {
        return rf_interval;
    }

}
