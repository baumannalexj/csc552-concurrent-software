package ajeffrey.teaching.debug;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;

import java.util.HashMap;

/**
 * A print stream which produces a step debugger user interface,
 * with run, pause, and step options.  Useful for debugging multi-threaded
 * applications.
 * @author Alan Jeffrey
 * @version v1.0.1
 */
public interface StepDebugStream {

    /**
     * A debug stream factory which produces step debugging information.
     */
    public static DebugStreamFactory factory
	= new StepDebugStreamFactoryImpl ();

}

class StepDebugStreamFactoryImpl implements DebugStreamFactory {

    public DebugStream build (final String threadName) {
	return new StepDebugStreamImpl (threadName);
    }

}

class StepDebugStreamImpl implements DebugStream {

    protected final StepDebugLogic logic;
    protected final StepDebugGUI gui;
    protected final PrintStream out;

    protected StepDebugStreamImpl (final String threadName) {
	logic = StepDebugLogic.factory.build (threadName);
	gui = StepDebugGUI.factory.build (logic);
	out = gui.getPrintStream ();
	gui.start ();
    }

    public void println (final String msg) {
	out.println (msg);
	logic.waitWhilePaused ();
    }

    public void breakPoint (final String msg) {
	out.println (msg);
	logic.pauseMode ();
	logic.waitWhilePaused ();
    }


}
