package ajeffrey.teaching.http.client.logic;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.io.Pipe;
import ajeffrey.teaching.io.HTTPIO;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InterruptedIOException;

import java.net.Socket;

/**
 * A class for building HTTP connections and sending the
 * result to an output stream.
 */
public interface Connection {
    
    public void start ();

    public void stop ();

    public static ConnectionFactory factory = new ConnectionFactoryImpl ();

}

class ConnectionFactoryImpl implements ConnectionFactory {

    public Connection build (final String url, final PrintWriter out) {
	return new ConnectionImpl (url, out);
    }

}

class ConnectionImpl implements Runnable, Connection {

    protected final Thread thread;
    protected final String url;
    protected final PrintWriter out;

    protected ConnectionImpl (final String url, final PrintWriter out) {
	this.url = url;
	this.out = out;
	this.thread = new Thread (this);
	Debug.out.println ("Connection.start: Built");	
    }

    public void start () {
	Debug.out.println ("Connection.start: Starting");
	thread.start ();
	Debug.out.println ("Connection.start: Returning");
    }

    public void run () {
	Debug.out.println ("Connection.run: Starting");
	try {
	    Debug.out.println ("Connection.run: Getting reader");
	    final BufferedReader in = HTTPIO.singleton.get (url);
	    try {
		Debug.out.println ("Connection.run: Building pipe");
		final Pipe pipe = Pipe.factory.build (in, out);
		pipe.connect ();
		Debug.out.println ("Connection.run: Done!");	
	    } catch (InterruptedIOException ex) {
		Debug.out.println ("Connection.run: Inner IO Exception! " + ex);
		out.println ("");
		out.println ("Connection to " + url + " interrupted.");
	    } catch (IOException ex) {
		Debug.out.println ("Connection.run: Inner IO Exception! " + ex);
		out.println ("");
		out.println ("Connection to " + url + " failed.");
	    } finally {
		Debug.out.println ("Connection.run: Closing reader");
		in.close ();
	    }
	} catch (InterruptedIOException ex) {
	    Debug.out.println ("Connection.run: Outer IO Exception! " + ex);
	    out.println ("");
	    out.println ("Connection to " + url + " interrupted.");
	} catch (IOException ex) {
	    Debug.out.println ("Connection.run: Outer IOException! " + ex);
	    out.println ("");
	    out.println ("Connection to " + url + " failed.");
	} catch (RuntimeException ex) {
	    Debug.out.println ("Connection.run: Outer IOException! " + ex);
	    throw ex;
	}
    }

    public void stop () {
	Debug.out.println ("Connection.stop: Starting");
	Debug.out.println ("Connection.stop: Calling thread.interrupt ()");
	thread.interrupt ();
	Debug.out.println ("Connection.stop: Returning");	
    }

}
