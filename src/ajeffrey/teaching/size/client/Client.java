package ajeffrey.teaching.size.client;

import ajeffrey.teaching.size.event.SizeRequestSerializer;
import ajeffrey.teaching.size.event.SizeResponseParser;
import ajeffrey.teaching.size.event.SizeResponseHandler;
import ajeffrey.teaching.size.event.ParseException;

import ajeffrey.teaching.debug.Debug;

import java.net.Socket;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * The business logic for a client which finds the size of files on
 * a matching server.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface Client {

    /**
     * Start the client.
     * @exception IOException thrown when there is network trouble
     */
    public void start () throws IOException;

    /**
     * Stop the client.
     * @exception IOException thrown when there is network trouble
     */
    public void stop () throws IOException;

    /**
     * Get the size of a file.
     * @param fileName the name of the file
     * @return the size of the file
     * @exception IOException thrown when there is network trouble
     * @exception FileNotFoundException thown when the file does not exist
     */
    public long getSize (String fileName) throws IOException, FileNotFoundException;

    /**
     * A factory for building clients
     */
    public static ClientFactory factory = new ClientFactoryImpl ();

}

class ClientFactoryImpl implements ClientFactory {

    public Client build (final String hostName, final int port) {
	return new ClientImpl (hostName, port);
    }

}

class ClientImpl implements Client, SizeResponseHandler {

    protected final String hostName;
    protected final int port;
    protected Socket socket;
    protected SizeRequestSerializer serializer;
    protected SizeResponseParser parser;

    protected ClientImpl (final String hostName, final int port) {
	this.hostName = hostName;
	this.port = port;
	Debug.out.println ("ClientImpl: built");
    }

    public void start () throws IOException {
	Debug.out.println ("ClientImpl.start: Starting");
	Debug.out.println ("ClientImpl.start: Opening socket to " + hostName + ":" + port);
	socket = new Socket (hostName, port);
	Debug.out.println ("ClientImpl.start: Building serializer");
	serializer = SizeRequestSerializer.factory.build (socket.getOutputStream ());
	Debug.out.println ("ClientImpl.start: Building parser");
	parser = SizeResponseParser.factory.build (socket.getInputStream ());
	Debug.out.println ("ClientImpl.start: Adding handler");
	parser.addSizeResponseHandler (this);
	Debug.out.println ("ClientImpl.start: Returning");
    }

    public void stop () throws IOException {
	Debug.out.println ("ClientImpl.stop: Starting");
	serializer.handleQuitRequest ();
	socket.close ();
	Debug.out.println ("ClientImpl.stop: Returning");
    }

    protected long size;

    public long getSize (final String fileName) throws FileNotFoundException, IOException {
	Debug.out.println ("ClientImpl.getSize: Starting");
	try {
	    Debug.out.println ("ClientImpl.getSize: Sending size request for " + fileName);
	    serializer.handleSizeRequest (fileName);
	    Debug.out.println ("ClientImpl.getSize: Waiting for response");
	    parser.parseSizeResponse ();
	    Debug.out.println ("ClientImpl.getSize: Got response");
	    if (size < 0) {
		Debug.out.println ("ClientImpl.getSize: Oh dear, file not found");
		throw new FileNotFoundException ();
	    } else {
		Debug.out.println ("ClientImpl.getSize: File found, size = " + size);
		return size;
	    }
	} catch (final ParseException ex) {
	    Debug.out.println ("ClientImpl.getSize: Caught " + ex);
	    throw new IOException ("Parse error: " + ex);
	} finally {
	    Debug.out.println ("ClientImpl.getSize: Returning");
	}
    }

    public void handleFoundResponse (final String fileName, final long size) throws IOException {
	Debug.out.println ("ClientImpl.handleFoundResponse: Starting");
	Debug.out.println ("ClientImpl.handleFoundResponse: Setting size = " + size);
	this.size = size;
	Debug.out.println ("ClientImpl.handleFoundResponse: Returning");
    }

    public void handleNotFoundResponse () throws IOException {
	Debug.out.println ("ClientImpl.handleNotFoundResponse: Starting");
	this.size = -1;
	Debug.out.println ("ClientImpl.handleNotFoundResponse: Returning");
    }

    public void handleOKResponse () throws IOException {
	Debug.out.println ("ClientImpl.handleOKResponse: Starting");
	Debug.out.println ("ClientImpl.handleOKResponse: Returning");
    }

    public void handleEndOfStream () throws IOException {
	Debug.out.println ("ClientImpl.handleEndOfStream: Starting");
	throw new IOException ("Stream closed");
    }

}
