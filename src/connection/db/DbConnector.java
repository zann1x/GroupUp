package connection.db;

import connection.Connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DbConnector implements Connector {

    protected String databaseUrl;
    protected Connection connection;

    public DbConnector(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    @Override
    public void open() throws Exception {
        System.out.println("connecting to " + databaseUrl + "...");
    }

    @Override
    public void close() throws Exception {
        System.out.println("disconnecting from " + databaseUrl + "...");
        if (!connection.isClosed())
            connection.close();
    }

}
