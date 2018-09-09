package ajeffrey.teaching.test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class TestRMICallbackClient {
    
    public static void main (String[] args) throws Exception {
	Foo myFoo = (Foo)(Naming.lookup ("foo"));
	Bar myBar = myFoo.getBar ();
	Baz myBaz = new BazImpl ();
	myBar.go ();
	myFoo.useBaz (myBaz);
    }

}

class BazImpl extends UnicastRemoteObject implements Baz {

    public BazImpl () throws RemoteException {}

    public void go () throws RemoteException { 
	System.out.println ("Baz created at client"); 
    }

}
