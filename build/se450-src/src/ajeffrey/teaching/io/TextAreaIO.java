package ajeffrey.teaching.io;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;

import java.awt.TextArea;
import ajeffrey.teaching.debug.Debug;

/**
 * A class for performing IO to a text area.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface TextAreaIO {
    
    /**
     * Build an output stream for writing to the given text area.
     * @param ta the text area to write to
     * @return an output stream which writes to that text area.
     */
    public OutputStream buildOutputStream (TextArea ta);

    /**
     * Build a print stream for writing to the given text area.
     * @param ta the text area to write to
     * @return a print stream which writes to that text area.
     */ 
    public PrintStream buildPrintStream (TextArea ta);

    /**
     * Build a writer for writing to the given text area.
     * @param ta the text area to write to
     * @return a writer which writes to that text area.
     */
    public Writer buildWriter (TextArea ta);

    /**
     * Build a print writer for writing to the given text area.
     * @param ta the text area to write to
     * @return a print writer which writes to that text area.
     */
    public PrintWriter buildPrintWriter (TextArea ta);

    /**
     * A factory object for building all these IO streams.
     */
    public static final TextAreaIO singleton = new TextAreaIOImpl ();

}

class TextAreaIOImpl implements TextAreaIO {

    public OutputStream buildOutputStream (final TextArea ta) {
	return new TextAreaOutputStream (ta);
    }

    public PrintStream buildPrintStream (final TextArea ta) {
	return new PrintStream (buildOutputStream (ta), true);
    }

    public Writer buildWriter (final TextArea ta) {
	return new TextAreaWriter (ta);
    }

    public PrintWriter buildPrintWriter (final TextArea ta) {
	return new PrintWriter (buildOutputStream (ta), true);
    }


}

class TextAreaWriter extends Writer {

    protected final TextArea ta;

    protected TextAreaWriter (final TextArea ta) {
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

class TextAreaOutputStream extends OutputStream {

    protected final TextArea ta;

    protected TextAreaOutputStream (final TextArea ta) {
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
