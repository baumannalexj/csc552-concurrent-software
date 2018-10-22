package ajeffrey.teaching.size.server;

import java.net.Socket;

/**
 * A factory for building connection objects.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface ConnectionFactory {

    /**
     * Build a new connection
     * @param socket the socket to connect over
     */
    public Connection build (Socket socket);

}
