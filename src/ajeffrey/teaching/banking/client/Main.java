package ajeffrey.teaching.banking.client;

import ajeffrey.teaching.banking.client.options.Options;

/**
 * The main class for the banking client program.
 * This class can be executed from the command line.
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public class Main {

    /**
     * The main method for the banking client program.
     * @param args the command line arguments
     */
    public static void main (final String[] args) {
	Options.singleton.parse (args);
	Client client = Client.factory.build ();
        client.run ();
    }

}
