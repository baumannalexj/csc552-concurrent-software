package ajeffrey.teaching.test;

import ajeffrey.teaching.minisoap.SoapWriter;
import ajeffrey.teaching.minisoap.SoapReader;
import ajeffrey.teaching.minisoap.SoapException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.io.IOException;

public class TestTransientSoap {

    public static void main (String[] args) throws IOException {
	PipedOutputStream os = new PipedOutputStream ();
	PipedInputStream is = new PipedInputStream (os);
	SoapWriter debug = SoapWriter.factory.build (System.out);
	SoapWriter out = SoapWriter.factory.build (os);
	SoapReader in = SoapReader.factory.build (is);
	try {
	    ReallySerializable x = new ReallySerializable ();
	    Unserializable y = new Unserializable ();
	    x.okay = y;
	    debug.serialize (x);
	    debug.flush ();
	    out.serialize (x);
	    out.flush ();
	    System.out.println ("SHOULD HAPPEN " + in.unserialize ());
            System.out.println ("SHOULD PRINT  RS (null)");
	} catch (SoapException ex) {
	    System.out.println ("SHOULDN'T HAPPEN " + ex);
	}        
    }

}

