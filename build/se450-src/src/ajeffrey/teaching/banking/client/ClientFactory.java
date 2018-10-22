package ajeffrey.teaching.banking.client;

/**
 * An interface for factories which build client objects.
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public interface ClientFactory {

    /**
     * Build a new client object.
     * @return a new client object.
     */
    public Client build ();

}
