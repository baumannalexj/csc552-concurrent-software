package ajeffrey.teaching.util.priority;

import ajeffrey.teaching.util.list.ImmutableList;
import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.util.time.TimeoutException;

/**
 * A pessimistic implementation of priority queues.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface PessimisticPriorityQueue {

    /**
     * A factory for building pessimistic priorioty queues.
     */
    public PriorityQueueFactory factory = new PessimisticPQFactory ();

}

class PessimisticPQFactory implements PriorityQueueFactory {

    public PriorityQueue build () { return new PessimisticPQ (); }

}

class PessimisticPQ implements PriorityQueue {

    protected final Object lock = new Object ();
    protected ImmutableList contents = ImmutableList.empty;

    protected ImmutableList insert (final Comparable element, final ImmutableList list) {
	Debug.out.println ("PessimisticPQ.insert: Starting");
	if (list.size () == 0) {
	    Debug.out.println ("PessimisticPQ.insert: List is empty");
	    Debug.out.println ("PessimisticPQ.insert: Returning");
	    return list.cons (element);
	} else if (element.compareTo (list.head ()) <= 0) {
	    Debug.out.println ("PessimisticPQ.insert: element <= " + list.head ());
	    Debug.out.println ("PessimisticPQ.insert: Returning");
	    return list.cons (element);
	} else {
	    Debug.out.println ("PessimisticPQ.insert: element > " + list.head ());
	    Debug.out.println ("PessimisticPQ.insert: Recursing");
	    return insert (element, list.tail ()).cons (list.head ());
	}
    }

    public void add (final Comparable element) {
	Debug.out.println ("PessimisticPQ.add: Starting");
	Debug.out.println ("PessimisticPQ.add: Grabbing lock...");
	synchronized (lock) {
	    Debug.out.println ("PessimisticPQ.add: Adding " + element + " to " + contents);
	    contents = insert (element, contents);
	    Debug.out.println ("PessimisticPQ.add: Calling notifyAll...");
	    lock.notifyAll ();
	    Debug.out.println ("PessimisticPQ.add: Releasing lock...");
	}
	Debug.out.println ("PessimisticPQ.add: Returning");
    }

    public Comparable get () throws InterruptedException {
	Debug.out.println ("PessimisticPQ.get: Starting");
	Debug.out.println ("PessimisticPQ.get: Grabbing lock...");
	synchronized (lock) {
	    Debug.out.println ("PessimisticPQ.get: contents = " + contents);
	    while (contents.size () == 0) { 
		Debug.out.println ("PessimisticPQ.get: Waiting...");
		lock.wait (); 
		Debug.out.println ("PessimisticPQ.get: Woken up");
		Debug.out.println ("PessimisticPQ.get: contents = " + contents);
	    }
	    Debug.out.println ("PessimisticPQ.get: Calling contents.head");
            Comparable result = (Comparable)(contents.head ());
	    contents = contents.tail ();
	    Debug.out.println ("PessimisticPQ.get: contents = " + contents);
	    Debug.out.println ("PessimisticPQ.get: Returning " + result);
	    return result;
	}
    }

    public Comparable get (final long timeout) throws InterruptedException, TimeoutException {
	Debug.out.println ("PessimisticPQ.get: Starting");
	Debug.out.println ("PessimisticPQ.get: Grabbing lock...");
	synchronized (lock) {
	    Debug.out.println ("PessimisticPQ.get: contents = " + contents);
	    final long endTime = System.currentTimeMillis () + timeout;
	    Debug.out.println ("PessimisticPQ.get: endTime = " + endTime);
	    while (contents.size () == 0) { 
		final long delay = endTime - System.currentTimeMillis ();
		if (delay > 0) {
		    Debug.out.println ("PessimisticPQ.get: Waiting for " + delay);
		    lock.wait (delay); 
		    Debug.out.println ("PessimisticPQ.get: Woken up");
		    Debug.out.println ("PessimisticPQ.get: contents = " + contents);
		} else {
		    Debug.out.println ("PessimisticPQ.get: Timeout");
		    throw new TimeoutException ();
		}
	    }
	    Debug.out.println ("PessimisticPQ.get: Calling contents.head");
            Comparable result = (Comparable)(contents.head ());
	    contents = contents.tail ();
	    Debug.out.println ("PessimisticPQ.get: contents = " + contents);
	    Debug.out.println ("PessimisticPQ.get: Returning " + result);
	    return result;
	}
    }

    public int size () {
	return contents.size ();
    }
    
}
