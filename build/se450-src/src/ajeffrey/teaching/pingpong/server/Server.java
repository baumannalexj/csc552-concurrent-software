package ajeffrey.teaching.pingpong.server;

import java.net.Socket;
import java.net.ServerSocket;

import com.macfaq.io.SafeBufferedReader;
import com.macfaq.io.SafePrintWriter;

import java.io.IOException;

import ajeffrey.teaching.io.SocketIO;

import ajeffrey.teaching.debug.Debug;

/**
 * An Ping-Pong server class.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface Server {

    public void start ();
    public final static ServerFactory factory = new ServerFactoryImpl ();

}

class ServerFactoryImpl implements ServerFactory {

    public Server build (final int portNumber) {
	return new ServerImpl (portNumber);
    }

}

class ServerImpl implements Server {
    
    final int portNumber;

    ServerImpl (final int portNumber) { 
	this.portNumber = portNumber; 
	Debug.out.println ("ServerImpl: built");
    }

    public void start () {
	Debug.out.println ("ServerImpl.start: Starting");
	try {
	    Debug.out.println ("ServerImpl.start: Opening server socket");
	    final ServerSocket serverSocket = new ServerSocket (2000);
	    while (true) {
		Debug.out.println ("ServerImpl.start: Waiting for connection");
		final Socket socket = serverSocket.accept ();
		Debug.out.println ("ServerImpl.start: Got connection");
		final Task task = new TaskImpl (socket);
		Debug.out.println ("ServerImpl.start: Executing task");
		Executor.singleton.execute (task);
	    }
	} catch (final IOException ex) {
	    Debug.out.println ("ServerImpl.start: Caught " + ex);
	}
    }

}

class TaskImpl implements Task {

    final Socket socket;
    final SafeBufferedReader in;
    final SafePrintWriter out;

    TaskImpl (final Socket socket) throws IOException {
	this.socket = socket;
	this.in = SocketIO.singleton.buildSafeBufferedReader (socket);
	this.out = SocketIO.singleton.buildSafePrintWriter (socket,"\n");
    }

    public void run () {
	try {
	    Debug.out.println ("Task.run: Starting");
	    out.println ("PINGPONG server ready.");
	    String line = in.readLine ();
	    while ((line !=null) && (!line.startsWith ("QUIT"))) {
		Debug.out.println ("Task.run: got " + line);
		if (line.startsWith ("PING")){
		    Debug.out.println ("Task.run: Printing PONG message");
		    out.println ("PONG");
		} else if (line.startsWith ("PONG")) {
		    Debug.out.println ("Task.run: Printing PING message");
		    out.println ("PING");
		} else {
		    Debug.out.println ("Task.run: Printing error message");
		    out.println ("ERROR");
		}
		line = in.readLine ();
	    }
	    Debug.out.println ("Task.run: Returning");
	} catch (final IOException ex) {
	    Debug.out.println ("Task.run: Caught " + ex);
	} finally {
	    try {
		socket.close ();
	    } catch (final IOException ex) {
		Debug.out.println ("Task.run: Caught " + ex + " on closing");
	    }
	}
    }

    public void cancel () {	
	try {
	    Debug.out.println ("Task.cancel: Starting");
	    out.println ("PINGPONG server to busy.");
	} catch (final IOException ex) {
	    Debug.out.println ("Task.cancel: Caught " + ex);
	} finally {
	    try {
		socket.close ();
	    } catch (final IOException ex) {
		Debug.out.println ("Task.cancel: Caught " + ex + " on closing");
	    }
	}
    }

}
