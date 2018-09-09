package ajeffrey.teaching.banking.client.logic;

/**
 * A factory for building new business logic.
 * @author Alan Jeffrey 
 * @version 1.0.2
 */
public interface LogicFactory {

    /**
     * Build a new business logic object
     * @return a new business logic object
     */
    public Logic build ();

}
