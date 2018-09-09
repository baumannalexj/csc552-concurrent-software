package ajeffrey.teaching.http.server;

import ajeffrey.teaching.http.server.manager.ConnectionManager;
import ajeffrey.teaching.http.server.connection.Connection;
// import ajeffrey.teaching.http.server.connection.JavaCCConnection;
import ajeffrey.teaching.http.server.connection.ConnectionFactory;
import ajeffrey.teaching.debug.Debug;

import java.io.IOException;

/**
 * A very simple HTTP server running on port 2000.
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public class Main {

    /**
     * Start the server.
     * @param args the command line arguments (which are discarded).
     * @exception IOException thrown if the HTTP server fails to
     *   bind to port 2000.
     */
    public static void main (String[] args) throws IOException {
	Debug.out.addPrintStream (System.err);
	Debug.out.println ("Main.main: Creating manager");
	final ConnectionFactory factory = 
	    Connection.factory;
	// JavaCCConnection.factory;
	final ConnectionManager manager = 
	    ConnectionManager.factory.build (2000, factory);
	Debug.out.println ("Main.main: Calling manager.run ()");
	manager.start ();
	Debug.out.println ("Main.main: Returning");
    }

}
