package ajeffrey.teaching.size.server;

/**
 * A factory for building server objects.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface ServerFactory {

    /**
     * Build a new server
     * @param port the port number to connect to
     */
    public Server build (int port);

}
