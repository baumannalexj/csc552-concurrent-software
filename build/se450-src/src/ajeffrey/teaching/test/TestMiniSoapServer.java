package ajeffrey.teaching.test;

import ajeffrey.teaching.minisoap.Remote;
import ajeffrey.teaching.minisoap.Naming;
import ajeffrey.teaching.debug.Debug;

public class TestMiniSoapServer implements TestMiniSoapIface, Remote {

    public static void main (String[] args) throws Exception {
	Debug.out.addPrintStream (System.out);
	Naming.singleton.rebind ("//localhost/test", new TestMiniSoapServer ());
    }

    public String getRemoteInterface () {
	return "ajeffrey.teaching.test.TestMiniSoapIface";
    }

    public void throwIt () throws TestMiniSoapException {
	throw new TestMiniSoapException ("hello");
    }

}
