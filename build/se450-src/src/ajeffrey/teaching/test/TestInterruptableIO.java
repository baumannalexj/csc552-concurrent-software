package ajeffrey.teaching.test;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.io.InterruptableIO;

import java.io.InputStream;

/**
 * This class tests to see whether interruptable IO has been implemented
 * correctly.  According to the Java API, it should produce output like:
 * <pre>
 * Thread 1: Sleeping
 * Thread 2: Blocking...
 * Thread 1: Interrupting
 * Thread 1: OK
 * Thread 2: Thrown: InterruptedIOException ...
 * </pre>
 * instead, most javas will produce:
 * <pre>
 * Thread 1: Sleeping
 * Thread 2: Blocking...
 * Thread 1: Interrupting
 * Thread 1: OK
 * </pre>
 * <p>ie the thread which is blocked waiting on I/O isn't interrupted
 * properly.  This appears to be a standing issue with the JDK, and
 * is referred to as the `interruptable I/O problem' on their
 * developer forum.</p>
 * <p>Sun are aware of this bug, and have declared it `unfixable'
 * because of implementation difficulties under Win95(!).
 * Their response is to deprectate interruptable IO, which just punts
 * the problem back to the programmer, and produces horrible space
 * leaks.  This is a mess!</p>
 * <p>See bug #4103109, #4154947 on the Java Developers' Connection
 * at http://developer.java.sun.com/developer/bugParade/
 * (registration required).</p>
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public class TestInterruptableIO {

    // Pick one of the following:
    public static InputStream test =
	System.in;
    //	InterruptableIO.singleton.build (System.in);

    public static void main (String[] args) throws Exception {
	Debug.out.addPrintStream (System.err);
	Thread thread = new Thread () { public void run () {
	    try {
		Debug.out.println ("Blocking...");
		test.read ();
		Debug.out.println ("Done.");
	    } catch (java.io.IOException ex) {
		Debug.out.println ("Thrown: " + ex);
	    } catch (Exception ex) {
		Debug.out.println ("Thrown more: " + ex);
	    }
	    } };
	thread.start ();
	Debug.out.println ("Sleeping");
	Thread.sleep (2000);
	Debug.out.println ("Interrupting");
	thread.interrupt ();
	Debug.out.println ("OK");
	Thread.sleep (2000);
	System.exit (0);
    }

}
