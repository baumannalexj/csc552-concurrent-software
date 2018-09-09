package ajeffrey.teaching.io;

import java.net.Socket;
import java.net.UnknownHostException;
import java.net.MalformedURLException;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

import ajeffrey.teaching.debug.Debug;

/**
 * An interface for constructing HTTP connections for 
 * a URL.  This is a cut-down version of functionality
 * implemented by the URL class, and is intended for teaching
 * purposes.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface HTTPIO {

    public BufferedReader get (String url)
	throws IOException, UnknownHostException, MalformedURLException;

    public BufferedReader get (String url, int defaultPort)
	throws IOException, UnknownHostException, MalformedURLException;

    public static HTTPIO singleton = new HTTPIOImpl ();

}

class HTTPIOImpl implements HTTPIO {

    public BufferedReader get (final String url)
	throws IOException, UnknownHostException, MalformedURLException
    {
	return get (url, 80);
    }

    public BufferedReader get (final String url, final int defaultPort)
	throws IOException, UnknownHostException, MalformedURLException
    {
	Debug.out.println ("HTTPIOImpl.get: Starting");
	final Socket socket = 
	    SocketIO.singleton.buildSocket (url, defaultPort);
	final PrintWriter out = 
	    SocketIO.singleton.buildPrintWriter (socket);
	final BufferedReader result = 
	    SocketIO.singleton.buildBufferedReader (socket);
	final String getMsg = 
	    "GET " + url + " HTTP/1.0";
	Debug.out.println ("HTTPIOImpl.get: Sending " + getMsg);
	out.println (getMsg);
	out.println ("");
	Debug.out.println ("HTTPIOImpl.get: Waiting for response");
	String line;
	do {
	    line = result.readLine (); 
	    Debug.out.println ("HTTPIOImpl.get: Discarding " + line);
	} while (line.length () > 0);
	Debug.out.println ("HTTPIOImpl.get: Returning");
	return result;
    }

}

