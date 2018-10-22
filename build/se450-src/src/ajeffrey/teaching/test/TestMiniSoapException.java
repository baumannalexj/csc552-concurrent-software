package ajeffrey.teaching.test;

public class TestMiniSoapException extends Exception {

    public String msg;

    public TestMiniSoapException () { this ("I got thown!"); }

    public TestMiniSoapException (String msg) { this.msg = msg; }

    public String toString () { return "TestMiniSoapException (\"" + msg + "\")"; }

}
