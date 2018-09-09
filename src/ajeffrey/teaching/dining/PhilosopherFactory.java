package ajeffrey.teaching.dining;

/**
 * A factory class for building philosophers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface PhilosopherFactory {

    /**
     * Build a philosopher.
     * @param name the name of the philosopher
     * @param lhFork the left-hand fork
     * @param rhFork the right-hand fork
     * @return a new philosopher
     */
    public Philosopher build (Comparable lhFork, Comparable rhFork, String name);

}
