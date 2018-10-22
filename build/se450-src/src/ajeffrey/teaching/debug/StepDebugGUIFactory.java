package ajeffrey.teaching.debug;

/**
 * A factory for building step debugging GUIs.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface StepDebugGUIFactory {

    /**
     * Build a new StepDebug GUI
     * @param logic the logic behind the GUI
     * @return a new step debug GUI.
     */
    public StepDebugGUI build (StepDebugLogic logic);

}
