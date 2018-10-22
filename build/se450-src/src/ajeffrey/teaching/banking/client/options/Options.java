package ajeffrey.teaching.banking.client.options;

import ajeffrey.teaching.debug.Debug;

/**
 * A interface for objects which parse the command line options
 * for the banking client program.  The options are:
 * <ul>
 * <li><code>-debug</code>: switch debugging on</li>
 * <li><code>-version</code>: print the version number</li>
 * <li><code>-help</code>: print help information on the command-line
 *   options.</li>
 * </ul>
 * In the future, there should be command line options to customize
 * which List and ImmutableList implementations are used.
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public interface Options {

    /**
     * An object to parse command line arguments.
     */
    public static final Options singleton = new OptionsImpl ();

    /**
     * Parse a collection of command line arguments.
     * @param args the command line arguments to parse
     */
    public void parse (String[] args);

}

class OptionsImpl implements Options {

    protected final String usage = 
	"Options:\n" +
	"  -debug:   print debugging information\n" +
        "  -help:    print this help information\n" +
        "  -version: print the program version";

    protected final String version = "Version 1.0.2";

    public void parse (final String[] args) {
	Debug.out.println ("Options.parse: Starting");
	for (int i = 0; i < args.length; i++) {
	    if (args[i].equals ("-debug")) {
		Debug.out.addPrintStream (System.err);
		Debug.out.println ("Options.parse: Debugging on");
	    } else if (args[i].equals ("-help")) {
		System.err.println (usage);
		System.exit (0);
	    } else if (args[i].equals ("-version")) {
		System.err.println (version);
		System.exit (0);
	    } else {
		System.err.println (usage);
		System.exit (1);
	    }
	}
	Debug.out.println ("Options.parse: Returning");
    }

}
