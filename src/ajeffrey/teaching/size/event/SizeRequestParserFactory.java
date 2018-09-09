package ajeffrey.teaching.size.event;

import java.io.InputStream;

/**
 * A factory for size request parsers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface SizeRequestParserFactory {

    /**
     * Build a new size request parser.
     * @param in the input stream to read from
     * @return a new size request parser.
     */
    public SizeRequestParser build (InputStream in);

}
