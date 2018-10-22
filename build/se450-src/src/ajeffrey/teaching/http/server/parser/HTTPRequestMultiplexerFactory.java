package ajeffrey.teaching.http.server.parser;

/**
 * A factory which builds HTTP request multiplexers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface HTTPRequestMultiplexerFactory {

    /**
     * Return a new HTTP request multiplexer
     */
    public HTTPRequestMultiplexer build ();

}
