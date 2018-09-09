package ajeffrey.teaching.size.event;

import java.net.Socket;

import java.io.IOException;

import java.util.Vector;
import java.util.Iterator;

/**
 * A multiplexer for SIZE repsonses.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface SizeResponseMultiplexer extends SizeResponseHandler {

    /**
     * Add a new handler.
     * @param the handler to add
     */
    public void addSizeResponseHandler (SizeResponseHandler handler);

    /**
     * Remove an existing handler.
     * @param the handler to remove
     */
    public void removeSizeResponseHandler (SizeResponseHandler handler);

    /**
     * A factory for building new multiplexers.
     */
    public static SizeResponseMultiplexerFactory factory =
	new SizeResponseMultiplexerFactoryImpl ();

}

class SizeResponseMultiplexerFactoryImpl implements SizeResponseMultiplexerFactory {

    public SizeResponseMultiplexer build () {
	return new SizeResponseMultiplexerImpl ();
    }

}

class SizeResponseMultiplexerImpl implements SizeResponseMultiplexer {

    protected final Vector contents = new Vector ();

    public void addSizeResponseHandler (final SizeResponseHandler handler) {
	contents.add (handler);
    }

    public void removeSizeResponseHandler (final SizeResponseHandler handler) {
	contents.remove (handler);
    }

    public void handleFoundResponse (final String fileName, final long size) throws IOException {
	for (final Iterator i = contents.iterator (); i.hasNext ();) {
	    ((SizeResponseHandler)(i.next ())).handleFoundResponse (fileName, size);
	}
    }

    public void handleNotFoundResponse () throws IOException {
	for (final Iterator i = contents.iterator (); i.hasNext ();) {
	    ((SizeResponseHandler)(i.next ())).handleNotFoundResponse ();
	}
    }

    public void handleOKResponse () throws IOException {
	for (final Iterator i = contents.iterator (); i.hasNext ();) {
	    ((SizeResponseHandler)(i.next ())).handleOKResponse ();
	}
    }

    public void handleEndOfStream () throws IOException {
	for (final Iterator i = contents.iterator (); i.hasNext ();) {
	    ((SizeResponseHandler)(i.next ())).handleEndOfStream ();
	}
    }

}
