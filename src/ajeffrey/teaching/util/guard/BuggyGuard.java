package ajeffrey.teaching.util.guard;

import ajeffrey.teaching.debug.Debug;

/**
 * A buggy guard class.
 * <p>This class has a subtle bug with it, due to not performing
 * a double check properly.</p>
 * @author Alan Jeffrey
 * @version 1.0.0
 */
public interface BuggyGuard {

    /**
     * A factory for building buggy guards.
     */
    public static GuardFactory factory = new BuggyGuardFactoryImpl ();

}

class BuggyGuardFactoryImpl implements GuardFactory {
    
    public Guard build (final boolean init) {
	return new BuggyGuardImpl (init);
    }

}

class BuggyGuardImpl implements Guard {

    protected boolean value;
    protected Object lock = new Object ();

    protected BuggyGuardImpl (final boolean init) {
	this.value = init;
	Debug.out.println ("BuggyGuardImpl: built");
    }

    public void setValue (final boolean value) {
	Debug.out.println ("BuggyGuardImpl.setValue: Starting");
	Debug.out.println ("BuggyGuardImpl.setValue: Current state=" + this);
	this.value = value;
	if (value) {
	    Debug.out.println ("BuggyGuardImpl.setValue: Grabbing the lock");
	    synchronized (lock) { 
		Debug.out.println ("BuggyGuardImpl.setValue: Grabbed the lock");
		Debug.out.println ("BuggyGuardImpl.setValue: Calling notifyAll ()");
		lock.notifyAll ();
	    }
	    Debug.out.println ("BuggyGuardImpl.setValue: Released the lock");
	}
	Debug.out.println ("BuggyGuardImpl.setValue: New state=" + this);
	Debug.out.println ("BuggyGuardImpl.setValue: Returning");
    }

    public boolean getValue () {
	Debug.out.println ("BuggyGuardImpl.getValue: Starting");
	final boolean result = value;
	Debug.out.println ("BuggyGuardImpl.getValue: Returning " + result);
	return result;
    }

    public void waitForTrue () throws InterruptedException {
	Debug.out.println ("BuggyGuardImpl.waitForTrue: Starting");
	if (!value) {
	    Debug.out.println ("BuggyGuardImpl.waitForTrue: The value was false");
	    Debug.out.println ("BuggyGuardImpl.waitForTrue: Grabbing the lock");
	    synchronized (lock) {
		Debug.out.println ("BuggyGuardImpl.waitForTrue: Grabbed the lock");
		// The double-check if statement has been left out here
		// which causes a bug!
		Debug.out.println ("BuggyGuardImpl.waitForTrue: Waiting " + this);
		lock.wait (); 
		Debug.out.println ("BuggyGuardImpl.waitForTrue: Done waiting");
	    }
	    Debug.out.println ("BuggyGuardImpl.waitForTrue: Released the lock");
	} else {
	    Debug.out.println ("BuggyGuardImpl.waitForTrue: The value was true");
	}
	Debug.out.println ("BuggyGuardImpl.getValue: Starting");
    }

    public String toString () {
	return "Guard { value=" + value + " }";
    }

}

