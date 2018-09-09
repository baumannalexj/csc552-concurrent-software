package ajeffrey.teaching.jack;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.io.DevNull;

import java.io.PrintWriter;

/**
 * A multi-threaded implementation of the business logic for the Jack
 * application.  This uses a separate thread for each window, and
 * uses polling to handle the suspend/resume calls.  This is a <i>very
 * bad idea</i> and is only used here for teaching purposes!  Don't copy
 * this code!
 * @version 1.0.1
 * @author Alan Jeffrey
 */
public interface PollingLogic {

    public static LogicFactory factory = new PollingLogicFactoryImpl ();

}

class PollingLogicFactoryImpl implements LogicFactory {
    
    public Logic build () { return new PollingLogicImpl (); }

}

class PollingLogicImpl implements Logic, Runnable {

    protected final Thread thread = new Thread (this);

    protected PrintWriter out = DevNull.printWriter;

    protected boolean flag = true;

    protected final String message = 
	"\nAll work and no play makes Jack a dull boy.";

    protected int offset = 0;

    protected void printChar () {
	offset = (offset + 1) % (message.length ());
	char c = message.charAt (offset);
	Debug.out.println ("PollingLogic.printChar (): Printing " + c);
	out.print (c);
	out.flush ();
    }

    public void setPrintWriter (final PrintWriter out) {
	Debug.out.println ("PollingLogic.setPrintWriter (): Starting");
	this.out = out;
	Debug.out.println ("PollingLogic.setPrintWriter (): Returning");
    }

    public void suspend () {
	Debug.out.println ("PollingLogic.suspend (): Starting");
	flag = false;
	Debug.out.println ("PollingLogic.suspend (): Returning");
    }

    public void resume () {
	Debug.out.println ("PollingLogic.resume (): Starting");
	flag = true;
	Debug.out.println ("PollingLogic.resume (): Returning");
    }

    public void run () { 
	Debug.out.println ("PollingLogic.run (): Starting");
	try {
	    while (true) {
		Thread.sleep (200);
		if (flag) {
		    printChar ();
		} else {
		    Debug.out.println ("PollingLogic.run (): suspended");
		}
	    }
	} catch (InterruptedException ex) {
	    Debug.out.println 
		("PollingLogic.run (): Caught exception " + ex);
	}
        Debug.out.println ("PollingLogic.run (): Returning");
    }

    public void start () {
        Debug.out.println ("PollingLogic.start (): Starting");
	thread.start ();
        Debug.out.println ("PollingLogic.start (): Returning");
    }

}
