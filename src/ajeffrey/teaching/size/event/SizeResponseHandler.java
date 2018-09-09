package ajeffrey.teaching.size.event;

import java.io.IOException;

/**
 * An event handler for a SIZE response (either FOUND, NOT FOUND, or OK).
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface SizeResponseHandler {

    /**
     * Handle a FOUND response
     * @param size the size of the file
     */
    public void handleFoundResponse (String fileName, long size) throws IOException;

    /**
     * Handle a NOT FOUND response
     */
    public void handleNotFoundResponse () throws IOException;

    /**
     * Handle an OK response
     */
    public void handleOKResponse () throws IOException;

    /**
     * Handle the stream closing prematurely
     */
    public void handleEndOfStream () throws IOException;

}
