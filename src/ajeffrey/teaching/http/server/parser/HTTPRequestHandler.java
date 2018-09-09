package ajeffrey.teaching.http.server.parser;

import java.net.URL;

/**
 * An interface for classes which can handle HTTP requests.
 * This is a very simplistic version which just handles GET requests.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface HTTPRequestHandler {

    /**
     * Handle a GET request
     * @param url the URL which was requested
     */
    public void handleGetRequest (URL url);

}
