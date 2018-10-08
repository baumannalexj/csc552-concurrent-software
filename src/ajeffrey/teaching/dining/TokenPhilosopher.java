package ajeffrey.teaching.dining;

import ajeffrey.teaching.debug.Debug;

/**
 * A philosopher from the dining philosophers problem.
 * A philosopher thinks, picks up their left-hand fork,
 * picks up their right-hand fork, then eats.
 * Unfortunately, putting a collection of philosophers in a circle
 * can produce deadlock, if they all pick up their lh forks before any
 * of them have a chance to pick up their rh forks.
 *
 * @author alex baumann
 */
public interface TokenPhilosopher {
    public static final PhilosopherFactory factory = new TokenPhilosopherFactoryImpl();

    public class TokenPhilosopherFactoryImpl implements PhilosopherFactory {

        private DinerTokenSemaphore semaphore = null;

        public Philosopher build(final Comparable lhFork,
                                 final Comparable rhFork,
                                 final String name) {

            return new TokenPhilosopherImpl(lhFork, rhFork, name, semaphore);
        }

        public Philosopher build(final Comparable lhFork,
                                 final Comparable rhFork,
                                 final String name,
                                 final DinerTokenSemaphore semaphore) {


            TokenPhilosopherImpl tokenPhilosopher = new TokenPhilosopherImpl(lhFork, rhFork, name, semaphore);
            return tokenPhilosopher;
        }

        public void setDinerTokenSemaphore(DinerTokenSemaphore semaphore) {
            if (semaphore == null) {
                this.semaphore = semaphore;
            } else {
                System.out.println("diner token semaphore already set");
            }
        }

        public DinerTokenSemaphore getSemaphore() {
            return semaphore;
        }
    }

}

class TokenPhilosopherImpl implements Runnable, Philosopher {

    final protected Object lhFork;
    final protected Object rhFork;
    final protected String name;
    final protected Thread thread;
    final DinerTokenSemaphore semaphore;

    protected TokenPhilosopherImpl
            (final Object lhFork, final Object rhFork, final String name, DinerTokenSemaphore semaphore) {
        this.lhFork = lhFork;
        this.rhFork = rhFork;
        this.name = name;
        this.semaphore = semaphore;
        this.thread = new Thread(this);
    }



    public void start() {
        thread.start();
    }

    public void run() {
        Debug.out.breakPoint(name + " is starting");


        try {
            while (true) {
                Debug.out.println(name + " is thinking");
                delay();
                Debug.out.println(name + " tries to get token");

                if (semaphore.acquire()) {
                    Debug.out.println(name + " tries to pick up " + lhFork);
                    synchronized (lhFork) {
                        Debug.out.println(name + " picked up " + lhFork);
                        delay();
                        Debug.out.println(name + " tries to pick up " + rhFork);
                        synchronized (rhFork) {
                            Debug.out.println(name + " picked up " + rhFork);
                            Debug.out.println(name + " starts eating");
                            delay();
                            Debug.out.println(name + " finishes eating");
                        }
                    }
                    semaphore.release();
                } else {
                    Debug.out.println(name + " didn't get a token");
                }
            }
        } catch (final InterruptedException ex) {
            Debug.out.println(name + " is interrupted");
        }
    }

    protected void delay() throws InterruptedException {
        Thread.currentThread().sleep((long) (1000 * Math.random()));
    }

}
