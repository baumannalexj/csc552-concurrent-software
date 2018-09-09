package ajeffrey.teaching.size.event;

import ajeffrey.teaching.debug.Debug;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.macfaq.io.SafePrintWriter;

/**
 * A handler which sends size responses to an output stream.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface SizeResponseSerializer extends SizeResponseHandler {

    /**
     * A factory for building new response serializers.
     */
    public static final SizeResponseSerializerFactory factory =
	new SizeResponseSerializerFactoryImpl ();

}

class SizeResponseSerializerFactoryImpl implements SizeResponseSerializerFactory {

    public SizeResponseSerializer build (final OutputStream out) {
	return new SizeResponseSerializerImpl (out);
    }

}

class SizeResponseSerializerImpl implements SizeResponseSerializer {

    protected final SafePrintWriter out;

    protected SizeResponseSerializerImpl (final OutputStream out) {
	try {
	    this.out = new SafePrintWriter (new OutputStreamWriter (out, "UTF-8"), true, "\n");
	} catch (final UnsupportedEncodingException ex) {
	    System.err.println ("This shouldn't happen! " + ex);
	    System.exit (1);
	    throw new RuntimeException ("A stupid exception to get past Java's control flow analysis");
	}
	Debug.out.println ("SizeResponseSerializerImpl: built");
    }

    public void handleFoundResponse (final String fileName, final long size) throws IOException {
	Debug.out.println 
	    ("SizeResponseSerializerImpl.handleFoundResponse: sending FOUND " 
	     + fileName + " " + size + " BYTES");
	out.println ("FOUND " + fileName + " " + size + " BYTES");
    }

    public void handleNotFoundResponse () throws IOException {
	Debug.out.println 
	    ("SizeResponseSerializerImpl.handleNotFoundResponse: sending NOT FOUND");
	out.println ("NOT FOUND");
    }

    public void handleOKResponse () throws IOException {
	Debug.out.println 
	    ("SizeResponseSerializerImpl.handleOKResponse: sending OK");
	out.println ("OK");
    }

    public void handleEndOfStream () throws IOException {
	Debug.out.println 
	    ("SizeResponseSerializerImpl.handleEndOfStream: closing stream");
	out.close ();
    }

}
