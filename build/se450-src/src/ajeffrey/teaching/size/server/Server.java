package ajeffrey.teaching.size.server;

import ajeffrey.teaching.size.event.SizeRequestHandler;
import ajeffrey.teaching.size.event.SizeResponseHandler;
import ajeffrey.teaching.size.event.ParseException;

import ajeffrey.teaching.io.SocketIO;
import ajeffrey.teaching.debug.Debug;

import com.macfaq.io.SafePrintWriter;

import java.net.Socket;
import java.net.ServerSocket;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * The business logic for a server which lists the size of files.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface Server {

    /**
     * Start the server.
     * @exception IOException thrown when there is network trouble
     */
    public void start () throws IOException;

    /**
     * Stop the server.
     * @exception IOException thrown when there is network trouble
     */
    public void stop () throws IOException;

    /**
     * A factory for building servers
     */
    public static ServerFactory factory = new ServerFactoryImpl ();

}

class ServerFactoryImpl implements ServerFactory {

    public Server build (final int port) {
	return new ServerImpl (port);
    }

}

class ServerImpl implements Server {

    protected final int port;
    protected ServerSocket serverSocket;
    protected boolean running = true;

    protected ServerImpl (final int port) {
	this.port = port;
	Debug.out.println ("ServerImpl: Built");
    }

    public void start () throws IOException {
	Debug.out.println ("ServerImpl.start: Starting");
	Debug.out.println ("ServerImpl.start: Creating server socket on port " + port);
	serverSocket = new ServerSocket (port);
	while (running) {
	    Debug.out.println ("ServerImpl.start: Listening");
	    final Socket socket = serverSocket.accept ();
	    Debug.out.println ("ServerImpl.start: Building connection");
	    final Connection connection = Connection.factory.build (socket);
	    Debug.out.println ("ServerImpl.start: Starting connection");
	    connection.start ();
	}
	Debug.out.println ("ServerImpl.start: Returning");
    }

    public void stop () throws IOException {
	Debug.out.println ("ServerImpl.stop: Starting");
	running = false;
	Debug.out.println ("ServerImpl.stop: Closing server socket");
	serverSocket.close ();
	Debug.out.println ("ServerImpl.stop: Returning");
    }

}

