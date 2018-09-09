package ajeffrey.teaching.http.server.parser;

import java.net.URL;
import java.util.Vector;
import java.util.Iterator;

/**
 * An HTTP request handler which passes on the request to
 * a number of downstream neighbours.
 * This implements the Observer/Observed pattern from the Gang Of Four book.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface HTTPRequestMultiplexer extends HTTPRequestHandler {

    /**
     * Add a new request handler.
     * @param handler the handler to add.
     */
    public void addHTTPRequestHandler (HTTPRequestHandler handler);

    /**
     * Remove an existing request handler.
     * @param handler the handler to remove.
     */
    public void removeHTTPRequestHandler (HTTPRequestHandler handler);

    /**
     * A factory for building HTTP request multiplexers.
     */
    public static HTTPRequestMultiplexerFactory factory =
	new HTTPRequestMultiplexerFactoryImpl ();

}

class HTTPRequestMultiplexerFactoryImpl implements HTTPRequestMultiplexerFactory {

    public HTTPRequestMultiplexer build () {
	return new HTTPRequestMultiplexerImpl ();
    }

}

class HTTPRequestMultiplexerImpl implements HTTPRequestMultiplexer {

    protected final Vector contents = new Vector ();

    public void handleGetRequest (final URL url) {
	for (Iterator i = contents.iterator (); i.hasNext ();) {
	    HTTPRequestHandler h = (HTTPRequestHandler)(i.next ());
	    h.handleGetRequest (url);
	}
    }

    public void addHTTPRequestHandler (final HTTPRequestHandler handler) {
	contents.add (handler);
    }

    public void removeHTTPRequestHandler (final HTTPRequestHandler handler) {
	contents.remove (handler);
    }


}
