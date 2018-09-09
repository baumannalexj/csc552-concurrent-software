package ajeffrey.teaching.util.guard;

import ajeffrey.teaching.debug.Debug;

/**
 * A guard class.
 * This provides an implementation of a mutable boolean variable,
 * plus one extra method <code>waitForTrue</code> which blocks
 * waiting for the guard to become true.
 * This method may return true even when the flag is false,
 * due to transitory values.  For example, if the flag is
 * false, and we have two threads:
 * <ul>
 * <li>T1 is blocked having called <code>waitForTrue</code></li>
 * <li>T2 calls <code>setValue (true)</code> then
 *   <code>setValue (false)</code>.<code>
 * </ul>
 * T1 is guaranteed to be woken up, even though once it wakes up
 * T2 may well have set the value back to be false again.
 * This is called <i>transitory values</i>.
 * If T1 wants to avoid transitories, it should do something like:
 * <pre>
 *   while (!(guard.getValue ())) { guard.waitForTrue (); }
 * </pre>
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface Guard {

    /**
     * Set the value of the guard
     * @param value the new value of the guard
     */
    public void setValue (boolean value);

    /**
     * Get the value of the guard
     * @return the current value of the guard
     */
    public boolean getValue ();

    /**
     * Wait for the guard to become true.
     * This method will block until the guard is set to be true.
     */
    public void waitForTrue () throws InterruptedException;

    /**
     * A factory for building guards.
     */
    public static GuardFactory factory = new GuardFactoryImpl ();

}

class GuardFactoryImpl implements GuardFactory {
    
    public Guard build (final boolean init) {
	return new GuardImpl (init);
    }

}

class GuardImpl implements Guard {

    protected boolean value;
    protected Object lock = new Object ();

    protected GuardImpl (final boolean init) {
	this.value = init;
	Debug.out.println ("GuardImpl: built");
    }

    public void setValue (final boolean value) {
	Debug.out.println ("GuardImpl.setValue: Starting");
	Debug.out.println ("GuardImpl.setValue: Current state=" + this);
	Debug.out.println ("GuardImpl.setValue: Grabbing the lock");
        synchronized (lock) {
	    this.value = value;
	    if (value) {
		Debug.out.println ("GuardImpl.setValue: Calling notifyAll ()");
		lock.notifyAll ();
	    }
	    Debug.out.println ("GuardImpl.setValue: Released the lock");
	}
	Debug.out.println ("GuardImpl.setValue: New state=" + this);
	Debug.out.println ("GuardImpl.setValue: Returning");
    }

    public boolean getValue () {
	Debug.out.println ("GuardImpl.getValue: Starting");
	final boolean result = value;
	Debug.out.println ("GuardImpl.getValue: Returning " + result);
	return result;
    }

    public void waitForTrue () throws InterruptedException {
	Debug.out.println ("GuardImpl.waitForTrue: Starting");
	if (!value) {
	    Debug.out.println ("GuardImpl.waitForTrue: The value was false");
	    Debug.out.println ("GuardImpl.waitForTrue: Grabbing the lock");
	    synchronized (lock) {
		Debug.out.println ("GuardImpl.waitForTrue: Grabbed the lock");
		if (!value) { 
		    Debug.out.println ("GuardImpl.waitForTrue: Still false");
		    Debug.out.println ("GuardImpl.waitForTrue: Waiting " + this);
		    lock.wait (); 
		    Debug.out.println ("GuardImpl.waitForTrue: Done waiting");
		} else {
		    Debug.out.println ("GuardImpl.waitForTrue: True again!");
		}
	    }
	    Debug.out.println ("GuardImpl.waitForTrue: Released the lock");
	} else {
	    Debug.out.println ("GuardImpl.waitForTrue: The value was true");
	}
	Debug.out.println ("GuardImpl.getValue: Starting");
    }

    public String toString () {
	return "Guard { value=" + value + " }";
    }

}

