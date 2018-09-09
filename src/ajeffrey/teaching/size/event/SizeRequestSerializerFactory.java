package ajeffrey.teaching.size.event;

import java.io.OutputStream;

/**
 * A factory for building size request serializers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface SizeRequestSerializerFactory {

    /**
     * Build a new request serializer
     * @param out the output stream to send messages to
     * @return a serializer which sends messages on the given output stream
     */
    public SizeRequestSerializer build (OutputStream out);

}
