package ajeffrey.teaching.jack;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.io.DevNull;
import ajeffrey.teaching.util.guard.Guard;

import java.io.PrintWriter;

/**
 * A multi-threaded implementation of the business logic for the Jack
 * application.  This uses a separate thread for each window, and
 * uses a guard to handle suspend/resume.  This is how I would recommend
 * implementing this application!
 * @version 1.0.1
 * @author Alan Jeffrey
 */
public interface GuardedLogic {

    public static LogicFactory factory = new GuardedLogicFactoryImpl ();

}

class GuardedLogicFactoryImpl implements LogicFactory {
    
    public Logic build () { return new GuardedLogicImpl (); }

}

class GuardedLogicImpl implements Logic, Runnable {

    protected final Thread thread = new Thread (this);

    protected PrintWriter out = DevNull.printWriter;

    protected Guard flag = Guard.factory.build (true);

    protected final String message = 
	"\nAll work and no play makes Jack a dull boy.";

    protected int offset = 0;

    protected void printChar () {
	offset = (offset + 1) % (message.length ());
	char c = message.charAt (offset);
	Debug.out.println ("GuardedLogic.printChar (): Printing " + c);
	out.print (c);
	out.flush ();
    }

    public void setPrintWriter (final PrintWriter out) {
	Debug.out.println ("GuardedLogic.setPrintWriter (): Starting");
	this.out = out;
	Debug.out.println ("GuardedLogic.setPrintWriter (): Returning");
    }

    public void suspend () {
	Debug.out.println ("GuardedLogic.suspend (): Starting");
	flag.setValue (false);
	Debug.out.println ("GuardedLogic.suspend (): Returning");
    }

    public void resume () {
	Debug.out.println ("GuardedLogic.resume (): Starting");
	flag.setValue (true);
	Debug.out.println ("GuardedLogic.resume (): Returning");
    }

    public void run () { 
	Debug.out.println ("GuardedLogic.run (): Starting");
	try {
	    while (true) {
		Thread.sleep (200);
		flag.waitForTrue ();
		printChar ();
	    }
	} catch (InterruptedException ex) {
	    Debug.out.println 
		("GuardedLogic.run (): Caught exception " + ex);
	}
        Debug.out.println ("GuardedLogic.run (): Returning");
    }

    public void start () {
        Debug.out.println ("GuardedLogic.start (): Starting");
	thread.start ();
        Debug.out.println ("GuardedLogic.start (): Returning");
    }

}
