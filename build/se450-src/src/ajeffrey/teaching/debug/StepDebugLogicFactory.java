package ajeffrey.teaching.debug;

/**
 * A factory which builds step debug logic
 * @author Alan Jeffrey
 * @version v1.0.1
 */
public interface StepDebugLogicFactory {

    /**
     * Build a new step debug logic.
     * @param threadName the name of the current thread
     * @return a new step debug logic
     */
    public StepDebugLogic build (String threadName);

}
