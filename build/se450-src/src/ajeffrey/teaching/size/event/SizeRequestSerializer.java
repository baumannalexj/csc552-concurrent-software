package ajeffrey.teaching.size.event;

import ajeffrey.teaching.debug.Debug;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.macfaq.io.SafePrintWriter;

/**
 * A handler which sends size requests to an output stream.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface SizeRequestSerializer extends SizeRequestHandler {

    /**
     * A factory for building new request serializers.
     */
    public static final SizeRequestSerializerFactory factory =
	new SizeRequestSerializerFactoryImpl ();

}

class SizeRequestSerializerFactoryImpl implements SizeRequestSerializerFactory {

    public SizeRequestSerializer build (final OutputStream out) {
	return new SizeRequestSerializerImpl (out);
    }

}

class SizeRequestSerializerImpl implements SizeRequestSerializer {

    protected final SafePrintWriter out;

    SizeRequestSerializerImpl (final OutputStream out) {
	try {
	    this.out = new SafePrintWriter (new OutputStreamWriter (out, "UTF-8"), true, "\n");
	} catch (final UnsupportedEncodingException ex) {
	    System.err.println ("This shouldn't happen! " + ex);
	    System.exit (1);
	    throw new RuntimeException ("A stupid exception to get past Java's control flow analysis");
	}
	Debug.out.println ("SizeRequestSerializerImpl: built");
    }

    public void handleSizeRequest (final String fileName) throws IOException {
	Debug.out.println ("SizeRequestSerializerImpl.handleSizeRequest: Starting");
	Debug.out.println ("SizeRequestSerializerImpl.handleSizeRequest: sending SIZE " + fileName);
	out.println ("SIZE " + fileName);
	Debug.out.println ("SizeRequestSerializerImpl.handleSizeRequest: Returning");
    }

    public void handleQuitRequest () throws IOException {
	Debug.out.println ("SizeRequestSerializerImpl.handleQuitRequest: Starting");
	Debug.out.println ("SizeRequestSerializerImpl.handleQuitRequest: Sending QUIT");
	out.println ("QUIT");
	Debug.out.println ("SizeRequestSerializerImpl.handleQuitRequest: Returning");
    }


}
