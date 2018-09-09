package ajeffrey.teaching.hash2.client;

import ajeffrey.teaching.hash2.iface.Hash;
import ajeffrey.teaching.minisoap.Naming;
import ajeffrey.teaching.debug.Debug;

public class Main {

    public static void main (String[] args) throws Exception {
	Debug.out.addPrintStream (System.out);
	String url;
	if (args.length > 0) {
	    url = args[0];
	} else {
	    url = "//localhost/hashserver";
	}
	final Hash hashTable = (Hash)(Naming.singleton.lookup (url));
	final GUI gui = GUI.factory.build (hashTable);
	gui.start ();
    }

}
