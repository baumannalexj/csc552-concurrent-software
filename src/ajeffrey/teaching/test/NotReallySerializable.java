package ajeffrey.teaching.test;
import java.io.Serializable;

public class NotReallySerializable implements Serializable {
    public Unserializable ohDear; // hmm...
    public String publicStuff = "this can go out over the network";
    public String toString () { return "NRS (" + ohDear + ")"; }
}

