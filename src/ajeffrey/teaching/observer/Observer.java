package ajeffrey.teaching.observer;

/**
 * An Observer object from the Gang of Four's Observer/Observed pattern
 * (also known as publish/subscribe).
 * An observer object is one which can attach itself to a 
 * Subject object, and wait for changes to the Subject's state.
 * When the Subject changes state, it will call the Observer's
 * <code>update</code> method, to notify it that it should update itself.
 * @author Alan Jeffrey
 * @version 1.0.2
 * @see Subject
 */
public interface Observer {

    public void update ();

}
