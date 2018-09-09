package ajeffrey.teaching.util.buffer;

/**
 * An interface for buffers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface Buffer {

    /**
     * The maximum number of elements the buffer can contain.
     * @return the capacity of the buffer
     */
    public int capacity ();

    /**
     * The number of elements the buffer currently contains.
     * @return the size of the buffer
     */
    public int size ();

    /**
     * Add an object into the buffer.
     * The object should not be null.
     * @param element the object to add
     * @exception BufferFullException thrown if the buffer is full
     * @exception IllegalArgumentException thrown if object==null
     */
    public void put (Object object);

    /**
     * Get an object from the buffer.
     * This method will never return null.
     * @return an object from the buffer
     * @exception BufferEmptyException thrown if the buffer is empty
     */
    public Object get ();

}
