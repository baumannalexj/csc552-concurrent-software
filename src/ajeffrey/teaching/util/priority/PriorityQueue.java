package ajeffrey.teaching.util.priority;

import ajeffrey.teaching.util.time.TimeoutException;

/**
 * An interface for priority queues.
 * A priority queue has two methods: add (which adds a new entry into
 * the queue) and get (which gets the highest priority entry from
 * the queue).  If the queue is empty, then get () will block.
 * The entries should implement Comparable, to determine their priority
 * order.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface PriorityQueue {

    /**
     * Add a new entry into the queue.
     * @param entry The entry to add
     */
    public void add (Comparable entry);

    /**
     * Get the highest priority entry out of the queue.
     * Blocks if the queue is empty.
     * @return the highest priority entry in the queue
     * @exception InterruptedException thrown if the thread is interrupted
     *   while blocking
     */
    public Comparable get () throws InterruptedException;

    /**
     * Get the highest priority entry out of the queue.
     * Blocks if the queue is empty, but times out.
     * @param timeout the time in milliseconds to wait
     * @return the highest priority entry in the queue
     * @exception InterruptedException thrown if the thread is interrupted
     *   while blocking
     */
    public Comparable get (long timeout) throws InterruptedException, TimeoutException;

    /**
     * The number of entries in the queue.
     * @return the number of entries in the queue.
     */
    public int size ();

}

