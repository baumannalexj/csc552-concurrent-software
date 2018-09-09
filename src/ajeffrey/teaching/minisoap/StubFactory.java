package ajeffrey.teaching.minisoap;

public interface StubFactory {

    public Stub build (Remote object);
    public String uniqueId ();

}
