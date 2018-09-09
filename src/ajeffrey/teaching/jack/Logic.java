package ajeffrey.teaching.jack;

import java.io.PrintWriter;

/**
 * The "business logic" for the Jack application.
 * This will print "All work and now play makes jack a dull boy" repeatedly
 * to an output stream.  It provides <code>suspend</code> and 
 * <code>resume</code> methods which will pause and restart the printing.
 * @version 1.0.1
 * @author Alan Jeffrey
 * @see LogicFactory
 */
public interface Logic {

    /**
     * Set the print writer for the logic to print to.
     * @param out the print writer to print to.
     */
    public void setPrintWriter (PrintWriter out);

    /**
     * Start printing.
     */
    public void start ();

    /**
     * Suspend printing until the next resume method call.
     */
    public void suspend ();

    /**
     * Resume printing if we're currently suspended.
     */
    public void resume ();

}
