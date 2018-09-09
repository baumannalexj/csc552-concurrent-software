package ajeffrey.teaching.test;

public class TestSoapCons extends TestSoapList {

    public Object hd;
    public TestSoapList tl;
    public int size;

    public TestSoapCons (final Object hd, final TestSoapList tl) {
	this.hd = hd; this.tl = tl; this.size = tl.size () + 1;
    }

    public TestSoapCons () {
	this.hd = null; this.tl = null; this.size = -1;
    }

    public int size () { return size; }
    public Object head () { return hd; }
    public TestSoapList tail () { return tl; }

}
