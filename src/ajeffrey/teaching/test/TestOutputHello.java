package ajeffrey.teaching.test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A class which prints "Hello, World" the hard way to an output
 * stream.
 * @author Alan Jeffrey
 * @version v1.0.1
 */
public class TestOutputHello {

    public static void main (String[] args) {
	TestOutputHello test = new TestOutputHello ();
	test.printHelloWorld ("test.txt", "Hello, World\n");
    }

    void printHelloWorld (final String filename, final String msg) {
	try {
            final OutputStream out = new FileOutputStream (filename);
	    try {
		for (int i=0; i < msg.length (); i++) {
		    out.write (msg.charAt (i));
		}
	    } catch (final IOException ex) {
		// Some error handling code goes here
		System.err.println ("Oops 1: " + ex);
	    } finally {
		// Always a good idea to close streams
		try {
		    out.close ();
		} catch (final IOException ex) {
		    // Some more error handling code goes here
		    System.err.println ("Oops 2: " + ex);
		}
	    }
	} catch (IOException ex) {
	    // Even more error handling code goes here
	    System.err.println ("Oops 2: " + ex);
	}
    }

}
