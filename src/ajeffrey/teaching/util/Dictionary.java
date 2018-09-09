package ajeffrey.teaching.util;

import java.util.HashMap;
import java.util.Arrays;

// An immutable dictionary implementation.
//
// This implementation relies on an aggregation:
//
//  +-----------+   +-----------+
//  | OuterDict |-->| InnerDict |
//  +-----------+   +-----------+
//
// the important property being that the inner dictionary
// never escapes from the outer dictionary, so there's only
// ever one pointer to the inner dictionary.  Since there's
// only one pointer to the inner dictionary, we can use a
// hash table for it.
//
// This implementation combines a linked list and a hash table:
// the linked list is terminated by a hash table.  Every time
// dict.add (key, value) is called, a cell containing a hash
// table is returned, so accessing the most recent dictionary is
// always efficient.
//
// This implementation gives the benefits of immutable programming
// (for example, it makes implementing "undo" trivial) with the
// efficiency of a hash table.
//
// In order to present an implementation-independent interface,
// we always return the array of keys in sorted order.  This means
// that the keys must implement the Comparable interface.

public interface Dictionary {

    public Dictionary add (Object key, Object value);
    public Dictionary remove (Object key);
    public Object get (Object key);
    public boolean containsKey (Object key);
    public Object[] getKeys ();
    public int size ();

    // The touch method makes the dictionary as efficient-to-use
    // as possible (after calling touch, the dictionary will
    // become a handle for a hash table).
    public Dictionary touch ();

    public static final Dictionary empty = new DictEmptyImpl ();

}

class DictEmptyImpl implements Dictionary {

    public Dictionary add (Object key, Object value) {
	return new DictOuterImpl ().add (key, value);
    }

    public Dictionary remove (Object key) {
	return this;
    }

    public Object get (Object key) {
	return null;
    }

    public boolean containsKey (Object key) {
	return false;
    }

    public Dictionary touch () {
	return this;
    }

    final Object[] keys = new Object[0];

    public Object[] getKeys () {
	return keys;
    }

    public int size () {
	return 0;
    }

}

class DictOuterImpl implements Dictionary {

    Dictionary contents;

    DictOuterImpl (Dictionary contents) {
	this.contents = contents;
    }

    DictOuterImpl () {
	this.contents = new DictInnerImpl ();
    }

    public Dictionary add (Object key, Object value) {
	contents = contents.touch ();
	Object oldValue = contents.get (key);
	contents = contents.add (key, value);
	Dictionary result = new DictOuterImpl (contents);
	if (oldValue == null) {
	    contents = new DictRemoveImpl (key, result);
	} else {
	    contents = new DictAddImpl (key, oldValue, result);
	}
	return result;
    }

    public Dictionary remove (Object key) {
	contents = contents.touch ();
	Object oldValue = contents.get (key);
	if (oldValue == null) {
	    return this;
	} else {
	    contents = contents.remove (key);
	    Dictionary result = new DictOuterImpl (contents);
	    contents = new DictAddImpl (key, oldValue, result);
	    return result;
	}
    }

    public Object get (Object key) {
	contents = contents.touch ();
	return contents.get (key);
    }

    public boolean containsKey (Object key) {
	return contents.containsKey (key);
    }

    public Object[] getKeys () {
	return contents.getKeys ();
    }

    public int size () {
	return contents.size ();
    }

    public Dictionary touch () {
	// To make ourselves as efficient as possible,
	// we touch our contents.  If our contents is
	// is an Inner class, then we return ourselves
	// (since Inner classes should not be leaked to
	// the outside world).  Otherwise we return
	// our contents.
	contents = contents.touch ();
	if (contents instanceof DictInnerImpl) {
	    return this;
	} else {
	    return contents;
	}
    }

}

// It is only safe to use a DictInnerImpl when we know
// that there is only ever one pointer to this object,
// and that we discard the pointer after calling add (key, value)
// or remove (key).
// In particular, this means we should wrap all Dictionary objects
// inside a DictOuter object.

class DictInnerImpl implements Dictionary {

    final HashMap contents = new HashMap ();

    public Dictionary add (Object key, Object value) {
	// Note that this method updates the current object,
	// which is why we insist that the current object be
	// discarded after use!
	if (!contents.containsKey (key)) { keys = null; }
	contents.put (key, value);
	return this;
    }

    public Dictionary remove (Object key) {
	// Note that this method updates the current object,
	// which is why we insist that the current object be
	// discarded after use!
	keys = null;
	contents.remove (key);
	return this;	
    }

    public Object get (Object key) {
	return contents.get (key);
    }

    public boolean containsKey (Object key) {
	return contents.containsKey (key);
    }

    Object[] keys = null;

    public Object[] getKeys () {
	if (keys == null) {
	    keys = contents.keySet ().toArray ();
	    Arrays.sort (keys);
	}
	return keys;
    }

    public int size () {
	return contents.size ();
    }

    public Dictionary touch () {
	return this;
    }

}

// We use a linked list implementation for dictionaries other
// than the most recently touched one.

class DictAddImpl implements Dictionary {

    final Object key;
    final Object value;
    final Dictionary rest;

    DictAddImpl (Object key, Object value, Dictionary rest) {
	this.key = key;
	this.value = value;
	this.rest = rest;
    }

    public Dictionary add (Object key, Object value) {
	return rest.add (this.key, this.value).add (key, value);
    }

    public Dictionary remove (Object key) {
	return rest.add (this.key, this.value).remove (key);
    }

    public Object get (Object key) {
	return rest.add (this.key, this.value).get (key);
    }

    public boolean containsKey (Object key) {
	return rest.add (this.key, this.value).containsKey (key);
    }

    public Object[] getKeys () {
	return rest.add (key, value).getKeys ();
    }

    public int size () {
	return rest.add (key, value).size ();
    }

    public Dictionary touch () {
	return rest.add (key, value);
    }

}

class DictRemoveImpl implements Dictionary {

    final Object key;
    final Dictionary rest;

    DictRemoveImpl (Object key, Dictionary rest) {
	this.key = key;
	this.rest = rest;
    }

    public Dictionary add (Object key, Object value) {
	return rest.remove (this.key).add (key, value);
    }

    public Dictionary remove (Object key) {
	return rest.remove (this.key).remove (key);
    }

    public Object get (Object key) {
	return rest.remove (this.key).get (key);
    }

    public boolean containsKey (Object key) {
	return rest.remove (this.key).containsKey (key);
    }

    public Object[] getKeys () {
	return rest.remove (key).getKeys ();
    }

    public int size () {
	return rest.remove (key).size ();
    }

    public Dictionary touch () {
	return rest.remove (key);
    }

}
