package ajeffrey.teaching.test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Foo extends Remote {

    public Bar getBar () throws RemoteException;
    public void useBaz (Baz b) throws RemoteException;

}
