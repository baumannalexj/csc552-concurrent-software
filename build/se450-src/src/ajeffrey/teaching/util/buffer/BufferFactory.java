package ajeffrey.teaching.util.buffer;

/**
 * A factory for building buffers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface BufferFactory {

    /**
     * Build a new buffer.
     * @param capacity the maximum size of the buffer
     * @return a new buffer
     */
    public Buffer build (int capacity);

}
