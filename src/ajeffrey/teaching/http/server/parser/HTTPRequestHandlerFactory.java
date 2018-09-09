package ajeffrey.teaching.http.server.parser;

/**
 * A factory which builds HTTP request handlers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface HTTPRequestHandlerFactory {

    /**
     * Return a new HTTP request handler
     */
    public HTTPRequestHandler build ();

}
