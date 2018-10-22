package ajeffrey.teaching.test;

import ajeffrey.teaching.util.lock.ReadWriteLock;

public class TestReadWriteLock implements Runnable {

    static int count = 0;
    static Count numReaders = new Count ();
    static Count numWriters = new Count ();
    static Count runningThreads = new Count ();
    final static ReadWriteLock lock = ReadWriteLock.factory.build ();

    final Thread thread = new Thread (this);
    final boolean isReader;

    public TestReadWriteLock (boolean isReader) { this.isReader = isReader; }

    public void start () { thread.start (); }

    public void run () {
	try {
	    if (isReader) { reader (); } else { writer (); }
	} catch (InterruptedException ex) {
	    System.err.println ("Interrupted...");
	}
    }

    public void reader () throws InterruptedException {
	while (runningThreads.isPositive () || count==0) {
	    lock.acquireReadLock ();
	    numReaders.inc ();
	    delay (2);
	    if (Math.random () < 0.05) { 
		System.out.println ("count = " + count + ", numReaders = " + numReaders + " numWriters = " + numWriters);
	    }
	    numReaders.dec ();
	    lock.releaseReadLock ();
	    delay (100);
	}
        System.out.println ("FINISHED: count = " + count + " (should be 100)");
    }

    public void writer () throws InterruptedException {
	runningThreads.inc ();
	for (int i=0; i<10; i++) {
	    lock.acquireWriteLock ();
	    numWriters.inc ();
	    int tmp = count;
	    delay (2);
	    count = tmp+1;
	    numWriters.dec ();
	    lock.releaseWriteLock ();
            delay (2);
	}
	runningThreads.dec ();
    }

    public void delay (long maxDelay) throws InterruptedException {
	thread.sleep ((long)(Math.random () * maxDelay));
    }

    public static void main (String[] args) {
	runningThreads.inc ();
        // Create 10 reader threads
	for (int i=0; i<10; i++) {
	    TestReadWriteLock test = new TestReadWriteLock (true);
	    test.start ();
	}
        // Create 10 writer threads
	for (int i=0; i<10; i++) {
	    TestReadWriteLock test = new TestReadWriteLock (false);
	    test.start ();
	}
	runningThreads.dec ();
        // This should finish once the writers have done their job!
    }

}

class Count {
    final Object lock = new Object ();
    int contents;
    public String toString () { return "" + contents; }
    void inc () { synchronized (lock) { contents++; } }
    void dec () { synchronized (lock) { contents--; } }
    boolean isPositive () { return contents > 0; }
}
