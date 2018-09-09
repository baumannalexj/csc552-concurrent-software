package ajeffrey.teaching.test;
import java.util.NoSuchElementException;

public class TestSoapEmpty extends TestSoapList {

    public int size () { return 0; }
    public Object head () { throw new NoSuchElementException (); }
    public TestSoapList tail () { throw new NoSuchElementException (); }

}

