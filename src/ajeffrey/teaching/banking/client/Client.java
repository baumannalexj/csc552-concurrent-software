package ajeffrey.teaching.banking.client;

import ajeffrey.teaching.banking.client.logic.Logic;
import ajeffrey.teaching.banking.client.gui.GUI;

import ajeffrey.teaching.debug.Debug;

/**
 * The specification of the SE&nbsp;552 banking client.
 * The client contains:
 * <ul>
 * <li>A business logic component <code>Logic</code>, and</li>
 * <li>A GUI component <code>GUI</code>.</li>
 * </ul>
 * Together, these define the functionality and appearance
 * of the client.
 * 
 * @author Alan Jeffrey
 * @version 1.0.1
 * @see Logic
 * @see GUI
 */
public interface Client extends Runnable {

    /**
     * A factory for building new clients.
     */
    public static ClientFactory factory = new ClientFactoryImpl ();

    /**
     * The business logic for the client.
     * @return the business logic
     */
    public Logic getLogic ();

    /**
     * The GUI for the client.
     * @return the GUI.
     */
    public GUI getGUI ();

    /**
     * Run the client.
     * When this method is called, it executes the client.
     */
    public void run ();

}

class ClientFactoryImpl implements ClientFactory {

    public Client build () { return new ClientImpl (); }

}

class ClientImpl implements Client {

    protected final Logic logic;
    protected final GUI gui;

    protected ClientImpl () {
	logic = Logic.factory.build ();
	gui = GUI.factory.build (logic);
	Debug.out.println ("ClientImpl: Built.");
    }

    public Logic getLogic () { return logic; }

    public GUI getGUI () { return gui; }

    public void run () {
	Debug.out.println 
	    ("ClientImpl.run: Starting");
	Debug.out.println 
	    ("ClientImpl.run: Calling " + logic + ".attach (" + gui + ")");
        logic.getSubject ().attach (gui);
	Debug.out.println 
	    ("ClientImpl.run: Calling " + gui + ".run ()");
        gui.run ();
	Debug.out.println 
	    ("ClientImpl.run: Returning");
    }

}
