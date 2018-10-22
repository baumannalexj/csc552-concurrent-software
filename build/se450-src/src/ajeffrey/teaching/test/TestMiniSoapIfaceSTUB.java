package ajeffrey.teaching.test;
import ajeffrey.teaching.minisoap.Host;
import ajeffrey.teaching.minisoap.Stub;
import ajeffrey.teaching.minisoap.RMI;
public class TestMiniSoapIfaceSTUB implements Stub, TestMiniSoapIface {
  public Host host = Host.localhost;
  public String objectId = Stub.factory.uniqueId ();
  public Host getHost () { return host; }
  public String getObjectId () { return objectId; }
  static final String[] throwItParamTypeIds = new String[] {  };
  public void throwIt () throws ajeffrey.teaching.test.TestMiniSoapException {
    Object[] args = new Object [] {  };
    try { RMI.singleton.remoteCall (host, objectId, "throwIt", throwItParamTypeIds, args); }
    catch (ajeffrey.teaching.test.TestMiniSoapException ex) { throw ex; }
    catch (Exception ex) { throw new RuntimeException ("Uncaught " + ex); }
  }
}
