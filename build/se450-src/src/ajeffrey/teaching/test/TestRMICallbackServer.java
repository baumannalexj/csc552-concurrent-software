package ajeffrey.teaching.test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class TestRMICallbackServer {

    public static void main (String[] args) throws Exception {
	Foo fooImpl = new FooImpl ();
	System.setSecurityManager (new RMISecurityManager ());
	Naming.rebind ("foo", fooImpl);	
    }

}

class FooImpl extends UnicastRemoteObject implements Foo {

    public FooImpl () throws RemoteException {}

    public Bar getBar () throws RemoteException { 
	return new BarImpl (); 
    }

    public void useBaz (final Baz b) throws RemoteException {
	b.go ();
    }

}

class BarImpl extends UnicastRemoteObject implements Bar {

    public BarImpl () throws RemoteException {}

    public void go () throws RemoteException { 
	System.out.println ("Bar created at server"); 
    }

}
