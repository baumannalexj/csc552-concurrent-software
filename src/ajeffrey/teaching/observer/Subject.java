package ajeffrey.teaching.observer;

import ajeffrey.teaching.util.list.Iterator;
import ajeffrey.teaching.util.list.MutableList;

/**
 * A Subject object from the Gang of Four's Observer/Observed pattern
 * (also known as publish/subscribe).
 * An subject object is one which can have Observers attached to it.
 * When the Subject changes state, it should call the 
 * <code>updateObservers</code> method, to notify all of the
 * attached observers that they should update themselves.
 * @author Alan Jeffrey
 * @version 1.0.2
 * @see Observer
 */
public interface Subject {

    /**
     * Add an observer to the list of observers observing this object.
     * @param observer the new Observer
     */
    public void attach (Observer observer);

    /**
     * Remove an observer from the list of observers observing this
     * object.
     * @param observer the Observer object to remove
     * @exception java.util.NoSuchElementException thrown if 
     *   the observer is not currently observing this object
     */
    public void detach (Observer observer);

    /**
     * Call the update method of all of the attached observers.
     */
    public void updateObservers ();

    /**
     * A factory for building new subjects.
     */
    public static SubjectFactory factory = new SubjectFactoryImpl ();

}

class SubjectFactoryImpl implements SubjectFactory {

    public Subject build () { return new SubjectImpl (); }

}

class SubjectImpl implements Subject {

    protected final MutableList observers = 
	MutableList.factory.build ();

    public void attach (final Observer observer) {
	observers.add (observer);
    }

    public void detach (final Observer observer) {
	observers.remove (observer);
    }

    public void updateObservers () {
	for (Iterator i = observers.iterator (); i.hasNext ();) {
	    Observer observer = (Observer)(i.next ());
	    observer.update ();
	}
    }

    public String toString () { return "Subject { }"; }

}
