package ajeffrey.teaching.http.server.manager;

import ajeffrey.teaching.http.server.connection.ConnectionFactory;

/**
 * A factory for building new connection managers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface ConnectionManagerFactory {

    /**
     * Build a new connection manager.
     * @param port the port to bind to
     * @param factory the factory to build new connections
     * @return a connection manager which listens on the port,
     *   and when it receives a connection, uses the factory to
     *   build a new connection object to deal with it.
     */
    public ConnectionManager build (int port, ConnectionFactory factory);

}
