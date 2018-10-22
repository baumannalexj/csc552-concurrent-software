package ajeffrey.teaching.util.list;

import java.util.NoSuchElementException;
import java.util.WeakHashMap;

import ajeffrey.teaching.debug.Debug;

/**
 * <p>This is an implementation of immutable lists, using the 
 * `Flyweight' pattern from the `Gang of Four' book
 * (this technique is also called `hash consing' in the LISP world).</p>
 * 
 * <p>For each list <code>foo</code>, we keep a hash table of all the lists
 * <code>bar</code> such that <code>bar.tail() == foo</code>, indexed
 * by <code>bar.head()</code>.  When we call <code>foo.cons(hd)</code>
 * we look up <code>hd</code>'s entry in the hash-table: if it has one,
 * we return it <i>without building a new cons cell</i>.  If it doesn't,
 * we build a new cons cell, put it in the hash table, and return it.</p>
 * 
 * <p>This all means that for any object <code>hd</code> and list 
 * <code>tl</code>, there is at most one list <code>l</code> where
 * <code>l.head() == hd</code> and <code>l.tail() == tl</code>.
 * This implementation is space-efficient, but at the cost of
 * more work when cons cells are created.</p>
 * 
 * @author Alan Jeffrey
 * @version 1.0.3
 */
public interface FlyweightImmutableList {

    /**
     * An empty list.
     */
    public static final ImmutableList empty = new FlyweightEmpty ();

}

class FlyweightFactory {

    protected static ImmutableList build 
	(final Object hd, final ImmutableList tl, final WeakHashMap cache) 
    {
	Debug.out.println ("FlyweightFactory.build: Starting");
	Debug.out.println ("FlyweightFactory.build: Cache lookup for " + hd);
	ImmutableList result = (ImmutableList)(cache.get (hd));
	Debug.out.println ("FlyweightFactory.build: Cache found " + result);
	if (result == null) {
	    Debug.out.println ("FlyweightFactory.build: Cache miss");
	    Debug.out.println ("FlyweightFactory.build: Grabbing lock");
	    synchronized (cache) {
		Debug.out.println ("FlyweightFactory.build: Grabbed lock");
		result = (ImmutableList)(cache.get (hd));
		Debug.out.println ("FlyweightFactory.build: Cache found " + result);
		if (result == null) {
		    Debug.out.println ("FlyweightFactory.build: Still a cache miss");
		    result = new FlyweightCons (hd, tl);
		    Debug.out.println ("FlyweightFactory.build: Adding " + result);
		    cache.put (hd, result);
		} else {
		    Debug.out.println ("FlyweightFactory.build: Now a cache hit");
		}
		Debug.out.println ("FlyweightFactory.build: Releasing lock");
	    }
	} else {
	    Debug.out.println ("FlyweightFactory.build: Cache hit");
	}
	Debug.out.println ("FlyweightFactory.build: Returning " + result);
	return result;
    }

}

class FlyweightEmpty extends ImmutableListEmpty {

    protected final WeakHashMap cache = new WeakHashMap ();

    public ImmutableList cons (final Object element) {
	return FlyweightFactory.build (element, this, cache);
    }

}

class FlyweightCons extends ImmutableListCons {

    protected final WeakHashMap cache = new WeakHashMap ();

    protected FlyweightCons (final Object hd, final ImmutableList tl) {
	super (hd, tl);
    }

    public ImmutableList cons (final Object element) {
	return FlyweightFactory.build (element, this, cache);
    }

}

