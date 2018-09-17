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
 * @author Alan Jeffrey
 * @version 1.0.1
 *
 *
 * last edit:
 *
 * Alexander Baumann
 * 9.16.18
 */
public interface OrderedPhilosopher {

    /**
     * A factory for building deadlocking philosophers.
     */
    public static final PhilosopherFactory factory
            = new OrderedPhilosopherFactoryImpl();

}

class OrderedPhilosopherFactoryImpl implements PhilosopherFactory {

    public Philosopher build(final Comparable lhFork,
                             final Comparable rhFork,
                             final String name) {

        return new OrderedPhilosopherImpl(lhFork, rhFork, name);
    }

}

class OrderedPhilosopherImpl implements Runnable, Philosopher {

    final protected Comparable lhFork;
    final protected Comparable rhFork;
    final protected String name;
    final protected Thread thread;

    protected OrderedPhilosopherImpl(final Comparable lhFork,
                                     final Comparable rhFork,
                                     final String name) {
        this.lhFork = lhFork;
        this.rhFork = rhFork;
        this.name = name;
        this.thread = new Thread(this);
    }

    @Override
    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        Debug.out.breakPoint(name + " is starting");
        try {
            Comparable firstForkToGrab;
            Comparable secondForkToGrab;

            while (true) {
                Debug.out.println(name + " is thinking");
                delay();

                //going around the table, we can avoid deadlock
                // if the last philosopher attempts to grab the right fork before their left fork

                if (lhFork.compareTo(rhFork) > 0) { // "Fork 1".compareTo("Fork 4) == 3; (> 0) is the last one
                    firstForkToGrab = rhFork;
                    secondForkToGrab = lhFork;
                } else {
                    firstForkToGrab = lhFork;
                    secondForkToGrab = rhFork;
                }

                attemptForkGrabbing(firstForkToGrab, secondForkToGrab);
            }
        } catch (final InterruptedException ex) {
            Debug.out.println(name + " is interrupted");
        }
    }

    private void attemptForkGrabbing(Comparable firstForkToGrab, Comparable secondForkToGrab) throws InterruptedException {
        Debug.out.println(name + " tries to pick up " + firstForkToGrab);

        synchronized (firstForkToGrab) {
            Debug.out.println(name + " picked up " + firstForkToGrab);
            delay();
            Debug.out.println(name + " tries to pick up " + secondForkToGrab);
            synchronized (secondForkToGrab) {
                Debug.out.println(name + " picked up " + secondForkToGrab);
                Debug.out.println(name + " starts eating");
                delay();
                Debug.out.println(name + " finishes eating");
            }
        }
    }

    protected void delay() throws InterruptedException {
        Thread.currentThread().sleep((long) (1000 * Math.random()));
    }

}
