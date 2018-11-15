package ajeffrey.teaching.util.stack;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class SafeStackImpl {
    private final UnsafeStack stack = UnsafeStack.factory.build();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private int version = 0;

    public void push(Object element) {
        Lock writeLock = readWriteLock.writeLock();

        writeLock.lock();
        try {
            stack.push(element);
            version++;
        } finally {
            writeLock.unlock();
        }
    }

    public Object pop() {
        Lock writeLock = readWriteLock.writeLock();

        writeLock.lock();
        try {
            Object element = stack.pop();
            version++;
            return element;
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try{
            return stack.size();
        } finally {
            readLock.unlock();
        }
    }

    public Iterator iterator() {
        return new SafeStackIterator(stack.iterator(), size(), version);
    }


    protected class SafeStackIterator implements Iterator {
        private final UnsafeStackIterator unsafeIterator;
        private final int size;
        private final int initialVersion;

        public SafeStackIterator(Iterator unsafeIterator, int size, int initialVersion) {
            this.unsafeIterator = (UnsafeStackIterator) unsafeIterator;
            this.size = size;
            this.initialVersion = initialVersion;
        }

        @Override
        public boolean hasNext() {
            Lock readLock = readWriteLock.readLock();
            readLock.lock();
            try {
                if (version != initialVersion) {
                    throw new ConcurrentModificationException("Data version has changed since iteration instantiation.");
                }

                return unsafeIterator.hasNext();
            } finally{
                readLock.unlock();
            }
        }

        @Override
        public Object next() {
            Lock readLock = readWriteLock.writeLock();
            readLock.lock();
            try {
                if (version != initialVersion) {
                    throw new ConcurrentModificationException("Data version has changed since iteration instantiation.");
                } else if (unsafeIterator.current >= size) { //index =0 is ok for size =1
                    throw new NoSuchElementException();
                }

                return unsafeIterator.next();
            } finally{
                readLock.unlock();
            }
        }
    }
}
