package ajeffrey.teaching.size.event;

/**
 * A factory for SIZE request multiplexers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface SizeRequestMultiplexerFactory {

    /**
     * Build a new multiplexer.
     */
    public SizeRequestMultiplexer build ();

}
