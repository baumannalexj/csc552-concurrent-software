package ajeffrey.teaching.util.list;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * optimistic mutable list, no locks used except through
 * the atomic lib
 *
 * @author alex baumann
 *
 * @see OptimisticMutableList
 */
public interface OptimisticMutableList {

    /**
     * A factory for building mutable lists.
     */
    public static final OptimisticMutableListFactory factory = new OptimisticMutableListFactoryImpl();

    /**
     * Add a new element to the list.
     *
     * @param element the object to add
     */
    public void add(Object element);

    /**
     * Remove an element from the list.
     *
     * @param element the object to remove
     * @throws NoSuchElementException thrown if the element
     *                                is not in the list.
     */
    public void remove(Object element);

    /**
     * Get an iterator over the elements in the list.
     *
     * @return an iterator over the elements in the list
     */
    public Iterator iterator();

    /**
     * The size of the list.
     *
     * @return the size of the list
     */
    public int size();

}

class OptimisticMutableListFactoryImpl implements OptimisticMutableListFactory {

    public MutableList build() {
        return new OptimisticMutableListImpl();
    }

}

class OptimisticMutableListImpl implements MutableList {

    protected AtomicReference<ImmutableList> contents = new AtomicReference<>(ImmutableList.empty);

    public void add(final Object element) {

        while (true) {
            ImmutableList oldList = contents.get();
            ImmutableList newList = new ImmutableListCons(element, oldList);

            if (contents.compareAndSet(oldList, newList)) {
                return;
            }
        }
    }

    public void remove(final Object element) {
        while (true) {
            ImmutableList oldList = contents.get();
            ImmutableList newList = oldList.remove(element);

            if (contents.compareAndSet(oldList, newList)) {
                return;
            }

        }
    }

    public Iterator iterator() {
        return contents.get().iterator();
    }

    public int size() {
        return contents.get().size();
    }

    public String toString() {
        return "{ contents = " + contents.get() + " }";
    }

}
