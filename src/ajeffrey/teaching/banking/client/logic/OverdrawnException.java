package ajeffrey.teaching.banking.client.logic;

/**
 * This exception is thrown if a user tries to withrdraw more money
 * than they have.
 * @author Alan Jeffrey
 * @version 1.0.2
 * @see Logic
 */
public class OverdrawnException extends Exception {

    public OverdrawnException () {}

    public OverdrawnException (String msg) { super (msg); }

}
