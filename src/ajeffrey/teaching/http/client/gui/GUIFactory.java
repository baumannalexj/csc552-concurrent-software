package ajeffrey.teaching.http.client.gui;

import ajeffrey.teaching.http.client.logic.Logic;

/**
 * A factory for building GUI objects.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface GUIFactory {

    /**
     * Build a new GUI object.
     * @param logic the business logic for the GUI
     * @return a new GUI object.
     */
    public GUI build (Logic logic);

}
