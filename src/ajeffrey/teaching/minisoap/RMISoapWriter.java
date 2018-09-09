package ajeffrey.teaching.minisoap;

import com.macfaq.io.SafePrintWriter;
import ajeffrey.teaching.debug.Debug;
import java.io.IOException;

public interface RMISoapWriter extends SoapWriter {
   
    public static SoapWriterFactory factory = new RMISoapWriterFactoryImpl ();

}

class RMISoapWriterFactoryImpl extends SoapWriterFactoryImpl {

    public SoapWriter build (final SafePrintWriter out) {
	return new RMISoapWriterImpl (out);
    }

}

class RMISoapWriterImpl extends SoapWriterImpl {

    protected RMISoapWriterImpl (final SafePrintWriter out) {
	super (out);
    }

    public void serialize (final Object obj) throws IOException {
	if (obj instanceof Remote) {
	    Debug.out.println ("RMISoapWriterImpl.serialize: serializing stub instead of object!");
	    super.serialize (Stub.factory.build ((Remote)obj));
	} else {
	    super.serialize (obj);
	}
    }

}
