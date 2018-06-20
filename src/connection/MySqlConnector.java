package connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

public class MySqlConnector {

    private MysqlDataSource mysqlDataSource;
    protected String databaseUrl;
    protected Connection connection;

    public MySqlConnector(String databaseUrl) {
    	this.databaseUrl = "jdbc:mysql://localhost:3306/" + databaseUrl + "?useTimeZone=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        
    }

    public void open() throws Exception {
        System.out.println("connecting to " + databaseUrl + "...");
        mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(databaseUrl);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("");
        connection = mysqlDataSource.getConnection();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    public void close() throws Exception {
        System.out.println("disconnecting from " + databaseUrl + "...");
        if (!connection.isClosed())
            connection.close();
    }
}
