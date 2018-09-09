package ajeffrey.teaching.size.server;

import ajeffrey.teaching.debug.Debug;

import ajeffrey.teaching.size.event.SizeRequestHandler;
import ajeffrey.teaching.size.event.SizeRequestParser;
import ajeffrey.teaching.size.event.SizeResponseSerializer;
import ajeffrey.teaching.size.event.ParseException;

import java.net.Socket;
import java.io.File;
import java.io.IOException;

/**
 * An object for handling a SIZE connection.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface Connection {

    /**
     * Start the connection (starts a new thread for this connection).
     */
    public void start ();

    /**
     * A factory for building new connections.
     */
    public static ConnectionFactory factory = new ConnectionFactoryImpl ();

}

class ConnectionFactoryImpl implements ConnectionFactory {

    public Connection build (final Socket socket) {
	return new ConnectionImpl (socket);
    }

}

class ConnectionImpl implements Connection, SizeRequestHandler, Runnable {

    protected final Socket socket;
    protected final Thread thread = new Thread (this);
    protected boolean running = true;
    protected SizeRequestParser parser;
    protected SizeResponseSerializer serializer;

    protected ConnectionImpl (final Socket socket) {
	this.socket = socket;
    }

    public void start () {
	thread.start ();
    }

    public void run () {
	Debug.out.println ("ConnectionImpl.run: Starting");
	try {
	    Debug.out.println ("ConnectionImpl.run: Building parser and serializer");
	    parser = SizeRequestParser.factory.build (socket.getInputStream ());
	    serializer = SizeResponseSerializer.factory.build (socket.getOutputStream ());
	    parser.addSizeRequestHandler (this);
	    Debug.out.println ("ConnectionImpl.run: Entering main loop");
	    while (running) {
		Debug.out.println ("ConnectionImpl.run: Waiting to parse a request");
		parser.parseSizeRequest ();
	    }
	} catch (final ParseException ex) {
	    Debug.out.println ("ConnectionImpl.run: Caught parse exception " + ex);
	} catch (final IOException ex) {
	    Debug.out.println ("ConnectionImpl.run: Caught I/O exception " + ex);
	} finally {
	    try {
		socket.close ();
	    } catch (final IOException ex) {
		Debug.out.println ("ConnectionImpl.run: Caught closing exception " + ex);
	    }
	    Debug.out.println ("ConnectionImpl.run: Returning");
	}
    }

    public void handleQuitRequest () throws IOException {
	Debug.out.println ("ConnectionImpl.handleQuitRequest: Starting");
	running = false;
	serializer.handleOKResponse ();
	Debug.out.println ("ConnectionImpl.handleQuitRequest: Returning");
    }

    public void handleSizeRequest (final String fileName) throws IOException {
	Debug.out.println ("ConnectionImpl.handleSizeRequest: Starting");
	Debug.out.println ("ConnectionImpl.handleSizeRequest: Opening file " + fileName);
	final File file = new File (fileName);
	if (file.exists ()) {
	    Debug.out.println ("ConnectionImpl.handleSizeRequest: Opened file size = " + file.length ());
	    serializer.handleFoundResponse (fileName, file.length ());
	} else {
	    Debug.out.println ("ConnectionImpl.handleSizeRequest: No such file");
	    serializer.handleNotFoundResponse ();
	}
	Debug.out.println ("ConnectionImpl.handleSizeRequest: Returning");
    }

}
