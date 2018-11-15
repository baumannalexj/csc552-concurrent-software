package inclass;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

class SerialExecutor implements Executor {
    final Queue<Runnable> tasks = new ArrayDeque<>(); // instead of guarding, we could replace with concurrent deque
    final Executor executor;
    Runnable active;

    SerialExecutor(Executor executor) {
        this.executor = executor;
    }

    public synchronized void execute(final Runnable r) {
        //needs to be synchronized since tasks is accessed twice
        // once here, once below
        // we need to guard "tasks" with two syncrhonized methods

        //TODO we don't have to worry about guarding if we use a concurrent safe task --> ConcurrentLinkQueue


        tasks.offer(new Runnable() {
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });

        if (active == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            executor.execute(active);
        }
    }
}