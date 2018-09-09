package ajeffrey.teaching.test;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.util.list.ImmutableList;
import ajeffrey.teaching.util.list.FlyweightImmutableList;

/**
 * A class which tests the flyweight list implementation.
 * @author Alan Jeffrey
 * @version 1.0.1
 * @see FlyweightImmutableList
 */
public class TestFlyweightList {

    public static void main (final String[] args) {
	// Switch on debugging
	Debug.out.addPrintStream (System.err);
	final ImmutableList empty = FlyweightImmutableList.empty;
	final ImmutableList foo = empty.cons ("Fred").cons ("Wilma");
	final ImmutableList bar = empty.cons ("Fred").cons ("Barney");
	System.out.println ("foo = " + foo);
	System.out.println ("bar = " + bar);
    }

}
