package ajeffrey.teaching.jack;

/**
 * A factory for building new business logic for the Jack application.
 * @version 1.0.1
 * @author Alan Jeffrey
 */

public interface LogicFactory {

    /**
     * Build a new business logic object.
     * @return the new business logic.
     */    
    public Logic build ();

} 
