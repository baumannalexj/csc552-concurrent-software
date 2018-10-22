package ajeffrey.teaching.minisoap;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface Host {

    public String getName ();
    public int getPort ();

    public static Host localhost = new HostImpl ();

}

class HostImpl implements Host {

    public String name;
    public int port;

    public HostImpl () { 
	try {
	    this.name = InetAddress.getLocalHost ().getHostName ();
	    this.port =  Executor.factory.getServerSocket ().getLocalPort ();
	} catch (UnknownHostException ex) {
	    throw new RuntimeException ("Local hostname lookup failed " + ex);
	}
    }

    public HostImpl (String name, int port) {
	this.name = name; this.port = port;
    }

    public String getName () { return name; }
    public int getPort () { return port; }

    public String toString () { return name + ":" + port; }
    public int hashCode () { return port + 2*name.hashCode (); }
    public boolean equals (Object other) { 
	if (other instanceof Host) {
	    Host otherHost = (Host)other;
	    return this.name.equals (otherHost.getName ()) 
		&& this.port == otherHost.getPort ();
	} else {
	    return false;
	}
    }

}
