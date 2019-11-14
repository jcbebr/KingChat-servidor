package control;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Client;

/**
 *
 * @author José Carlos
 */
public class DataBaseController {

    private static DataBaseController instance;
    private Connection conn;

    public static DataBaseController getInstance() {
        if (instance == null) {
            instance = new DataBaseController();
        }
        return instance;
    }

    private DataBaseController() {
        initDataBase();
    }

    private boolean initDataBase() {
        File path = new File("C:/sqlite");
        if (!path.exists()) {
            path.mkdir();
        }
        path = new File("C:/sqlite/db");
        if (!path.exists()) {
            path.mkdir();
        }

        try {
            String url = "jdbc:sqlite:" + path.toString() + "/" + "KingChat.db";

            this.conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Connection established - " + meta.getDriverName());

                String sql = "CREATE TABLE IF NOT EXISTS Client (\n"
                        + "    id       INTEGER PRIMARY KEY     ,\n"
                        + "    nick     VARCHAR(30)             ,\n"
                        + "    email    VARCHAR(100)            ,\n"
                        + "    oldEmail VARCHAR(100)            ,\n"
                        + "    pass     VARCHAR(30)             ,\n"
                        + "    birth    INTEGER                 ,\n"
                        + "    online   BOOLEAN                 ,\n"
                        + "    path     VARCHAR(100)            ,\n"
                        + "    port     INTEGER                  \n);";
                Statement stmt = conn.createStatement();
                stmt.execute(sql);

                sql = "CREATE TABLE IF NOT EXISTS ClientRel (\n"
                        + "    id       INTEGER PRIMARY KEY ,\n"
                        + "    client1  INTEGER             ,\n"
                        + "    client2  INTEGER              \n);";
                stmt = conn.createStatement();
                stmt.execute(sql);

                this.insertClient(new Client("jose", "jose", "jose", "jose", 1999, false, "", 0));
                this.insertClient(new Client("luis", "luis", "luis", "luis", 2001, false, "", 0));
                this.insertClient(new Client("joao", "joao", "joao", "joao", 2000, false, "", 0));
                this.insertClient(new Client("sony", "sony", "sony", "sony", 2003, false, "", 0));
                this.insertClient(new Client("play", "play", "play", "play", 2002, false, "", 0));

                System.out.println("Data-base initialized");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean insertClient(Client client) {
        if (client.getNick()== null || client.getPass()== null) {
            return false;
        }
        
        String sql = "INSERT INTO CLIENT(nick,email,pass,birth,online,path,port) VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, client.getNick());
            pstmt.setString(2, client.getEmail());
            pstmt.setString(3, client.getPass());
            pstmt.setInt(4, client.getBirth());
            pstmt.setBoolean(5, false);
            pstmt.setString(6, client.getPath());
            pstmt.setInt(7, client.getPort());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateClient(Client client) {
        if (client.getId() == null) {
            return false;
        }

        String sql = "UPDATE CLIENT "
                + "      SET nick      = ?,"
                + "          email     = ?,"
                + "          oldEmail  = ?,"
                + "          pass      = ?,"
                + "          birth     = ?,"
                + "          online    = ?,"
                + "          path      = ?,"
                + "          port      = ? "
                + "    WHERE id        = " + client.getId();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, client.getNick());
            pstmt.setString(2, client.getEmail());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getPass());
            pstmt.setInt(5, client.getBirth());
            pstmt.setBoolean(6, client.isOnline());
            pstmt.setString(7, client.getPath());
            pstmt.setInt(8, client.getPort());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public Client searchClient(Client client) {
        String sql = "SELECT * "
                + "     FROM CLIENT "
                + "    WHERE nick = '" + client.getNick() + "'"
                + "      AND pass = '" + client.getPass() + "'";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                return getClientFromResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean insertRelationClients(Client client01, Client client02) {
        if (client01.getId() == null || client02.getId() == null) {
            return false;
        }
        
        String sql = "INSERT INTO ClientRel(client1,client2) VALUES(?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, client01.getId());
            pstmt.setInt(2, client02.getId());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, client02.getId());
            pstmt.setInt(2, client01.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean removeRelationClients(Client client01, Client client02) {
        if (client01.getId() == null || client02.getId() == null) {
            return false;
        }
        
        String sql = "DELETE FROM ClientRel WHERE client1 = ? AND client2 = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, client01.getId());
            pstmt.setInt(2, client02.getId());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, client02.getId());
            pstmt.setInt(2, client01.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public ArrayList<Client> listRelationClients(Client client) {
        if (client.getId() == null) {
            return null;
        }
        
        ArrayList clients = new ArrayList();
        String sql = "SELECT * "
                + "     FROM CLIENT "
                + "    WHERE ID <> " + client.getId()
                + "      AND EXISTS (SELECT 1 "
                + "                    FROM ClientRel "
                + "                   WHERE CLIENT1 = " + client.getId()
                + "                     AND CLIENT2 = CLIENT.ID)";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                clients.add(getClientFromResultSet(rs));
            }
            return clients;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<Client> listAvaliableRelationClients(Client client) {
        if (client.getId() == null) {
            return null;
        }
        
        ArrayList clients = new ArrayList();
        String sql = "SELECT * "
                + "     FROM CLIENT "
                + "    WHERE ID <> " + client.getId()
                + "      AND NOT EXISTS (SELECT 1 "
                + "                        FROM ClientRel "
                + "                       WHERE CLIENT1 = " + client.getId()
                + "                         AND CLIENT2 = CLIENT.ID)";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                clients.add(getClientFromResultSet(rs));
            }
            return clients;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Client getClientFromResultSet(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setId(rs.getInt("id"));
        c.setNick(rs.getString("nick"));
        c.setEmail(rs.getString("email"));
        c.setOldemail(rs.getString("oldEmail"));
        c.setPass(rs.getString("pass"));
        c.setBirth(rs.getInt("birth"));
        c.setOnline(rs.getBoolean("online"));
        c.setPath(rs.getString("path"));
        c.setPort(rs.getInt("port"));
        return c;
    }

    public void printAllClients() {
        try {
            String sql = "SELECT * FROM CLIENT ";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(getClientFromResultSet(rs).toString());
            }
            System.out.println("");
            sql = "SELECT * FROM CLIENTREL";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("client1: " + rs.getInt("client1") + " - " + "client2: " + rs.getInt("client2"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
