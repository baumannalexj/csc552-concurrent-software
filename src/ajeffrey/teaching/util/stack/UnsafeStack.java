package ajeffrey.teaching.util.stack;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface UnsafeStack {

    public void push (Object element);
    public Object pop ();
    public int size ();
    public Iterator iterator ();
  
    public static final UnsafeStackFactory factory = new UnsafeStackFactoryImpl ();

}

class UnsafeStackFactoryImpl implements UnsafeStackFactory {

    public UnsafeStack build () { return new UnsafeStackImpl (); }

}

class UnsafeStackImpl implements UnsafeStack {

    protected Object[] contents = new Object[1];
    protected int size = 0;

    public void push (Object element) {
	while (size == contents.length) { grow (); }
	contents[size++] = element;
    }
    
    public Object pop () {
	if (size == 0) { throw new NoSuchElementException (); }
	final Object result = contents[--size];
	contents[size] = null;
	return result;
    }

    public int size () { return size; }

    public Iterator iterator () { return new UnsafeStackIterator (contents, size); }

    protected void grow () {
	Object[] newContents = new Object[contents.length * 2];
	System.arraycopy (contents, 0, newContents, 0, size);
	contents = newContents;
    }

}

class UnsafeStackIterator implements Iterator {

    protected final Object[] contents;
    protected final int size;
    protected int current = 0;

    UnsafeStackIterator (final Object[] contents, final int size) { 
	this.contents = contents; this.size = size;
    }

    public boolean hasNext () { return current < size; }

    public Object next () { return contents[current++]; }

    public void remove () { throw new UnsupportedOperationException (); }

}
