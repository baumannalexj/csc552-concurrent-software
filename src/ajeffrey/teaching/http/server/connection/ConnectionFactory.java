package ajeffrey.teaching.http.server.connection;

import java.net.Socket;

/**
 * A factory for building an HTTP connection
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface ConnectionFactory {

    public Connection build (Socket socket);

}
