package ajeffrey.teaching.size.server;

import java.io.IOException;

import ajeffrey.teaching.debug.Debug;

/**
 * A server which lists the size of files asked for by a client.
 * It takes a port as an argument.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public class Main {

    public static void main (String[] args) throws IOException {
        Debug.out.addPrintStream (System.err);
	final int port = Integer.parseInt (args[0]);
	Debug.out.println ("Main.main: building server");
	final Server server = Server.factory.build (port);
	try {
	    Debug.out.println ("Main.main: starting server");
	    server.start ();
	} finally {
	    Debug.out.println ("Main.main: stopping server");
	    server.stop ();
	}
	Debug.out.println ("Main.main: returning");
    }

}
