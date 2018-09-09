package ajeffrey.teaching.minisoap;

import java.util.HashMap;
import java.net.URL;
import java.net.MalformedURLException;

public interface Naming {

    public void rebind (String url, Object object) throws MalformedURLException;
    public Object lookup (String url) throws MalformedURLException;

    public void rebindLocal (String objectId, Object object);
    public Object lookupLocal (String objectId);

    public final static Naming singleton = new NamingImpl ();
    public final static int port = 1099;
    public final static String objectId = "naming";

}

class NamingImpl implements Naming, Remote {

    final HashMap lookup = new HashMap ();

    public void rebindLocal (String objectId, Object object) {
	lookup.put (objectId, object);
    }

    public Object lookupLocal (String objectId) {
	return lookup.get (objectId);
    }

    String[] rebindArgTypeIds = new String[] { "java.lang.String", "java.lang.Object" };
    public void rebind (String urlName, Object object) throws MalformedURLException {
	URL url = new URL ("http:" + urlName);  // cheat!
	String hostName = url.getHost ();
	String objectId = url.getPath ();
	int port = url.getPort (); if (port == -1) { port = Naming.port; }	
	Host host = new HostImpl (hostName, port);
	Object[] args = new Object[] { objectId, object };
	try {
	    RMI.singleton.remoteCall (host, Naming.objectId, "rebindLocal", rebindArgTypeIds, args);
	} catch (Exception ex) {
	    throw new RuntimeException ("rebind failed: " + ex);
	}
    }

    String[] lookupArgTypeIds = new String[] { "java.lang.String" };
    public Object lookup (String urlName) throws MalformedURLException {
	URL url = new URL ("http:" + urlName); // cheat!
	String hostName = url.getHost ();
	String objectId = url.getPath ();
	int port = url.getPort (); if (port == -1) { port = Naming.port; }	
	Host host = new HostImpl (hostName, port);
	Object[] args = new Object[] { objectId };
	try {
	    return RMI.singleton.remoteCall (host, Naming.objectId, "lookupLocal", lookupArgTypeIds, args);
	} catch (Exception ex) {
	    throw new RuntimeException ("lookup failed: " + ex);
	}
    }

    public String getRemoteInterface () { return "ajeffrey.teaching.minisoap.Naming"; }

}

