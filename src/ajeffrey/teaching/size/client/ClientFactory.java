package ajeffrey.teaching.size.client;

/**
 * A factory for building client objects.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface ClientFactory {

    /**
     * Build a new client
     * @param hostName the name of the server
     * @param port the port number to connect to
     */
    public Client build (String hostName, int port);

}
