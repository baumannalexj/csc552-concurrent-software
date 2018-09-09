package ajeffrey.teaching.test;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.debug.StepDebugStream;

import ajeffrey.teaching.util.buffer.Buffer;
import ajeffrey.teaching.util.buffer.UnsafeBuffer;

public class TestBuffer {

    public static final Buffer buffer = UnsafeBuffer.factory.build (2);

    public static final Thread threadA = new Thread (new Runnable () {
	    public void run () {
		Debug.out.breakPoint ("A starting");
		buffer.put ("Fred");
		final Object result = buffer.get ();
		System.out.println ("A got " + result);
		Debug.out.assertion (result != null);		
	    }
	});

    public static final Thread threadB = new Thread (new Runnable () {
	    public void run () {
		Debug.out.breakPoint ("B starting");
		buffer.put ("Wilma");
		final Object result = buffer.get ();
		System.out.println ("B got " + result);
		Debug.out.assertion (result != null);		
	    }
	});

    public static void main (String[] args) throws Exception {
	// Send debugging output to a file
	Debug.out.addFile ("TestBufferDebug.txt");
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
