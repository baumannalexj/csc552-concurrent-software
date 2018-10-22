package ajeffrey.teaching.test;

import ajeffrey.teaching.minisoap.Naming;
import ajeffrey.teaching.debug.Debug;

public class TestMiniSoapClient {

    public static void main (String[] args) throws Exception {
	Debug.out.addPrintStream (System.out);
	TestMiniSoapIface test = (TestMiniSoapIface)(Naming.singleton.lookup ("//localhost/test"));
	try {
	    test.throwIt ();
	} catch (TestMiniSoapException ex) {
	    System.out.println ("Exception caught! " + ex);
	    System.exit (0);
	}
    }

}
