package application.connection.db;

public class SqliteConnector extends DbConnector {

    static {
        String className = "org.sqlite.JDBC";
        loadClass(className);
    }

    public SqliteConnector(String url) {
        super("jdbc:sqlite:" + url);
    }

}
