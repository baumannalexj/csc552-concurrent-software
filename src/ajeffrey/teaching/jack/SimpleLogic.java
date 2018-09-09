package ajeffrey.teaching.jack;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.io.DevNull;

import java.io.PrintWriter;

/**
 * A simplistic implementation of the business logic for the Jack
 * application.  So simplistic in fact that it doesn't work!
 * @version 1.0.1
 * @author Alan Jeffrey
 */
public interface SimpleLogic {

    public static LogicFactory factory = new SimpleLogicFactoryImpl ();

}

class SimpleLogicFactoryImpl implements LogicFactory {
    
    public Logic build () { return new SimpleLogicImpl (); }

}

class SimpleLogicImpl implements Logic {

    protected PrintWriter out = DevNull.printWriter;

    protected boolean flag = true;

    protected final String message = 
	"\nAll work and no play makes Jack a dull boy.";

    protected int offset = 0;

    protected void printChar () {
	offset = (offset + 1) % (message.length ());
	char c = message.charAt (offset);
	Debug.out.println ("SimpleLogic.printChar (): Printing " + c);
	out.print (c);
	out.flush ();
    }

    public void setPrintWriter (final PrintWriter out) {
	Debug.out.println ("SimpleLogic.setPrintWriter (): Starting");
	this.out = out;
	Debug.out.println ("SimpleLogic.setPrintWriter (): Returning");
    }

    public void suspend () {
	Debug.out.println ("SimpleLogic.suspend (): Starting");
	flag = false;
	Debug.out.println ("SimpleLogic.suspend (): Returning");
    }

    public void resume () {
	Debug.out.println ("SimpleLogic.resume (): Starting");
	flag = true;
	Debug.out.println ("SimpleLogic.resume (): Returning");
    }

    public void start () {
	Debug.out.println ("SimpleLogic.start (): Starting");
	try {
	    while (true) {
		Thread.sleep (200);
		if (flag) {
		    printChar ();
		} else {
		    Debug.out.println ("SimpleLogic.start (): suspended");
		}
	    }
	} catch (InterruptedException ex) {
	    Debug.out.println 
		("SimpleLogic.start (): Caught exception " + ex);
	}
        Debug.out.println ("SimpleLogic.start (): Returning");
    }

}
