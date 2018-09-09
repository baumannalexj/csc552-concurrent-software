package ajeffrey.teaching.debug;

import ajeffrey.teaching.observer.Subject;
import ajeffrey.teaching.observer.Observer;

/**
 * The logic behind a step debug GUI.
 * This logic includes the Gang of Four's Observer/Observed pattern.
 * Whenever the state of the object changes, it will notify
 * any observers which are attached to it.
 * @author Alan Jeffrey
 * @version v1.0.1
 */
public interface StepDebugLogic extends Subject {

    /**
     * If we are currently in pause mode, then block,
     * waiting for us to enter step or run mode.
     * Then, if we are in step mode, set the debugger 
     * back to pause mode.
     */
    public void waitWhilePaused ();

    /**
     * Put the debugger in pause mode.
     */
    public void pauseMode ();

    /**
     * Put the debugger in run mode.
     */
    public void runMode ();

    /**
     * Put the debugger in step mode.
     */
    public void stepMode ();

    /**
     * Are we in run mode?
     * @return true if we are currently in run mode.
     */
    public boolean inRunMode ();

    /**
     * Are we in step mode?
     * @return true if we are currently in step mode.
     */
    public boolean inStepMode ();

    /**
     * Are we in pause mode?
     * @return true if we are currently in pause mode.
     */
    public boolean inPauseMode ();

    /**
     * A string corresponding to the name of the current thread.
     * @return the name of the current thread
     */
    public String getThreadName ();

    /**
     * A string corresponding to the name of the current mode.
     * @return the name of the current mode
     */
    public String getModeName ();

    /**
     * A factory for building step debugger logic.
     */
    public static StepDebugLogicFactory factory 
	= new StepDebugLogicFactoryImpl ();

}

class StepDebugLogicFactoryImpl implements StepDebugLogicFactory {
   
    public StepDebugLogic build (String threadName) {
	return new StepDebugLogicImpl (threadName);
    }

}

class StepDebugLogicImpl implements StepDebugLogic {

    protected final int PAUSE = 0;
    protected final int RUN = 1;
    protected final int STEP = 2;

    protected int mode = RUN;
    protected final Object lock = new Object ();
    protected final Subject subject = Subject.factory.build ();
    protected final String threadName;

    protected StepDebugLogicImpl (final String threadName) {
	this.threadName = threadName;
    }

    public void waitWhilePaused () {
	if (!inRunMode ()) {
	    if (inStepMode ()) { pauseMode (); }
	    synchronized (lock) {
		while (inPauseMode ()) { 
		    try {
			lock.wait (); 
		    } catch (final InterruptedException ex) {
			Thread.currentThread ().interrupt ();
		    }
		}
	    }
	}
    }

    public void pauseMode () {
	mode = PAUSE;
	updateObservers ();
    }

    public void runMode () {
	mode = RUN;
	updateObservers ();
	synchronized (lock) { lock.notifyAll (); }
    }

    public void stepMode () {
	mode = STEP;
	updateObservers ();
	synchronized (lock) { lock.notifyAll (); }
    }

    public boolean inRunMode () { return mode == RUN; }

    public boolean inStepMode () { return mode == STEP; }

    public boolean inPauseMode () { return mode == PAUSE; }

    public String getModeName () {
	switch (mode) {
	case RUN: return "Running";
	case STEP: return "Stepping";
	case PAUSE: return "Paused";
	default: return "BUGGY";
	}
    }

    public String getThreadName () {
	return threadName;
    }

    public void attach (final Observer observer) {
	subject.attach (observer);
    }

    public void detach (final Observer observer) {
	subject.detach (observer);
    }

    public void updateObservers () {
	subject.updateObservers ();
    }

}
