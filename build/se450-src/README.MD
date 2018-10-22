This homework is to provide a fixed version of the Dining Philosophers problem, as discussed in class.

Compile and run ajeffrey.teaching.test.TestPhilosopher.

0. You should be able to get this class to deadlock.  Show a trace that makes the code deadlock.

1. Implement a resource ordering solution to the problem.

For this solution, you should save a copy of DeadlockingPhilosopher.java as OrderedPhilosopher.java, and edit it. You should make sure that the forks are picked up in order. You should do this by using Comparable objects rather than Object for the locks, then using the compareTo method to compare the locks for order. This ensures there will be no deadlock, as discussed in class and in Lea 2.2.6).

Submit your homework as before.
