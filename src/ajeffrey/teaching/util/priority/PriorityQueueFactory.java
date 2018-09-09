package ajeffrey.teaching.util.priority;

/**
 * A factory for building priority queues.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface PriorityQueueFactory {

    /**
     * Build a new priority queue.
     * @return a new priority queue.
     */
    public PriorityQueue build ();

}
