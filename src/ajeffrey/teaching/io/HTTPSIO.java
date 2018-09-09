package ajeffrey.teaching.io;

import javax.net.ssl.SSLSocket;
import java.net.UnknownHostException;
import java.net.MalformedURLException;

import com.macfaq.io.SafeBufferedReader;
import com.macfaq.io.SafePrintWriter;
import java.io.IOException;

import ajeffrey.teaching.debug.Debug;

/**
 * An interface for constructing HTTPS connections for 
 * a URL.  This is a cut-down version of functionality
 * implemented by the URL class, and is intended for teaching
 * purposes.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface HTTPSIO {

    public SafeBufferedReader get (String url)
	throws IOException, UnknownHostException, MalformedURLException;

    public SafeBufferedReader get (String url, int defaultPort)
	throws IOException, UnknownHostException, MalformedURLException;

    public static HTTPSIO singleton = new HTTPSIOImpl ();

}

class HTTPSIOImpl implements HTTPSIO {

    public SafeBufferedReader get (final String url)
	throws IOException, UnknownHostException, MalformedURLException
    {
	return get (url, 443);
    }

    public SafeBufferedReader get (final String url, final int defaultPort)
	throws IOException, UnknownHostException, MalformedURLException
    {
	Debug.out.println ("HTTPSIOImpl.get: Starting");
	final SSLSocket socket = 
	    SSLSocketIO.singleton.buildSSLSocket (url, defaultPort);
	final SafePrintWriter out = 
	    SSLSocketIO.singleton.buildSafePrintWriter (socket, "\n");
	final SafeBufferedReader result = 
	    SSLSocketIO.singleton.buildSafeBufferedReader (socket);
	final String getMsg = 
	    "GET " + url + " HTTP/1.0";
	Debug.out.println ("HTTPSIOImpl.get: Sending " + getMsg);
	out.println (getMsg);
	out.println ("");
	Debug.out.println ("HTTPSIOImpl.get: Waiting for response");
	String line;
	do {
	    line = result.readLine (); 
	    Debug.out.println ("HTTPSIOImpl.get: Discarding " + line);
	} while (line.length () > 0);
	Debug.out.println ("HTTPSIOImpl.get: Returning");
	return result;
    }

}

