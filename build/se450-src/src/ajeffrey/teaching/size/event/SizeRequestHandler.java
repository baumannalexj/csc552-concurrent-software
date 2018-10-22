package ajeffrey.teaching.size.event;

import java.io.IOException;

/**
 * An event handler for a SIZE request (either SIZE or QUIT).
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface SizeRequestHandler {

    /**
     * Handle a SIZE request
     * @param fileName the name of the file
     */
    public void handleSizeRequest (String fileName) throws IOException;

    /**
     * Handle a QUIT request
     */
    public void handleQuitRequest () throws IOException;

}
