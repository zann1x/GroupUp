package application.connection;

public interface Connection {

    void open() throws Exception;
    void close() throws Exception;

}
