package ajeffrey.teaching.hash2.server;

import ajeffrey.teaching.hash2.iface.Hash;

import ajeffrey.teaching.minisoap.Naming;
import ajeffrey.teaching.minisoap.Remote;
import ajeffrey.teaching.debug.Debug;

import java.util.Hashtable;

public class Main {

    public static void main (String[] args) throws Exception {
	Debug.out.addPrintStream (System.out);
	Debug.out.println ("Main: registering hashserver");
	Naming.singleton.rebind ("//localhost/hashserver", new HashImpl ());
    }

}

class HashImpl implements Hash, Remote {

    protected final Hashtable contents = new Hashtable ();
    
    public void put (Object key, Object value) {
        Debug.out.println ("HashImpl.put: adding (" + key + ", " + value + ")");
	contents.put (key, value);
    }

    public Object get (Object key) {
        Debug.out.println ("HashImpl.get: looking up " + key);
	return contents.get (key);
    }

    public String getRemoteInterface () { return "ajeffrey.teaching.hash2.iface.Hash"; }

}
