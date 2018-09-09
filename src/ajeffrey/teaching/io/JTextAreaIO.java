package ajeffrey.teaching.io;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;

import javax.swing.JTextArea;
import ajeffrey.teaching.debug.Debug;

/**
 * A class for performing IO to a text area.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface JTextAreaIO {
    
    /**
     * Build an output stream for writing to the given text area.
     * @param ta the text area to write to
     * @return an output stream which writes to that text area.
     */
    public OutputStream buildOutputStream (JTextArea ta);

    /**
     * Build a print stream for writing to the given text area.
     * @param ta the text area to write to
     * @return a print stream which writes to that text area.
     */ 
    public PrintStream buildPrintStream (JTextArea ta);

    /**
     * Build a writer for writing to the given text area.
     * @param ta the text area to write to
     * @return a writer which writes to that text area.
     */
    public Writer buildWriter (JTextArea ta);

    /**
     * Build a print writer for writing to the given text area.
     * @param ta the text area to write to
     * @return a print writer which writes to that text area.
     */
    public PrintWriter buildPrintWriter (JTextArea ta);

    /**
     * A factory object for building all these IO streams.
     */
    public static final JTextAreaIO singleton = new JTextAreaIOImpl ();

}

class JTextAreaIOImpl implements JTextAreaIO {

    public OutputStream buildOutputStream (final JTextArea ta) {
	return new JTextAreaOutputStream (ta);
    }

    public PrintStream buildPrintStream (final JTextArea ta) {
	return new PrintStream (buildOutputStream (ta), true);
    }

    public Writer buildWriter (final JTextArea ta) {
	return new JTextAreaWriter (ta);
    }

    public PrintWriter buildPrintWriter (final JTextArea ta) {
	return new PrintWriter (buildOutputStream (ta), true);
    }


}

class JTextAreaWriter extends Writer {

    protected final JTextArea ta;

    protected JTextAreaWriter (final JTextArea ta) {
 	this.ta = ta;
    }

    public void write (final char[] cbuf, final int off, final int len) {
	write (new String (cbuf, off, len));
    }

    public void write (final String msg) {
	ta.append (msg);
    }

    public void close () {}
    
    public void flush () {}

}

class JTextAreaOutputStream extends OutputStream {

    protected final JTextArea ta;

    protected JTextAreaOutputStream (final JTextArea ta) {
	this.ta = ta;
    }

    public void write (final int b) {
	write (String.valueOf ((char)b));
    }

    public void write (final byte[] b) {
	write (new String (b));
    }

    public void write (final byte[] b, final int off, final int len) {
	write (new String (b, off, len));
    }

    public void write (final String msg) {
	ta.append (msg);
    }

    public void close () {}

    public void flush () {}

}
