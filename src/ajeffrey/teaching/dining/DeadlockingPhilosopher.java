package ajeffrey.teaching.dining;

import ajeffrey.teaching.debug.Debug;

/**
 * A philosopher from the dining philosophers problem.
 * A philosopher thinks, picks up their left-hand fork,
 * picks up their right-hand fork, then eats.
 * Unfortunately, putting a collection of philosophers in a circle
 * can produce deadlock, if they all pick up their lh forks before any
 * of them have a chance to pick up their rh forks.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface DeadlockingPhilosopher {

    /**
     * A factory for building deadlocking philosophers.
     */
    public static final PhilosopherFactory factory 
	= new DeadlockingPhilosopherFactoryImpl ();

}

class DeadlockingPhilosopherFactoryImpl implements PhilosopherFactory {

    public Philosopher build 
	(final Comparable lhFork, final Comparable rhFork, final String name) 
    {
	return new DeadlockingPhilosopherImpl (lhFork, rhFork, name);
    }

}

class DeadlockingPhilosopherImpl implements Runnable, Philosopher {

    final protected Object lhFork;
    final protected Object rhFork;
    final protected String name;
    final protected Thread thread;

    protected DeadlockingPhilosopherImpl
	(final Object lhFork, final Object rhFork, final String name) 
    {
	this.lhFork = lhFork;
	this.rhFork = rhFork;
	this.name = name;
	this.thread = new Thread (this);
    }

    public void start () {
	thread.start ();
    }

    public void run () {
	Debug.out.breakPoint (name + " is starting");
	try {
	    while (true) {
		Debug.out.println (name + " is thinking");
		delay ();
		Debug.out.println (name + " tries to pick up " + lhFork);
		synchronized (lhFork) {
		    Debug.out.println (name + " picked up " + lhFork);
		    delay ();
		    Debug.out.println (name + " tries to pick up " + rhFork);
		    synchronized (rhFork) {
			Debug.out.println (name + " picked up " + rhFork);
			Debug.out.println (name + " starts eating");
			delay ();
			Debug.out.println (name + " finishes eating");
		    }
		}
	    }
	} catch (final InterruptedException ex) {
	    Debug.out.println (name + " is interrupted");
	}
    }

    protected void delay () throws InterruptedException {
	Thread.currentThread().sleep ((long)(1000*Math.random()));
    }

}
