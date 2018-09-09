package ajeffrey.teaching.minisoap;

import ajeffrey.teaching.debug.Debug;

public class RMIRegistry {

    public static void main (String[] args) {
	Debug.out.addPrintStream (System.out);
	Debug.out.println ("RMIRegistry: registering naming service.");
	Stub.lookup.put (Naming.objectId, Naming.singleton);
	Debug.out.println ("RMIRegistry: starting executor.");
	new ExecutorFactoryImpl (Naming.port);
	Debug.out.println ("RMIRegistry: done.");
    }

}
