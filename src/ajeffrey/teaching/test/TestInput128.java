package ajeffrey.teaching.test;

import java.io.InputStream;
import java.io.IOException;

/**
 * A class which reads 128 bytes from standard in, and prints it.
 * @author Alan Jeffrey
 * @version v1.0.1
 */
public class TestInput128 {

    public static void main (String[] args) {
	TestInput128 test = new TestInput128 ();
	final byte[] input = test.get128 (System.in);
	System.out.println ("Read: " + new String (input));
    }

    byte[] get128 (final InputStream in) {
        final byte[] result = new byte[128];
	try {
	    for (int bytesRead = 0; bytesRead < 128;) {
		final int read = in.read (result, bytesRead, 128-bytesRead);
		// Check to see if we hit the end of the stream
		if (read > 0) {
		    bytesRead = bytesRead + read;
		} else {
		    // End of stream!
		    break;
		}
	    }
	} catch (IOException ex) {
	    System.err.println ("Oops: " + ex);
	} finally {
	    return result;
	}
    }

}
