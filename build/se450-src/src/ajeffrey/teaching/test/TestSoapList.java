package ajeffrey.teaching.test;

public abstract class TestSoapList {

    public abstract int size ();
    public abstract Object head ();
    public abstract TestSoapList tail ();
    public String toString () {
        final StringBuffer result = new StringBuffer ();
        final int size = size ();
        TestSoapList tmp = this;
	for (int i=0; i < size; i++) {
	    result.append ("cons (").append (tmp.head ()).append (", ");
	    tmp = tmp.tail ();
	}
	if (tmp.size () == 0) {
	    result.append ("empty");
	} else {
	    result.append ("...");
	}
	for (int i=0; i < size; i++) {
	    result.append (")");
	}
	return result.toString ();
    }

}
