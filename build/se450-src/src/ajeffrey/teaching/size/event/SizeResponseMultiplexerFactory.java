package ajeffrey.teaching.size.event;

/**
 * A factory for SIZE repsonse multiplexers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface SizeResponseMultiplexerFactory {

    /**
     * Build a new multiplexer.
     */
    public SizeResponseMultiplexer build ();

}
