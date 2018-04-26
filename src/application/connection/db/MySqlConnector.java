package application.connection.db;

import com.mysql.cj.jdbc.MysqlDataSource;

public class MySqlConnector extends DbConnector {

    private Process process;

    public MySqlConnector(String databaseUrl) {
        super("jdbc:mysql://localhost:3306/" + databaseUrl + "?useTimeZone=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
    }

    @Override
    public void open() throws Exception {
        //super.open();

        /*try {
            process = Runtime.getRuntime().exec("C:/xampp/mysql/bin/mysqld");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(databaseUrl);
        connection = mysqlDataSource.getConnection();
    }

    @Override
    public void close() throws Exception {
        super.close();
        //process.destroy();
    }

}
