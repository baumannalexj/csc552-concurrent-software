package ajeffrey.teaching.util.stack;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SafeStackImpl {

    final UnsafeStack stack = UnsafeStack.factory.build();


    final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public UnsafeStack iterate() {
        readWriteLock.readLock().lock();

        try {


        } finally {
            readWriteLock.readLock().unlock();
        }

    }
}

