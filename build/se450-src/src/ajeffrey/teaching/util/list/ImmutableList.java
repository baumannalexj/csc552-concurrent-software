package ajeffrey.teaching.util.list;

import java.util.NoSuchElementException;
import ajeffrey.teaching.debug.Debug;

/**
 * An immutable ordered collection of elements.
 * @author Alan Jeffrey
 * @version 1.0.5
 */
public interface ImmutableList {

    /**
     * Build a new list, with the current list as the tail,
     * and the new elemet as the head
     * @param element the object to add
     */
    public ImmutableList cons (Object head);

    /**
     * Get the size of the list.
     * @return the size of the list
     */
    public int size ();

    /**
     * Get the head of the list.
     * @return the head of the list
     * @exception NoSuchElementException thrown if the list is empty
     */
    public Object head ();

    /**
     * Get the tail of the list.
     * @return the tail of the list
     * @exception NoSuchElementException thrown if the list is empty
     */
    public ImmutableList tail ();

    /**
     * Remove one element from the list.
     * Removes the first element equal to the parameter.
     * @param element the element to remove
     * @exception NoSuchElementException thrown if the list does not
     *   contain the element
     * @return the list with the element removed.
     */
    public ImmutableList remove (Object element);

    /**
     * Get an iterator over the elements in the list.
     * @return an iterator over the elements in the list
     */
    public Iterator iterator ();

    /**
     * An empty list.
     */
    public final static ImmutableList empty = new ImmutableListEmpty ();

}

class ImmutableListEmpty implements ImmutableList {
	
    protected final Iterator it = new ImmutableListIterator (this);

    public ImmutableList cons (final Object element) {
	Debug.out.println ("ImmutableListEmpty.cons: Starting");
	ImmutableList result = new ImmutableListCons (element, this);
	Debug.out.println ("ImmutableListEmpty.cons: Returning " + result);
	return result;
    }

    public Object head () {
	Debug.out.println ("ImmutableListEmpty.head: Oops");
	throw new NoSuchElementException (); 
    }

    public ImmutableList tail () { 
	Debug.out.println ("ImmutableListEmpty.tail: Oops");
	throw new NoSuchElementException (); 
    }

    public int size () { 
	return 0; 
    }
   
    public Iterator iterator () { 
	return it; 
    }

    public ImmutableList remove (Object element) { 
	Debug.out.println ("ImmutableListEmpty.remove: Oops");
	throw new NoSuchElementException (); 
    }

    public String toString () {
	return "ImmutableList { }";
    }

}

class ImmutableListCons implements ImmutableList {
	
    protected final Object hd;
    protected final ImmutableList tl;
    protected final int sz;

    protected ImmutableListCons (final Object hd, final ImmutableList tl) {
	this.hd = hd;
	this.tl = tl;
	this.sz = tl.size () + 1;
	Debug.out.println ("ImmutableListCons: Built");
    }

    public ImmutableList cons (final Object element) {
	Debug.out.println ("ImmutableListCons.cons: Starting");
	ImmutableList result = new ImmutableListCons (element, this);
	Debug.out.println ("ImmutableListCons.cons: Returning " + result);
	return result;
    }

    public Object head () { 
	return hd; 
    }

    public ImmutableList tail () { 
	return tl; 
    }

    public int size () { 
	return sz; 
    }

    public Iterator iterator () { 
	return new ImmutableListIterator (this);
    }

    public ImmutableList remove (Object element) { 
	Debug.out.println ("ImmutableListCons.remove: Starting");
	if (hd.equals (element)) {
	    Debug.out.println ("ImmutableListCons.remove: Returning " + tl);
	    return tl;
	} else {
	    Debug.out.println 
		("ImmutableListCons.remove: Recursing " + tl + 
		 ".remove (" + element +").cons (" + hd + ")");
	    final ImmutableList result = tl.remove (element).cons (hd);
	    Debug.out.println ("ImmutableListCons.remove: Returning " + result);
	    return result;
	}
    }

    public String toString () {
	StringBuffer result = new StringBuffer ("ImmutableList { ");
	for (Iterator i = iterator (); i.hasNext ();) {
	    result.append (i.next ());
	    if (i.hasNext ()) { result.append (", "); }
	}
	return result.append (" }").toString ();
    }

}

class ImmutableListIterator implements Iterator {

    protected ImmutableList contents;

    protected ImmutableListIterator (final ImmutableList contents) {
	this.contents = contents;
    }

    public boolean hasNext () { return (contents.size () > 0); }

    public Object next () {
	Object result = contents.head ();
	contents = contents.tail ();
	return result;
    }

}
