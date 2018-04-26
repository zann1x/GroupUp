package application.connection.db;

import com.mysql.cj.jdbc.MysqlDataSource;

public class MySqlConnector extends DbConnector {

    public MySqlConnector(String databaseUrl) {
        super("jdbc:mysql://localhost:3306/" + databaseUrl + "?useTimeZone=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
    }

    @Override
    public void open() throws Exception {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(databaseUrl);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("");
        connection = mysqlDataSource.getConnection();
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
}
