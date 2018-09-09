package ajeffrey.teaching.util.buffer;

import ajeffrey.teaching.debug.Debug;

/**
 * An unsafe class for buffers.
 * @author Alan Jeffrey
 * @version 1.0.1
 */

public interface UnsafeBuffer {

    /**
     * A factory for building unsafe buffers.
     */
    public static BufferFactory factory = new UnsafeBufferFactoryImpl ();

}

class UnsafeBufferFactoryImpl implements BufferFactory {

    public Buffer build (int capacity) { 
	return new UnsafeBufferImpl (capacity);
    }

}

class UnsafeBufferImpl implements Buffer {

    protected final int capacity;
    protected final Object[] contents;
    protected int first;
    protected int last;
    protected int size;

    UnsafeBufferImpl (final int capacity) {
	this.capacity = capacity;
	this.contents = new Object[capacity];
	this.first = 0;
	this.last = 0;
	this.size = 0;
    }
    
    protected void checkInvariants () {
	Debug.out.println ("UnsafeBuffer.checkInvariants (): State = " + this);
	Debug.out.assertion (size <= capacity);
	Debug.out.assertion ((size % capacity) == ((capacity + last - first) % capacity));
	Debug.out.assertion (capacity == contents.length);
	for (int i=0; i < size; i++) {
	    Debug.out.assertion (contents[(first+i)%capacity] != null);
	}
	for (int i=size; i < capacity; i++) {
	    Debug.out.assertion (contents[(first+i)%capacity] == null);
	}
    }

    public int capacity () { 
	return capacity; 
    }

    public int size () { 
	return size; 
    }

    public void put (final Object object) {
	Debug.out.println ("UnsafeBuffer.put: Starting");
	Debug.out.println ("UnsafeBuffer.put: Checking !full");
	if (size == capacity) { throw new BufferFullException (); }
	Debug.out.println ("UnsafeBuffer.put: Checking object != null");
	if (object == null) { throw new IllegalArgumentException (); }
	Debug.out.println ("UnsafeBuffer.put: Begin critical section.");
	contents[last] = object;
	Debug.out.println ("UnsafeBuffer.put: Set contents[" + last + "] = " + object);
	final int newLast = (last + 1) % capacity;
	Debug.out.println ("UnsafeBuffer.put: Set newLast = " + newLast);
	last = newLast;
	Debug.out.println ("UnsafeBuffer.put: Setting last = " + last);
	final int newSize = size + 1;
	Debug.out.println ("UnsafeBuffer.put: Set newSize = " + newSize);
	size = newSize;
	Debug.out.println ("UnsafeBuffer.put: Setting size = " + size);
	Debug.out.println ("UnsafeBuffer.put: End critical section.");
	checkInvariants ();
	Debug.out.println ("UnsafeBuffer.put: Returning");
    }

    public Object get () {
	Debug.out.println ("UnsafeBuffer.get: Starting");
	Debug.out.println ("UnsafeBuffer.get: Checking !empty");
	if (size == 0) { throw new BufferEmptyException (); }
	Debug.out.println ("UnsafeBuffer.get: Begin critical section.");
	final Object result = contents[first];
	Debug.out.println ("UnsafeBuffer.get: Set result = " + result);
	contents[first] = null;
	Debug.out.println ("UnsafeBuffer.get: Set contents[" + first + "] = null");
	final int newFirst = (first + 1) % capacity;
	Debug.out.println ("UnsafeBuffer.get: Set newFirst = " + newFirst);
	first = newFirst;
	Debug.out.println ("UnsafeBuffer.get: Set first = " + first);
	final int newSize = size - 1;
	Debug.out.println ("UnsafeBuffer.get: Set newSize = " + newSize);
	size = size - 1;
	Debug.out.println ("UnsafeBuffer.get: Set size = " + size);
	Debug.out.println ("UnsafeBuffer.get: End critical section");
	checkInvariants ();
	Debug.out.println ("UnsafeBuffer.get: Returning " + result);
	return result;
    }

    public String toString () {
	StringBuffer result = new StringBuffer 
	    ("UnsafeBuffer { capacity=" + capacity + ", size=" + size + 
	     ", first=" + first + ", last=" + last);
	for (int i=0; i<capacity; i++) {
	    result.append (", contents[" + i + "]=" + contents[i]);
	}
	result.append (" }");
	return result.toString ();
    }

}
