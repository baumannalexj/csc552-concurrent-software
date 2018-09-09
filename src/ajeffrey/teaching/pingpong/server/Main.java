package ajeffrey.teaching.pingpong.server;

import ajeffrey.teaching.debug.Debug;

/**
 * A simple FTP server.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public class Main {

    public static void main (String[] args) {
	Debug.out.addPrintStream (System.out);
	Debug.out.println ("Starting server");
	final Server server = Server.factory.build (2000);
	server.start ();
    }

}
