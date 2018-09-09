package ajeffrey.teaching.hash2.iface;
import ajeffrey.teaching.minisoap.Host;
import ajeffrey.teaching.minisoap.Stub;
import ajeffrey.teaching.minisoap.RMI;
public class HashSTUB implements Stub, Hash {
  public Host host = Host.localhost;
  public String objectId = Stub.factory.uniqueId ();
  public Host getHost () { return host; }
  public String getObjectId () { return objectId; }
  static final String[] putParamTypeIds = new String[] { "java.lang.Object", "java.lang.Object" };
  public void put (java.lang.Object arg0, java.lang.Object arg1) {
    Object[] args = new Object [] { arg0, arg1 };
    try { RMI.singleton.remoteCall (host, objectId, "put", putParamTypeIds, args); }
    catch (Exception ex) { throw new RuntimeException ("Uncaught " + ex); }
  }
  static final String[] getParamTypeIds = new String[] { "java.lang.Object" };
  public java.lang.Object get (java.lang.Object arg0) {
    Object[] args = new Object [] { arg0 };
    try { return (java.lang.Object)(RMI.singleton.remoteCall (host, objectId, "get", getParamTypeIds, args)); }
    catch (Exception ex) { throw new RuntimeException ("Uncaught " + ex); }
  }
}
