package ajeffrey.teaching.jack;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.io.DevNull;

import java.io.PrintWriter;

/**
 * A multi-threaded implementation of the business logic for the Jack
 * application.  This version uses Thread.suspend/resume to handle the
 * suspend and resume calls, which works, but is deprecated.
 * @version 1.0.1
 * @author Alan Jeffrey
 */
public interface SuspendResumeLogic {

    public static LogicFactory factory = new SuspendResumeLogicFactoryImpl ();

}

class SuspendResumeLogicFactoryImpl implements LogicFactory {
    
    public Logic build () { return new SuspendResumeLogicImpl (); }

}

class SuspendResumeLogicImpl implements Logic, Runnable {

    protected final Thread thread = new Thread (this);

    protected PrintWriter out = DevNull.printWriter;

    protected final String message = 
	"\nAll work and no play makes Jack a dull boy.";

    protected int offset = 0;

    protected void printChar () {
	offset = (offset + 1) % (message.length ());
	char c = message.charAt (offset);
	Debug.out.println ("SuspendResumeLogic.printChar (): Printing " + c);
	out.print (c);
	out.flush ();
    }

    public void setPrintWriter (final PrintWriter out) {
	Debug.out.println ("SuspendResumeLogic.setPrintWriter (): Starting");
	this.out = out;
	Debug.out.println ("SuspendResumeLogic.setPrintWriter (): Returning");
    }

    public void suspend () {
	Debug.out.println ("SuspendResumeLogic.suspend (): Starting");
	thread.suspend ();
	Debug.out.println ("SuspendResumeLogic.suspend (): Returning");
    }

    public void resume () {
	Debug.out.println ("SuspendResumeLogic.resume (): Starting");
	thread.resume ();
	Debug.out.println ("SuspendResumeLogic.resume (): Returning");
    }

    public void run () { 
	Debug.out.println ("SuspendResumeLogic.run (): Starting");
	try {
	    while (true) {
		Thread.sleep (200);
		printChar ();
	    }
	} catch (InterruptedException ex) {
	    Debug.out.println 
		("SuspendResumeLogic.run (): Caught exception " + ex);
	}
        Debug.out.println ("SuspendResumeLogic.run (): Returning");
    }

    public void start () {
        Debug.out.println ("SuspendResumeLogic.start (): Starting");
	thread.start ();
        Debug.out.println ("SuspendResumeLogic.start (): Returning");
    }

}
