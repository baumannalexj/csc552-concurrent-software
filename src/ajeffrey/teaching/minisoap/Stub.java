package ajeffrey.teaching.minisoap;

import ajeffrey.teaching.debug.Debug;
import java.util.HashMap;

public interface Stub {

    Host getHost ();
    String getObjectId ();

    public static final StubFactory factory = new StubFactoryImpl ();
    public static final HashMap lookup = new HashMap ();

}

class StubFactoryImpl implements StubFactory {

    final HashMap stubs = new HashMap ();
    int numStubs;

    public Stub build (Remote object) {
	Stub result = (Stub)(stubs.get (object));
	if (result == null) {
	    try {
		String remoteInterface = object.getRemoteInterface ();
		Class stubClass = Class.forName (remoteInterface + "STUB");
		result = (Stub)(stubClass.newInstance ());
	    } catch (Exception ex) {
		Debug.out.println ("RMISoapWriterImpl.serialize: failed to build stub object!");
		throw new RuntimeException ("Failed to build stub object " + ex);
	    }
	    stubs.put (object, result);
	    Stub.lookup.put (result.getObjectId (), object);
	}
	return result;
    }

    public String uniqueId () {
	return "remote" + ++numStubs;
    }

}
