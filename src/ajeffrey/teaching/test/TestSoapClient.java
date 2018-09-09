package ajeffrey.teaching.test;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.minisoap.SoapWriter;
import java.io.IOException;

import java.net.Socket;

public class TestSoapClient {

    public static void main (final String[] args) throws IOException {
	// Debug.out.setPrintStream (System.err);
	final Socket socket = new Socket ("localhost", 2000);
	final SoapWriter out = SoapWriter.factory.build (socket.getOutputStream ());
	final SoapWriter debugSoap = SoapWriter.factory.build (System.out);
	final TestSoapCons loopMarker = new TestSoapCons (args[0], new TestSoapEmpty ());
	TestSoapList test = loopMarker;
	for (int i=1; i < args.length; i++) {
	    if (args[i].equals ("LOOP")) {
		loopMarker.tl = test;
	    } else {
		test = new TestSoapCons (args[i], test);
	    }
	}
	System.out.println ("Sending " + test);
        debugSoap.serialize (test);
	debugSoap.flush ();
	out.serialize (test);
	out.flush ();
	socket.close ();
    }
}

