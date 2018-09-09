package ajeffrey.teaching.banking.client.logic;

import ajeffrey.teaching.observer.Subject;
import ajeffrey.teaching.observer.Observer;
import ajeffrey.teaching.debug.Debug;

/**
 * The business logic for the banking client.
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public interface Logic {

    /**
     * A factory for building new business logic objects.
     */
    public static final LogicFactory factory = new LogicFactoryImpl ();

    /**
     * A Subject for the business logic, suitable for using with the
     * observer pattern.  Whenever the logic changes state, this subject
     * will update all its observers.
     * @return the Subject for this logic object.
     */
    public Subject getSubject ();

    /**
     * The current total credits to the account
     * @return the total credits to the account
     */
    public int getCredits ();

    /**
     * The current total debits to the account
     * @return the total debits to the account
     */
    public int getDebits ();

    /**
     * The current total balance of the account
     * @return the total balance of the account
     */
    public int getBalance ();

    /**
     * Make a credit of the given amount.
     * @param amount the amount to credit
     */
    public void credit (int amount);

    /**
     * Make a debit of the given amount.
     * @param amount the amount to debit
     * @exception OverdrawnException thrown if the amount is 
     *   greater than the current balance
     */
    public void debit (int amount) throws OverdrawnException;

    /**
     * Make a credit of the given amount.
     * @param amount the amount to credit
     * @exception NumberFormatException thrown if the amount is 
     *   not a positive number
     */
    public void credit (String amount) throws NumberFormatException;

    /**
     * Make a debit of the given amount.
     * @param amount the amount to debit
     * @exception NumberFormatException thrown if the amount is 
     *  not a positive number
     * @exception OverdrawnException thrown if the amount is 
     *  greater than the current balance
     */
    public void debit (String amount) throws OverdrawnException, NumberFormatException;

}

class LogicFactoryImpl implements LogicFactory {

    public Logic build () { return new LogicImpl (); }

}

class LogicImpl implements Logic {

    protected final Subject subject = Subject.factory.build ();

    protected int debits = 0;
    protected int credits = 0;
    protected int balance = 0;

    public Subject getSubject () { return subject; }
    public int getDebits () { return debits; }
    public int getCredits () { return credits; }
    public int getBalance () { return balance; }

    public void credit (final String amountString)
    throws NumberFormatException {
	Debug.out.println 
	    ("Logic.credit: Starting");
	Debug.out.println 
	    ("Logic.credit: Calling parseInt (" + amountString + ")");
	final int amount = Integer.parseInt (amountString);
	if (amount < 0) { 
	    Debug.out.println ("Logic.credit: Negative amount");
	    throw new NumberFormatException () ;
	}
	Debug.out.println ("Logic.credit: Calling credit (" + amount + ")");
	credit (amount);
	Debug.out.println ("Logic.credit: Returning");
    }

    public void debit (final String amountString)
    throws NumberFormatException, OverdrawnException {
	Debug.out.println 
	    ("Logic.debit: Starting");
 	Debug.out.println 
	    ("Logic.debit: Calling parseInt (" + amountString + ")");
	final int amount = Integer.parseInt (amountString);
	if (amount < 0) { 
	    Debug.out.println ("Logic.debit: Negative amount");
	    throw new NumberFormatException () ;
	}
	Debug.out.println ("Logic.debit: Calling debit (" + amount + ")");
	debit (amount);
	Debug.out.println ("Logic.debit: Returning");
    }

    public void credit (final int amount) {
	Debug.out.println ("Logic.credit: Starting");
	Debug.out.println ("Logic.credit: State was: " + this);
	credits = credits + amount;
	balance = balance + amount;
	Debug.out.println ("Logic.credit: State is now: " + this);
	subject.updateObservers ();
	Debug.out.println ("Logic.credit: Returning");
    }

    public void debit (final int amount) 
    throws OverdrawnException {
	Debug.out.println ("Logic.debit: Starting");
	if (amount > balance) { 
	    Debug.out.println 
		("Logic.debit: Tried to overdraw: amount = " +
		 amount + ". balance = " + balance);
	    throw new OverdrawnException (); 
	}
	Debug.out.println ("Logic.debit: State was: " + this);
	debits = debits + amount;
	balance = balance - amount;
	Debug.out.println ("Logic.debit: State is now: " + this);
	Debug.out.println ("Logic.debit: calling subject.updateObservers ()");
	subject.updateObservers ();
	Debug.out.println ("Logic.debit: Returning");
    }

    public String toString () { 
	return "Logic { debits = " + debits +
	    ", credits = " + credits + 
	    ", balance = " + balance + " }"; 
    }

}
