package ajeffrey.teaching.test;

public class Unserializable {
    public String privateStuff = "not to go out over the network";
    public String toString () { return "SUPER SECRET PRIVATE " + privateStuff; }
}
