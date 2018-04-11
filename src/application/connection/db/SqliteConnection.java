package application.connection.db;

import application.connection.Connection;

import java.sql.*;

public class SqliteConnection implements Connection {

    private String databaseUrl;

    private java.sql.Connection connection;
    private Statement statement;

    static {
        System.out.println("loading class org.sqlite.JDBC...");
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("loaded class org.sqlite.JDBC.");
        } catch (ClassNotFoundException e) {
            System.out.println("could not load class org.sqlite.JDBC.");
            e.printStackTrace();
        }
    }

    public SqliteConnection(String dbUrl) {
        databaseUrl = "jdbc:sqlite:" + dbUrl;
    }

    @Override
    public void open() throws Exception {
        System.out.println("connecting to " + databaseUrl + "...");
        connection = DriverManager.getConnection(databaseUrl);
        statement = connection.createStatement();
        System.out.println("connected to " + databaseUrl + ".");
    }

    @Override
    public void close() throws Exception {
        System.out.println("disconnecting from " + databaseUrl + "...");
        statement.close();
        connection.close();
        System.out.println("disconnected from " + databaseUrl + ".");
    }

}
