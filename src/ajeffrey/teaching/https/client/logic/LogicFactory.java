package ajeffrey.teaching.https.client.logic;

/**
 * A factory for building new business logic objects
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface LogicFactory {

    /**
     * Build a new business logic object
     * @return a new business logic object
     */
    public Logic build ();

}
