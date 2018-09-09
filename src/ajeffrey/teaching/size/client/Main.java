package ajeffrey.teaching.size.client;

import java.io.IOException;
import java.io.FileNotFoundException;

import ajeffrey.teaching.debug.Debug;

/**
 * A client which finds the size of files given by a size server.
 * It takes a server name, a port and a list of files as arguments.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public class Main {

    public static void main (String[] args) throws IOException {
	Debug.out.addPrintStream (System.err);
	final String hostName = args[0];
	final int port = Integer.parseInt (args[1]);
	Debug.out.println ("Main.main: Building client");
	final Client client = Client.factory.build (hostName, port);
	Debug.out.println ("Main.main: Starting client");
	client.start ();
	for (int i = 2; i<args.length; i++) {
	    Debug.out.println ("Main.main: Finding size of " + args[i]);
	    try {
		long size = client.getSize (args[i]);
		System.out.println ("File " + args[i] + " is " + size + " bytes");
	    } catch (final FileNotFoundException ex) {
		System.out.println ("File " + args[i] + " does not exist");
	    }
	}
	Debug.out.println ("Main.main: Stopping client");
	client.stop ();
	Debug.out.println ("Main.main: Returning");
    }

}
