package application.connection.db;

import org.h2.tools.Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Connector extends DbConnector {

    private Server server;
    private String serverUrl;

    static {
        String className = "org.h2.Driver";
        loadClass(className);
    }

    public H2Connector(String url) {
        super("jdbc:h2:./" + url);
        this.serverUrl = "jdbc:h2:tcp://localhost/./" + url;
    }

    @Override
    public void open() throws Exception {
        if (serverUrl != null) {
            System.out.println("starting tcp server at " + serverUrl + "...");
            server = Server.createTcpServer("-web", "-webAllowOthers", "-tcpAllowOthers");
            server.start();
            System.out.println("done.");
            System.out.println(server.getStatus());
        }
        super.open();
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (serverUrl != null) {
            System.out.println("shutting down server " + serverUrl + "...");
            if (server.isRunning(true))
                server.stop();
            System.out.println("done.");
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE);
    }

}
