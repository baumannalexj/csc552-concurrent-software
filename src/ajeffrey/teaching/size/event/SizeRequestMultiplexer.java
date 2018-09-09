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
public interface SizeRequestMultiplexer extends SizeRequestHandler {

    /**
     * Add a new handler.
     * @param the handler to add
     */
    public void addSizeRequestHandler (SizeRequestHandler handler);

    /**
     * Remove an existing handler.
     * @param the handler to remove
     */
    public void removeSizeRequestHandler (SizeRequestHandler handler);

    /**
     * A factory for building new multiplexers.
     */
    public static SizeRequestMultiplexerFactory factory =
	new SizeRequestMultiplexerFactoryImpl ();

}

class SizeRequestMultiplexerFactoryImpl implements SizeRequestMultiplexerFactory {

    public SizeRequestMultiplexer build () {
	return new SizeRequestMultiplexerImpl ();
    }

}

class SizeRequestMultiplexerImpl implements SizeRequestMultiplexer {

    protected final Vector contents = new Vector ();

    public void addSizeRequestHandler (final SizeRequestHandler handler) {
	contents.add (handler);
    }

    public void removeSizeRequestHandler (final SizeRequestHandler handler) {
	contents.remove (handler);
    }

    public void handleSizeRequest (final String fileName) throws IOException {
	for (final Iterator i = contents.iterator (); i.hasNext ();) {
	    ((SizeRequestHandler)(i.next ())).handleSizeRequest (fileName);
	}
    }

    public void handleQuitRequest () throws IOException {
	for (final Iterator i = contents.iterator (); i.hasNext ();) {
	    ((SizeRequestHandler)(i.next ())).handleQuitRequest ();
	}
    }

}
