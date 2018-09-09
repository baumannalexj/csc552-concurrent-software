package ajeffrey.teaching.size.event;

import java.io.OutputStream;

/**
 * A factory for building size response serializers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface SizeResponseSerializerFactory {

    /**
     * Build a new response serializer
     * @param out the output stream to send messages to
     * @return a serializer which sends messages on the given output stream
     */
    public SizeResponseSerializer build (OutputStream out);

}
