package ajeffrey.teaching.util.list;

import java.util.NoSuchElementException;
import ajeffrey.teaching.debug.Debug;

/**
 * An ordered collection of elements.
 * This is a cut down version of the Java collections library,
 * intended for teaching purposes.
 * @author Alan Jeffrey
 * @version 1.0.4
 * @see MutableListFactory
 */
public interface MutableList {

    /**
     * Add a new element to the list.
     * @param element the object to add
     */
    public void add (Object element);

    /**
     * Remove an element from the list.
     * @param element the object to remove
     * @exception NoSuchElementException thrown if the element 
     *   is not in the list.
     */
    public void remove (Object element);

    /**
     * Get an iterator over the elements in the list.
     * @return an iterator over the elements in the list
     */
    public Iterator iterator ();

    /**
     * The size of the list.
     * @return the size of the list
     */
    public int size ();

    /**
     * A factory for building mutable lists.
     */
    public static final MutableListFactory factory = new MutableListFactoryImpl ();

}

class MutableListFactoryImpl implements MutableListFactory {

    public MutableList build () {
	return new MutableListImpl ();
    }

}

class MutableListImpl implements MutableList {

    protected ImmutableList contents = ImmutableList.empty;
    protected final Object lock = new Object ();

    public void add (final Object element) {
        synchronized (lock) {
	    Debug.out.println 
		("MutableListImpl.add: Starting...");
	    final ImmutableList oldContents = contents;
	    Debug.out.println 
		("MutableListImpl.add: Calling " + 
		 oldContents + ".cons (" + element + ")");
	    final ImmutableList newContents = oldContents.cons (element);
	    Debug.out.println 
		("MutableListImpl.add: setting contents = " +
		 newContents);
	    contents = newContents;
	    Debug.out.println 
		("MutableListImpl.add: ...done.");
	}
    }

    public void remove (final Object element) {
	synchronized (lock) {
	    Debug.out.println 
		("MutableListImpl.remove: Starting...");
	    final ImmutableList oldContents = contents;
	    Debug.out.println 
		("MutableListImpl.remove: Calling " + oldContents + 
		 ".remove (" + element + ")");
	    final ImmutableList newContents = oldContents.remove (element);
	    Debug.out.println 
		("MutableListImpl.remove: setting contents = " + 
		 newContents);
	    contents = newContents;
	    Debug.out.println 
		("MutableListImpl.remove: ...done.");
	}
    }

    public Iterator iterator () { 
	return contents.iterator ();
    }

    public int size () {
	return contents.size ();
    }

    public String toString () {
	return "{ contents = " + contents + " }";
    }

}
