package ajeffrey.teaching.test;
import java.io.Serializable;

public class ReallySerializable implements Serializable {
    public transient Unserializable okay; // OK since it's transient
    public String publicStuff = "this can go out over the network";
    public String toString () { return "RS (" + okay + ")"; }
}

