package ajeffrey.teaching.test;

import ajeffrey.teaching.util.stack.UnsafeStack;
import java.util.Iterator;

public class TestStack {

    public static void main (String[] args) {

	final UnsafeStack stack = UnsafeStack.factory.build ();
	stack.push ("fred"); stack.push ("wilma"); stack.push ("barney"); stack.push ("betty");
	for (Iterator i = stack.iterator (); i.hasNext ();) {
	    System.out.println (i.next ());
	}
	
    }

}
