package application.connection.db;

import application.connection.Connector;

import java.sql.*;

public abstract class DbConnector implements Connector {

    protected String databaseUrl;
    protected Connection connection;

    protected DbConnector(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    protected static void loadClass(String className) {
        System.out.println("loading class " + className + "...");
        try {
            Class.forName(className);
            System.out.println("done.");
        } catch (ClassNotFoundException e) {
            System.out.println("could not load class " + className);
            e.printStackTrace();
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE);
    }

    @Override
    public void open() throws Exception {
        System.out.println("connecting to " + databaseUrl + "...");
        connection = DriverManager.getConnection(databaseUrl);
        System.out.println("done.");
    }

    @Override
    public void close() throws Exception {
        System.out.println("disconnecting from " + databaseUrl + "...");
        if (!connection.isClosed())
            connection.close();
        System.out.println("done.");
    }

}
