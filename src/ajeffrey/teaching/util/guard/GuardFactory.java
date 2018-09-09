package ajeffrey.teaching.util.guard;

/**
 * A factory for building new guard objects
 * @author Alan Jeffrey
 * @version 1.0.0
 */
public interface GuardFactory {

    /**
     * Build a new guard, with given initial value.
     * @param value the initial value
     * @return a new Guard object
     */
    public Guard build (boolean init);

}
