package ajeffrey.teaching.pingpong.server;

import ajeffrey.teaching.debug.Debug;

/**
 * An Executor class for executing tasks.
 * @author Alan Jeffrey
 * @version 1.0.1
 */

public interface Executor {

    /**
     * Try to execute a given task.
     * The task will be run, if system resources permit.
     * If the system is too busy, then the task will be cancelled.
     * @param task the task to execute
     */
    public void execute (Task task);

    /**
     * An executor.
     */
    public Executor singleton = new ExecutorImpl ();

}

class ExecutorImpl implements Executor {

    // This is a simplistic executor which just builds a new
    // thread for each task.  This is not realistic!

    public void execute (Task task) {
	Debug.out.println ("Executor.execute: Starting");
	final Thread thread = new Thread (task);
	thread.start ();
	Debug.out.println ("Executor.execute: Returning");
    }

}
