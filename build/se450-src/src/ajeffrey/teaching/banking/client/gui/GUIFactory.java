package ajeffrey.teaching.banking.client.gui;

import ajeffrey.teaching.banking.client.logic.Logic;

/**
 * A factory for building GUIs.
 * @author Alan Jeffrey
 * @version 1.0.2
 * @see Logic
 */
public interface GUIFactory {
   
    /**
     * Build a new GUI.
     * @param logic the business logic for the GUI
     * @return a new GUI based on the given business logic
     */
    public GUI build (Logic logic);

}
