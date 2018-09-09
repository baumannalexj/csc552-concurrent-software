package ajeffrey.teaching.io;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * This class is a hack!
 * <p>It provides an interruptable I/O stream built on top of
 * a non-interruptable stream.  To do this, it has to use
 * polling under the hood, so this class will busy-wait rather
 * than blocking.  This is very annoying, but any other solution
 * runs into the problem that Sun removed the interruptable I/O
 * features from Java in version 1.3 without really telling anyone:
 * see bug #4103109, #4154947 on the Java Developers' Connection
 * at http://developer.java.sun.com/developer/bugParade/
 * (registration required).</p>
 * <p>You can set the minimum and maximum delays (the stream uses
 * exponential backoff) either by setting the default values,
 * or in the factory's build method.</p>
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface InterruptableIO {

    /**
     * Build a new interruptable input stream.
     * @param in the input stream to read from
     * @return a new interruptable input stream, which reads from in
     */
    public InputStream build (InputStream in);
    
    /**
     * Build a new interruptable input stream.
     * @param in the input stream to read from
     * @param minDelay the minimum time to wait between attempts to read in ms
     * @param maxDelay the maximum time to wait between attempts to read in ms
     * @return a new interruptable input stream, which reads from in
     */    
    public InputStream build (InputStream in, int minDelay, int maxDelay);

    /**
     * Set the default delays.
     * These are the delay parameters used by
     * <code>build (InputStream)</code>.
     * @param minDelay the minimum time to wait between attempts to read in ms
     * @param maxDelay the maximum time to wait between attempts to read in ms
     */
    public void setDefaultDelays (int minDelay, int maxDelay);

    /**
     * An object which builds new input streams.
     */
    public static InterruptableIO singleton = new InterruptableIOImpl ();

}

class InterruptableIOImpl implements InterruptableIO {

    protected int minDelay = 1;
    protected int maxDelay = 2048;

    public InputStream build (final InputStream in) {
	return build (in, minDelay, maxDelay);
    }

    public InputStream build 
	(final InputStream in, final int minDelay, final int maxDelay) 
    {
	return new InterruptableInputStream (in, minDelay, maxDelay);
    }

    public void setDefaultDelays (final int minDelay, final int maxDelay) {
	this.minDelay = minDelay;
	this.maxDelay = maxDelay;
    }

}

class InterruptableInputStream extends FilterInputStream {

    protected final int minDelay;
    protected final int maxDelay;
    protected int delay;
    protected int bytesTransferred = 0;

    protected InterruptableInputStream 
	(final InputStream in, final int minDelay, final int maxDelay)
    {
	super (in);
	this.minDelay = minDelay;
	this.maxDelay = maxDelay;
	this.delay = minDelay;
    }

    protected void pollForAvailable () throws IOException {
	delay = delay / 2;
	if (delay < minDelay) { delay = minDelay; }
	try {
	    while (available () < 1) {
		Thread.currentThread ().sleep (delay);
		delay = delay * 2;
		if (delay > maxDelay) { delay = maxDelay; }
	    }
	} catch (final InterruptedException ex) {
	    InterruptedIOException toThrow = 
		new InterruptedIOException (ex.toString ());
	    toThrow.bytesTransferred = bytesTransferred;
	    throw toThrow;
	}
    }

    public int read (final byte[] buff, final int off, final int len) 
    throws IOException {
	pollForAvailable ();
	final int result = super.read (buff, off, len);
	if (result > 0) { bytesTransferred = bytesTransferred + result; }
	return result;
    }

    public int read (final byte[] buff) 
    throws IOException {
	pollForAvailable ();
	final int result = super.read (buff);
	if (result > 0) { bytesTransferred = bytesTransferred + result; }
	return result;
    }

    public int read ()
    throws IOException {
	pollForAvailable ();
	final int result = super.read ();
	if (result > 0) { bytesTransferred++; }
	return result;
    }

}
