package ajeffrey.teaching.test;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.debug.StepDebugStream;

import ajeffrey.teaching.util.guard.Guard;
import ajeffrey.teaching.util.guard.BuggyGuard;

/**
 * A class which tests a guard class.
 * I ran this under Sun's Linux JDK 1.3 and got:
 * <pre>
 * # HotSpot Virtual Machine Error, Unexpected Signal 11
 * # Please report this error at
 * # http://java.sun.com/cgi-bin/bugreport.cgi
 * #
 * # Error ID: 4F533F4C494E55580E4350500595
 * #
 * # Problematic Thread: prio=1 tid=0x80ba630 nid=0x5971 runnable 
 * #
 * </pre>
 * Hopefully this doesn't happen too often!
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public class TestGuard {

    // Choose whether you want the buggy version or not!
    // public static final Guard guard = Guard.factory.build (false);
    public static final Guard guard = BuggyGuard.factory.build (false);

    public static final Thread threadA = new Thread (new Runnable () {
	    public void run () {
		Debug.out.breakPoint ("A starting");
		guard.setValue (true);
		Debug.out.println ("A done");
	    }
	});

    public static final Thread threadB = new Thread (new Runnable () {
	    public void run () {
		Debug.out.breakPoint ("B starting");
		try {
		    guard.waitForTrue ();
		} catch (final InterruptedException ex) {
		    Debug.out.println ("B interrupted");
		}
		Debug.out.println ("B done");
	    }
	});

    public static void main (String[] args) throws Exception {
	// Send debugging output to a file
	Debug.out.addFile ("TestGuardDebug.txt");
	// Switch on step debugging
	Debug.out.addFactory (StepDebugStream.factory);
	// Start the threads.
	threadA.start ();
	threadB.start ();
	threadA.join ();
	threadB.join ();
	System.exit (0);
    }

}
