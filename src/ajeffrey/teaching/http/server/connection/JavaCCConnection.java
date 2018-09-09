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

import ajeffrey.teaching.http.server.parser.ParseException;
import ajeffrey.teaching.http.server.parser.HTTPParser;
import ajeffrey.teaching.http.server.parser.HTTPRequestHandler;

import ajeffrey.teaching.io.Pipe;
import ajeffrey.teaching.io.SocketIO;
import ajeffrey.teaching.debug.Debug;

import com.macfaq.io.SafePrintWriter;

/**
 * A class to handle a single HTTP connection.
 * This implementation uses the JavaCC parser generator to
 * parse incoming requests.
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public interface JavaCCConnection {

    /**
     * A factory for building new connections
     */
    public ConnectionFactory factory = new JavaCCConnectionFactoryImpl ();

}

class JavaCCConnectionFactoryImpl implements ConnectionFactory {

    public Connection build (final Socket socket) {
	return new JavaCCConnectionImpl (socket);
    }

}

class JavaCCConnectionImpl implements Connection, Runnable, HTTPRequestHandler {

    protected final Thread thread = new Thread (this);
    protected final Socket socket;
    protected HTTPParser parser;
    protected SafePrintWriter out;

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

    protected JavaCCConnectionImpl (final Socket socket) {
	this.socket = socket;
	Debug.out.println ("JavaCCConnectionImpl: Built");
    }

    public void handleGetRequest (final URL url) {
	Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: Starting");
	Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: url = " + url);
	try {
	    String fileName = url.getFile ();
            if (fileName.equals ("") || fileName.endsWith ("/")) {
		fileName = fileName + "index.html";
	    }
	    final String contentType;
	    if (fileName.endsWith (".gif")) {
		contentType = "Content-type: image/gif";
	    } else if (fileName.endsWith (".html")) {
		contentType = "Content-type: text/html";
	    } else {
		contentType = "Content-type: unknown";
	    }
	    Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: Opening " + fileName);
	    final InputStream file = new FileInputStream ("." + fileName);
	    Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: Printing header");
	    out.println (ok200Response);
	    out.println (contentType);
	    out.println ();
	    Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: Building pipe");
	    final Pipe pipe = Pipe.factory.build (file, socket.getOutputStream ());
	    Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: Starting pipe");
	    pipe.connect ();
	    file.close ();
	} catch (final FileNotFoundException ex) {
	    try {
		Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: Caught " + ex);
		out.println (error404Response);
	    } catch (final IOException ioex) {
		Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: Caught " + ioex);
	    }
	} catch (final IOException ex) {
	    try {
		Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: Caught " + ex);
		out.println (error400Response);	    
	    } catch (final IOException ioex) {
		Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: Caught " + ioex);
	    }
	} finally {
	    Debug.out.println ("JavaCCConnectionImpl.handleGetRequest: Returning");
	}
    }

    public void run () {
	try {
	    try {
		Debug.out.println ("JavaCCConnectionImpl.run: Starting");
		Debug.out.println ("JavaCCConnectionImpl.run: Building IO");
		out = SocketIO.singleton.buildSafePrintWriter (socket, "\n");
		parser = new HTTPParser (socket.getInputStream ());
		Debug.out.println ("JavaCCConnectionImpl.run: Adding handler");
		parser.addHTTPRequestHandler (this);
		Debug.out.println ("JavaCCConnectionImpl.run: Parsing");
		parser.HTTPRequest ();
		Debug.out.println ("JavaCCConnectionImpl.run: Done parsing");
	    } catch (ParseException ex) {
		Debug.out.println ("JavaCCConnectionImpl.run: Caught " + ex);
		out.println (error400Response);	    
	    }
	} catch (final IOException ex) {
	    Debug.out.println ("JavaCCConnectionImpl.run: Caught " + ex);
	} finally {
	    try {
		Debug.out.println ("JavaCCConnectionImpl.run: Calling socket.close ()");
		socket.close ();
	    } catch (final IOException ex) {
		Debug.out.println ("JavaCCConnectionImpl.run: Caught closing " + ex);
	    } finally {
		Debug.out.println ("JavaCCConnectionImpl.run: Returning");
	    }
	}
    }

    public void start () {
	Debug.out.println ("JavaCCConnectionImpl.start: Starting");
	thread.start ();
	Debug.out.println ("JavaCCConnectionImpl.start: Returning");
    }

}
