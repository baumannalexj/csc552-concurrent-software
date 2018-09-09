package ajeffrey.teaching.http.client.logic;

import ajeffrey.teaching.debug.Debug;

import java.io.PrintWriter;

/**
 * The business logic for a simple HTTP client.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface Logic {

    /**
     * Set the output stream for the business logic to print to.
     * @param out the new output stream
     */
    public void setPrintWriter (PrintWriter out);

    /**
     * Get a URL and print the contents out to the output stream.
     * If the URL is malformed, or the contents cannot be fetched,
     * an error is printed to the output stream.
     * @param url the URL to fetch
     */
    public void get (String url);

    /**
     * Stop getting a URL.
     * This will stop the fetch of the URL.
     * It is safe to call this even if there is no get running at the moment.
     */
    public void stop ();

    /**
     * A factory for building new business logic.
     */
    public static final LogicFactory factory = new LogicFactoryImpl ();

}

class LogicFactoryImpl implements LogicFactory {

    public Logic build () { return new LogicImpl (); }

}

class LogicImpl implements Logic {

    protected PrintWriter out;
    protected Connection connection;

    public synchronized void setPrintWriter (final PrintWriter out) { 
	Debug.out.println ("LogicImpl.setWriter: Starting");
	this.out = out; 
	Debug.out.println ("LogicImpl.setWriter: Returning");
    }

    public synchronized void get (final String url) {
	Debug.out.println ("LogicImpl.get: Starting");
	Debug.out.println ("LogicImpl.get: Building connection");
	connection = Connection.factory.build (url, out);
	Debug.out.println ("LogicImpl.get: Starting connection");
	connection.start ();
	Debug.out.println ("LogicImpl.get: Returning");    
    }

    public synchronized void stop () {
	Debug.out.println ("LogicImpl.stop: Starting");
	if (connection == null) {
	    Debug.out.println ("LogicImpl.stop: connection is null");
	} else {
	    connection.stop ();
	}
	Debug.out.println ("LogicImpl.stop: Returning");    
    }

}
