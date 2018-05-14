package connection;

public interface Connector {

    void open() throws Exception;
    void close() throws Exception;

}
