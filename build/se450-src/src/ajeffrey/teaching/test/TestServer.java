package ajeffrey.teaching.test;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A class which listens on port 2000, and prints anything it hears.
 * Very useful for reverse engineering protocols!
 */

public class TestServer {

    public static void main (String[] args) throws Exception {
	ServerSocket ss = new ServerSocket (2000);
	Socket s = ss.accept ();
	InputStream in = s.getInputStream ();
	byte[] buffer = new byte[1024];
	for (int i=0; i>=0; i=in.read (buffer)) {
	    System.out.write (buffer, 0, i);
	    System.out.flush ();
	}
    }

}
