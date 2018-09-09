package ajeffrey.teaching.jack;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.debug.StepDebugStream;

/**
 * A program which repeatedly prints 
 * `All work and no play makes jack a dull boy'
 * to a text area.
 * @version 1.0.1
 * @author Alan Jeffrey
 */
public class Main {

    public static void main (String[] args) {
	//
	// Which logic factory to use: uncomment one of the lines to
	// choose the business logic.
	// LogicFactory logicFactory = SimpleLogic.factory;
	// LogicFactory logicFactory = PollingLogic.factory;
	// LogicFactory logicFactory = SuspendResumeLogic.factory;
	 LogicFactory logicFactory = GuardedLogic.factory;
	//
	// Set up the GUI
	GUI gui = GUI.factory.build (logicFactory);
	//
	// Set debugging to be true 
	// (comment this out to lose the debugging info)
	Debug.out.addPrintStream (System.err);
        //
        // Uncommenting the next line will cause debugging output
        // to be sent to a separate step debugging window.
	// Debug.out.addFactory (StepDebugStream.factory);
	//
	// Kick everything off
	gui.start ();
    }

}
