package ajeffrey.teaching.jack;

/**
 * A GUI for the Jack application.
 * @version 1.0.1
 * @author Alan Jeffrey
 * @see GUIFactory
 */
public interface GUIFactory {

    /**
     * Build a new GUI.
     * @param factory the factory for building business logic for the GUI
     * @return a new GUI object
     */
    public GUI build (LogicFactory factory);
    
}
