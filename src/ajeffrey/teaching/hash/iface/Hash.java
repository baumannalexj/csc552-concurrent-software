package ajeffrey.teaching.hash.iface;

import java.rmi.*;
import java.io.*;

/**
 * The interface for a remote hash table.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface Hash extends Remote {

  public void put (Serializable key, Serializable value) throws RemoteException;
  public Serializable get (Serializable key) throws RemoteException;

}
