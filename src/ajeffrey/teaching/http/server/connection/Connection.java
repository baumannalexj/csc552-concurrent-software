package ajeffrey.teaching.http.server.connection;

import java.net.Socket;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.net.URL;
import java.net.MalformedURLException;

import ajeffrey.teaching.io.Pipe;
import ajeffrey.teaching.io.SocketIO;
import ajeffrey.teaching.debug.Debug;

/**
 * A class to handle a single HTTP connection.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface Connection {

    /**
     * Start servicing the connection.
     */
    public void start ();

    /**
     * A factory for building new connections
     */
    public ConnectionFactory factory = new ConnectionFactoryImpl ();

}

class ConnectionFactoryImpl implements ConnectionFactory {

    public Connection build (final Socket socket) {
	return new ConnectionImpl (socket);
    }

}

class ConnectionImpl implements Connection, Runnable {

    protected final Socket socket;
    protected final Thread thread = new Thread (this);

    protected final String error400Response = 
	"HTTP/1.0 400 Bad Request\n" +
	"Content-Type: text/html; charset=iso-8859-1\n" +
	"\n" +
	"<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
	"<HTML><HEAD>\n" +
	"<TITLE>400 Bad Request</TITLE>\n" +
	"</HEAD><BODY>\n" +
	"<H1>Bad Request</H1>\n" +
	"Your browser sent a request that this server could not understand.<P>\n" +
	"</BODY></HTML>";

    protected final String error404Response =
	"HTTP/1.0 404 Not Found\n" +
	"Content-Type: text/html; charset=iso-8859-1\n" +
	"\n" +
	"<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
	"<HTML><HEAD>\n" +
	"<TITLE>404 Not Found</TITLE>\n" +
	"</HEAD><BODY>\n" +
	"<H1>Not Found</H1>\n" +
	"The requested URL was not found on this server.<P>\n" +
	"</BODY></HTML>";

    protected final String ok200Response =
	"HTTP/1.0 200 OK";

    protected ConnectionImpl (final Socket socket) {
	this.socket = socket;
	Debug.out.println ("ConnectionImpl: Built");
    }

    public void run () {
	try {
	    Debug.out.println ("ConnectionImpl.run : Starting");
	    Debug.out.println ("ConnectionImpl.run : Building IO");
	    final BufferedReader in = 
		SocketIO.singleton.buildBufferedReader (socket);
	    final PrintStream out = 
		SocketIO.singleton.buildPrintStream (socket);
	    try {
		Debug.out.println ("ConnectionImpl.run : Waiting for request");
		final String request = in.readLine ();
		Debug.out.println ("ConnectionImpl.run : Received " + request);
		Debug.out.println ("ConnectionImpl.run : Waiting for blank line");
		String line;
		do {
		    line = in.readLine ();
		    Debug.out.println ("ConnectionImpl.run : Received " + line);
		} while (!line.equals (""));
		Debug.out.println ("ConnectionImpl.run : Got blank line");
		if (request.startsWith ("GET ") && request.endsWith (" HTTP/1.0")) {
		    Debug.out.println ("ConnectionImpl.run : Got OK request");
		    String urlString = 
			request.substring (4, request.length () - 9);
		    Debug.out.println ("ConnectionImpl.run : Parsed `" + urlString + "'");
		    if (!urlString.startsWith ("http://")) {
			if (!urlString.startsWith ("/")) {
			    urlString = "/" + urlString;
			}
			urlString = "http://localhost" + urlString;
		    }
		    if (urlString.endsWith ("/")) {
			urlString = urlString + "index.html";
		    }
		    String contentType = "Content-type: unknown";
		    if (urlString.endsWith (".gif")) {
			contentType = "Content-type: image/gif";
		    } else if (urlString.endsWith (".html")) {
			contentType = "Content-type: text/html";
		    }
		    final URL url = new URL (urlString);
		    Debug.out.println ("ConnectionImpl.run : Parsed url");
		    final String fileName = url.getFile ();
		    Debug.out.println ("ConnectionImpl.run : Opening " + fileName);
		    final InputStream file = new FileInputStream ("." + fileName);
		    Debug.out.println ("ConnectionImpl.run : Printing header");
		    out.println (ok200Response);
		    out.println (contentType);
		    out.println ();
		    Debug.out.println ("ConnectionImpl.run : Building pipe");
		    final Pipe pipe = Pipe.factory.build (file, out);
		    Debug.out.println ("ConnectionImpl.run : Starting pipe");
		    pipe.connect ();
		    file.close ();
		} else {
		    Debug.out.println ("ConnectionImpl.run : Malformed request");
		    out.println (error400Response);
		}
	    } catch (final FileNotFoundException ex) {
		Debug.out.println ("ConnectionImpl.run : Caught inner " + ex);
		out.println (error404Response);
	    } catch (final MalformedURLException ex) {
		Debug.out.println ("ConnectionImpl.run : Caught inner " + ex);
		out.println (error400Response);
	    } catch (final IOException ex) {
		Debug.out.println ("ConnectionImpl.run : Caught inner " + ex);
		out.println (error400Response);
	    } finally {
		Debug.out.println ("ConnectionImpl.run : Calling in.close ()");
		in.close ();
		Debug.out.println ("ConnectionImpl.run : Calling out.close ()");
		out.close ();
	    }
	} catch (final IOException ex) {
	    Debug.out.println ("ConnectionImpl.run : Caught outer " + ex);
	} finally {
	    try {
		Debug.out.println ("ConnectionImpl.run : Calling socket.close ()");
		socket.close ();
	    } catch (final IOException ex) {
		Debug.out.println ("ConnectionImpl.run : Caught closing " + ex);
	    } finally {
		Debug.out.println ("ConnectionImpl.run : Returning");
	    }
	}
    }

    public void start () {
	Debug.out.println ("ConnectionImpl.start : Starting");
	thread.start ();
	Debug.out.println ("ConnectionImpl.start : Returning");
    }

}
