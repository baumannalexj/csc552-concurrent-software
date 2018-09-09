package ajeffrey.teaching.date;

import ajeffrey.teaching.debug.Debug;
import java.util.Date;

interface Model {

    public void connect (String hostName);
    public Date getDate ();

    public static final ModelFactory factory = new ModelFactoryImpl ();

}

class ModelFactoryImpl implements ModelFactory {

    public Model build () { return new ModelImpl (); }

}

class ModelImpl implements Model {

    DateFactory factory = DateFactory.singleton;

    public void connect (String hostName) {
        Debug.out.println ("Model.connect (" + hostName + "): Starting...");
	// Fix this!
        Debug.out.println ("Model.connect: Throwing exception!");
	throw new RuntimeException ("This method is not implemented!");
    }

    public Date getDate () {
        Debug.out.println ("Model.getDate: Starting...");
	Date result = factory.build ();
        Debug.out.println ("Model.getDate: Returning " + result);
	return result;
    }

}
