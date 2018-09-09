package ajeffrey.teaching.hash2.iface;

/**
 * The interface for a remote hash table.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface Hash {

    public void put (Object key, Object value);
    public Object get (Object key);

}
