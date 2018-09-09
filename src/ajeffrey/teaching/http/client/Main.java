package ajeffrey.teaching.http.client;

import ajeffrey.teaching.http.client.gui.GUI;
import ajeffrey.teaching.http.client.logic.Logic;
import ajeffrey.teaching.debug.Debug;

/**
 * A simple HTTP client.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public class Main {

    public static void main (String[] args) {
	// Switch on debugging
	Debug.out.addPrintStream (System.err);
	// Set up the business logic and GUI
	Logic logic = Logic.factory.build ();
	GUI gui = GUI.factory.build (logic);
	// Kick it all off
	gui.start ();
    }

}
