package ajeffrey.teaching.http.client.logic;

import java.io.PrintWriter;

/**
 * A factory for building HTTP connections.
 * @author Alan Jeffrey
 * @version 1.0.1
 */
public interface ConnectionFactory {

    public Connection build (String url, PrintWriter out);

}
