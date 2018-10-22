package ajeffrey.teaching.https.client;

import ajeffrey.teaching.https.client.gui.GUI;
import ajeffrey.teaching.https.client.logic.Logic;
import ajeffrey.teaching.debug.Debug;

import java.security.Security;
import com.sun.net.ssl.internal.ssl.Provider;

/**
 * A simple HTTPS client.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public class Main {

    public static void main (String[] args) {
	// Switch on debugging
	Debug.out.addPrintStream (System.err);
	// Add Sun's SSL security provider
	Security.addProvider(new Provider());
	// Add Sun's https protocol handler
        System.setProperty ("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
	// Add Sun's SSL debugging
	System.setProperty ("javax.net.debug", "ssl,handshake,data,trustmanager");
	// Set up the business logic and GUI
	Logic logic = Logic.factory.build ();
	GUI gui = GUI.factory.build (logic);
	// Kick it all off
	gui.start ();
    }

}
