package ajeffrey.teaching.test;

import ajeffrey.teaching.util.priority.PriorityQueue;
import ajeffrey.teaching.util.priority.PessimisticPriorityQueue;
import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.debug.StepDebugStream;

public class TestPriorityQueue {

    public static void main (final String[] args) throws InterruptedException {
	// Debug.out.addPrintStream (System.err);
	// Debug.out.addFactory (StepDebugStream.factory);
	final PriorityQueue queue = PessimisticPriorityQueue.factory.build ();
	final Thread t1 = new Thread () { public void run () {
	    Debug.out.breakPoint ("Starting t1");
	    for (int i=0; i < args.length; i++) {
		queue.add (args[i]);
	    }
	    Debug.out.println  ("Done t1");
	} };
	final Thread t2 = new Thread () { public void run () {
	    try {
		sleep (1000);
		Debug.out.breakPoint ("Starting t2");
		while (t1.isAlive () || queue.size () > 0) {
		    Debug.out.println ("Getting...");
		    final String gotten = (String)(queue.get ());
		    Debug.out.println ("Got: " + gotten);
		    System.out.println ("Got: " + gotten);
		}
	    } catch (final InterruptedException ex) {
		Debug.out.println  ("Interrupted t2");		
	    }
	    Debug.out.println  ("Done t2");
	} };
	t1.start ();
	t2.start ();
    }

}
