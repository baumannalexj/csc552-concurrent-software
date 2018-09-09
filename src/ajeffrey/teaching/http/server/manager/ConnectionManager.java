package ajeffrey.teaching.http.server.manager;

import ajeffrey.teaching.http.server.connection.Connection;
import ajeffrey.teaching.http.server.connection.ConnectionFactory;
import ajeffrey.teaching.debug.Debug;

import java.io.IOException;

import java.net.Socket;
import java.net.ServerSocket;

/**
 * A connection manager for an HTTP server.
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public interface ConnectionManager {

    /**
     * Start the main connection manager.
     */
    public void start () throws IOException;

    /**
     * A factory for building new connection managers.
     */
    public static ConnectionManagerFactory factory
	= new ConnectionManagerFactoryImpl ();

}

class ConnectionManagerFactoryImpl implements ConnectionManagerFactory {

    public ConnectionManager build 
	(final int port, final ConnectionFactory factory) 
    {
	return new ConnectionManagerImpl (port, factory);
    }

}

class ConnectionManagerImpl implements ConnectionManager {

    protected final int port;
    protected final ConnectionFactory factory;

    protected ConnectionManagerImpl 
	(final int port, final ConnectionFactory factory) 
    {
	this.port = port;
	this.factory = factory;
	Debug.out.println ("ConnectionManagerImpl: Built");
    }

    public void start () throws IOException {
	Debug.out.println ("ConnectionManagerImpl.start: Starting");
	final ServerSocket serverSocket = new ServerSocket (port);
	while (true) {
	    Debug.out.println 
		("ConnectionManagerImpl.start: Waiting for connection");
	    final Socket socket = serverSocket.accept ();
	    Debug.out.println 
		("ConnectionManagerImpl.start: Got a connection from " +
		 socket.getInetAddress ().getHostName ());
	    final Connection connection = factory.build (socket);
	    Debug.out.println 
		("ConnectionManagerImpl.start: Starting connection");
	    connection.start ();
	    Debug.out.println 
		("ConnectionManagerImpl.start: Connection started");
	}
    }

}
