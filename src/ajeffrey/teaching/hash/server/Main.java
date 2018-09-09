package ajeffrey.teaching.hash.server;

import ajeffrey.teaching.hash.iface.Hash;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import java.util.Hashtable;

import java.io.Serializable;

public class Main {

    public static void main (String[] args) throws Exception {
	System.setSecurityManager (new RMISecurityManager ());
	Naming.rebind ("hashserver", new HashImpl ());
    }

}

class HashImpl extends UnicastRemoteObject implements Hash {

    protected final Hashtable contents = new Hashtable ();
    
    protected HashImpl () throws RemoteException {}

    public void put (Serializable key, Serializable value) 
      throws RemoteException 
    {
	contents.put (key, value);
    }

    public Serializable get (Serializable key) 
      throws RemoteException 
    {
	return (Serializable)(contents.get (key));
    }


}
