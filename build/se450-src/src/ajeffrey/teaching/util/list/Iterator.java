package ajeffrey.teaching.util.list;

/**
 * An iterator over a collection.
 * This is an implementation of the Iterator pattern from the 
 * `Gang of Four' book.  It is a simplified version of the
 * Java <code>Iterator</code> interface, and does not support
 * deletion.  Collections can return Iterator objects to iterate
 * over them, for example:
 * <pre>
 *   List l = ...;
 *   for (Iterator i = l.iterator (); i.hasNext ();) {
 *     Object next = i.next ();
 *     ...
 *   }
 * </pre>
 * If the underlying list is modified while the iterator is being used,
 * one of two things may happen:
 * <ul>
 * <li>A <code>IteratorModificationException</code> may be thrown.</li>
 * <li>The iterator will iterate over the elements in the original
 *   list, not over the elements in the new list.</li>
 * </ul>
 * See Lea for a discussion of concurrent updates to collections.
 * @author Alan Jeffrey
 * @version 1.0.1
 * @see List
 */
public interface Iterator {

    /**
     * Are there more elements in the iterator?
     * @return true if there are more elements in the iterator
     */
    public boolean hasNext ();

    /**
     * The next element in the iterator
     * @return the next element in the iterator.
     */
    public Object next ();

}
