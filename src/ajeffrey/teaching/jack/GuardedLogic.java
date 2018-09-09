package ajeffrey.teaching.jack;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.io.DevNull;
import ajeffrey.teaching.util.guard.Guard;

import java.io.PrintWriter;

/**
 * A multi-threaded implementation of the business logic for the Jack
 * application.  This uses a separate thread for each window, and
 * uses a guard to handle suspend/resume.  This is how I would recommend
 * implementing this application!
 *
 * @author Alan Jeffrey
 * @version 1.0.1
 */

/**
 *
 * CSC 552 - fall 2018
 * Alexander Baumann
 * HW 1
 *
 * Added global should-run-flag, all running threads use same flag instead of each thread
 * managing it's own flag.
 *
 * changes are marked in comments as // hw1
 *
 * */


public interface GuardedLogic {
    public static LogicFactory factory = new GuardedLogicFactoryImpl();
}

class GuardedLogicFactoryImpl implements LogicFactory {
    protected Guard globalShouldRun = Guard.factory.build(true); //hw 1

    public Logic build() {
        return new GuardedLogicImpl(globalShouldRun); //hw1
    }

}

class GuardedLogicImpl implements Logic, Runnable {

    protected final Thread thread = new Thread(this);
    protected final String message = "\nAll work and no play makes Jack a dull boy.";
    protected PrintWriter out = DevNull.printWriter;
    //    protected Guard flag = Guard.factory.build(true); //  hw1
    protected Guard flag;

    protected int offset = 0;

    public GuardedLogicImpl(Guard flag) { //hw 1
        this.flag = flag;
    }

    protected void printChar() {
        offset = (offset + 1) % (message.length());
        char c = message.charAt(offset);
        Debug.out.println("GuardedLogic.printChar (): Printing " + c);
        out.print(c);
        out.flush();
    }

    public void setPrintWriter(final PrintWriter out) {
        Debug.out.println("GuardedLogic.setPrintWriter (): Starting");
        this.out = out;
        Debug.out.println("GuardedLogic.setPrintWriter (): Returning");
    }

    public synchronized void suspend() {
        Debug.out.println("GuardedLogic.suspend (): Starting");
        flag.setValue(false);
        Debug.out.println("GuardedLogic.suspend (): Returning");
    }

    public void resume() {
        Debug.out.println("GuardedLogic.resume (): Starting");
        flag.setValue(true);
        Debug.out.println("GuardedLogic.resume (): Returning");
    }

    public void run() {
        Debug.out.println("GuardedLogic.run (): Starting");
        try {
            while (true) {
                Thread.sleep(200);
                flag.waitForTrue();
                printChar();
            }
        } catch (InterruptedException ex) {
            Debug.out.println
                    ("GuardedLogic.run (): Caught exception " + ex);
        }
        Debug.out.println("GuardedLogic.run (): Returning");
    }

    public void start() {
        Debug.out.println("GuardedLogic.start (): Starting");
        thread.start();
        Debug.out.println("GuardedLogic.start (): Returning");
    }

}
