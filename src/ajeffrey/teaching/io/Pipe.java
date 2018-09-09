package ajeffrey.teaching.io;

import java.io.Reader;
import java.io.Writer;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import ajeffrey.teaching.debug.Debug;

public interface Pipe {

    public void connect () throws IOException;

    public static PipeFactory factory = new PipeFactoryImpl ();

}

class PipeFactoryImpl implements PipeFactory {

    public Pipe build (final Reader in, final Writer out) {
	return new PipeRW (in, out);
    }

    public Pipe build (final InputStream in, final OutputStream out) {
	return new PipeStream (in, out);
    }

}

class PipeRW implements Pipe {

    protected final Reader in;
    protected final Writer out;

    protected PipeRW (final Reader in, final Writer out) {
	this.in = in; 
	this.out = out;
	Debug.out.println ("PipeRW: Built");
    }

    public void connect () throws IOException  {
	Debug.out.println ("PipeRW.connect: Starting");
	final char[] buffer = new char[1024];
	for (int i=0; i>=0; i=in.read (buffer)) {
	    if (i > 0) {
		Debug.out.println 
		    ("PipeRW.connect: out.write (" + 
		     new String (buffer, 0, i) + ")");
		out.write (buffer, 0, i);
		out.flush ();
	    } else {
		Debug.out.println ("PipeRW.connect: read nothing");
	    }
	}
	Debug.out.println ("PipeRW.connect: Returning");
    }

}

class PipeStream implements Pipe {

    protected final InputStream in;
    protected final OutputStream out;

    protected PipeStream (final InputStream in, final OutputStream out) {
	this.in = in; 
	this.out = out;
	Debug.out.println ("PipeStream: Built");
    }

    public void connect () throws IOException  {
	Debug.out.println ("PipeStream.connect: Starting");
	final byte[] buffer = new byte[1024];
	for (int i=0; i>=0; i=in.read (buffer)) {
	    if (i > 0) {
		Debug.out.println 
		    ("PipeStream.connect: out.write (" + 
		     new String (buffer, 0, i) + ")");
		out.write (buffer, 0, i);
		out.flush ();
	    } else {
		Debug.out.println ("PipeStream.connect: read nothing");
	    }
	}
	Debug.out.println ("PipeStream.connect: Returning");
    }

}
