package ajeffrey.teaching.test;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.debug.StepDebugStream;

import ajeffrey.teaching.dining.Philosopher;
import ajeffrey.teaching.dining.TokenPhilosopher;

import java.util.concurrent.Semaphore;

/**
 * A test of the dining philosophers, with four philosophers.
 *
 * @author Alan Jeffrey
 * @version 1.0.1
 * @see Philosopher
 *
 *
 *
 * last edit:
 *
 * Alexander Baumann
 * 9.16.18
 */
public class TestPhilosopher {


	public static void main(String[] args) throws Exception {
		Debug.out.addFile("Dining.txt");
		// Switch on step debugging
		Debug.out.addFactory(StepDebugStream.factory);

		// Switch on step debugging
		// Debug.out.addFactory (StepDebugStream.factory);
		// Send debugging to stderr
		// Debug.out.addPrintStream (System.err);
		// Create the forks
		final Comparable fork1 = "Fork 1";
		final Comparable fork2 = "Fork 2";
		final Comparable fork3 = "Fork 3";
		final Comparable fork4 = "Fork 4";
		// Which philosopher factory to use: you may want to edit this!
//		final PhilosopherFactory factory = DeadlockingPhilosopher.factory;
//		final PhilosopherFactory factory = OrderedPhilosopher.factory;
		final TokenPhilosopher.TokenPhilosopherFactoryImpl factory = (TokenPhilosopher.TokenPhilosopherFactoryImpl) TokenPhilosopher.factory;

		int numberOfForks = 4;
		Semaphore semaphore = new Semaphore(numberOfForks - 1);

		factory.setDinerTokenSemaphore(semaphore);

		// Create the philosophers
		final Philosopher fred = factory.build(fork1, fork2, "Fred", semaphore);
		final Philosopher wilma = factory.build(fork2, fork3, "Wilma", semaphore);
		final Philosopher barney = factory.build(fork3, fork4, "Barney", semaphore);
		final Philosopher betty = factory.build(fork4, fork1, "Betty", semaphore);

		// Start the philosophers
		fred.start();
		wilma.start();
		barney.start();
		betty.start();
	}

}
