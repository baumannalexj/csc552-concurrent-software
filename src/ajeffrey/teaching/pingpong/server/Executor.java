package ajeffrey.teaching.pingpong.server;

import ajeffrey.teaching.debug.Debug;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * An Executor class for executing tasks.
 *
 * @author Alan Jeffrey
 * @version 1.0.1
 *
 *
 *
 * You should open the file ajeffrey/teaching/pingpong/server/Executor.java, which executes tasks provided by the server. Currently it uses a simplistic one-thread-per-task model. You should change this, using the inbuilt (Thread?)executor classes from java.util.concurrent to achieve as many as possible of the following:
 * Your answer should be put into a zip archive file, and submitted using the Course OnLine system.
 */

public interface Executor {
    /// never more than 10 idle workers ==> carpool size

    public static final int MAX_POOL_SIZE = 50;     /** There are never more than 50 worker threads in use at one time.*/

     static final int MIN_POOL_SIZE = 10;     /** There are never more than 10 idle worker threads (i.e. threads which have been built, but are not currently performing a task) at one time.*/


    static final long KEEP_ALIVE_TIME = 60; //TODO look this up, NULL?

    public static final int MAX_NUM_CLIENT_CONNECTIONS = 100;     /** * There are never be more than 100 client connections waiting to be serviced at one time.*/

     static final boolean IS_FAIR_FIFO_ORDER = true;     /** New client connections are cancelled in preference to old client connections.*/


    BlockingQueue<Runnable> blockingWorkQueue= new ArrayBlockingQueue<>(
            MAX_NUM_CLIENT_CONNECTIONS,
            IS_FAIR_FIFO_ORDER);

    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            MIN_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            blockingWorkQueue
    );

    /**
     * An executor.
     */
    public Executor singleton = new ExecutorImpl();

    /**
     * Try to execute a given task.
     * The task will be run, if system resources permit.
     * If the system is too busy, then the task will be cancelled.
     *
     * @param task the task to execute
     */
    public void execute(Task task);

}

class ExecutorImpl implements Executor {



    public void execute(Task task) {
        Debug.out.println("Executor.execute: Starting");
        System.out.println("Number of connections in queue: " + blockingWorkQueue.size());

        executor.submit(task);

        Debug.out.println("Executor.execute: Returning");
        System.out.println("Number of connections in queue: " + blockingWorkQueue.size());

    }

}
