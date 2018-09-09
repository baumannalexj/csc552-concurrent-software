package ajeffrey.teaching.banking.client.gui;

import ajeffrey.teaching.observer.Observer;
import ajeffrey.teaching.banking.client.logic.Logic;
import ajeffrey.teaching.banking.client.logic.OverdrawnException;
import ajeffrey.teaching.debug.Debug;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.awt.GridLayout;

/**
 * The graphical user interface for the banking client.
 * This is an instance of the Observer pattern from the 
 * `Gang of Four' book.  It acts as an observer of the 
 * business logic.  Whenever the business logic updates
 * itself, it should call the <code>update ()</code>
 * method of the GUI, so it can redraw itself.
 * @author Alan Jeffrey
 * @version 1.0.2
 * @see Logic
 */
public interface GUI extends Runnable, Observer {

    /**
     * A factory for building new GUIs.
     */
    public static final GUIFactory factory = new GUIFactoryImpl ();

    /**
     * When the GUI is run, it will display itself.
     */
    public void run ();
}

class GUIFactoryImpl implements GUIFactory {

    public GUI build (final Logic logic) { return new GUIImpl (logic); }

}

class GUIImpl implements GUI {

    // Get round a bug with final fields in JDK1.2
    // protected final Logic logic;
    protected Logic logic;

    protected final Frame frame = new Frame ("Banking Client");
    protected final Panel balancePanel = new Panel ();
    protected final Panel creditPanel = new Panel ();
    protected final Panel debitPanel = new Panel ();
    protected final Panel quitPanel = new Panel ();
    protected final Button quitButton = new Button ("Quit");
    protected final Label balanceLabel = new Label ();
    protected final Label creditsLabel = new Label ();
    protected final Label debitsLabel = new Label ();
    protected final Label debitLabel = new Label ("New debit: ");
    protected final Label creditLabel = new Label ("New credit: ");
    protected final TextField debitField = new TextField (20);
    protected final TextField creditField = new TextField (20);
    protected final Button debitButton = new Button ("Debit");
    protected final Button creditButton = new Button ("Credit");
 
    protected final WindowListener exitWindowListener = 
	new WindowAdapter () {
	    public void windowClosing (WindowEvent ev) {
		Debug.out.println ("exitWindowListener.windowClosing: Exiting");
		System.exit (0);
	    }
	};

    protected final ActionListener exitActionListener = 
	new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		Debug.out.println 
		    ("exitActionListener.actionPerformed: Exiting");
		System.exit (0);
	    }
	};

    protected final ActionListener creditActionListener = 
	new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		Debug.out.println 
		    ("creditActionListener.actionPerformed: Starting");
		final String creditString = creditField.getText ();
		try {
		    Debug.out.println 
			("creditActionListener.actionPerformed: calling " 
			 + logic + ".credit (" + creditString + ")");
		    logic.credit (creditString);
		} catch (NumberFormatException ex) {
		    Debug.out.println
			("creditActionListener.actionPerformed: " 
			 + "calling errorMessage");
		    errorMessage 
			("The amount entered: \"" + creditString 
			 + "\" does not appear to be a number");
		}
		Debug.out.println 
		    ("creditActionListener.actionPerformed: Returning");
	    }
	};

    protected final ActionListener debitActionListener = 
	new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		Debug.out.println 
		    ("debitActionListener.actionPerformed: Starting");
		final String debitString = debitField.getText ();
		try {
		    Debug.out.println 
			("debitActionListener.actionPerformed: calling " 
			 + logic + ".debit (" + debitString + ")");
		    logic.debit (debitString);
		} catch (OverdrawnException ex) {
		    Debug.out.println
			("debitActionListener.actionPerformed: " 
			 + "calling errorMessage");
		    errorMessage 
			("That debit would make you overdrawn.");
		} catch (NumberFormatException ex) {
		    Debug.out.println
			("debitActionListener.actionPerformed: " 
			 + "calling errorMessage");
		    errorMessage 
			("The amount entered: \"" + debitString 
			 + "\" does not appear to be a number");
		}
		Debug.out.println 
		    ("debitActionListener.actionPerformed: Returning");
	    }
	};

    protected void errorMessage (final String msg) {
	Debug.out.println ("GUI.errorMessage: Starting");
	Debug.out.println ("GUI.errorMessage: Message is " + msg);
	Debug.out.println ("GUI.errorMessage: Preparing components");
	final Dialog errorDialog = new Dialog (frame, "Error Message", true);
	final Button okButton = new Button ("OK");
	final Panel p1 = new Panel ();
	final Panel p2 = new Panel ();
	okButton.addActionListener (new ActionListener () {
		public void actionPerformed (ActionEvent ev) {
		    Debug.out.println ("GUI.errorMessage: Disposing");
		    errorDialog.dispose ();
		}
	    });
	p1.add (new Label (msg));
	p2.add (okButton);
	errorDialog.setLayout (new GridLayout (2,1));
	errorDialog.add (p1);
	errorDialog.add (p2);
	errorDialog.setSize (400,100);
	Debug.out.println ("GUI.errorMessage: Showing error message");
	errorDialog.show ();
	Debug.out.println ("GUI.errorMessage: Returning");
    }

    protected GUIImpl (final Logic logic) {
	this.logic = logic;
	Debug.out.println ("GUIImpl: Built");
    }

    public void run () {
	Debug.out.println ("GUIImpl.run: Starting.");
	Debug.out.println ("GUIImpl.run: Customizing components");
	frame.setLayout (new GridLayout (4,1));
	Debug.out.println ("GUIImpl.run: Adding listeners");
	frame.addWindowListener (exitWindowListener);
	quitButton.addActionListener (exitActionListener);
	debitButton.addActionListener (debitActionListener);
	creditButton.addActionListener (creditActionListener);
	Debug.out.println ("GUIImpl.run: Adding components");
	balancePanel.add (creditsLabel);
	balancePanel.add (debitsLabel);
	balancePanel.add (balanceLabel);
	debitPanel.add (debitLabel);
	debitPanel.add (debitField);
	debitPanel.add (debitButton);
	creditPanel.add (creditLabel);
	creditPanel.add (creditField);
	creditPanel.add (creditButton);
	quitPanel.add (quitButton);
	frame.add (balancePanel);
	frame.add (debitPanel);
	frame.add (creditPanel);
	frame.add (quitPanel);
	Debug.out.println ("GUIImpl.run: Calling update ()");
	update ();
	Debug.out.println ("GUIImpl.run: Displaying frame");
	frame.setSize (400,200);
	frame.setVisible (true);
	Debug.out.println ("GUIImpl.run: Returning.");
    }

    public void update () {
	Debug.out.println ("GUIImpl.update: Starting.");
	Debug.out.println ("GUIImpl.update: Getting values.");
	final int credits = logic.getCredits ();
	final int debits = logic.getDebits ();
	final int balance = logic.getBalance ();
	Debug.out.println ("GUIImpl.update: Updating fields.");
	creditsLabel.setText ("Total credits: $" + credits);
	debitsLabel.setText ("Total debits: $" + debits);
	balanceLabel.setText ("Total balance: $" + balance);
	Debug.out.println ("GUIImpl.update: Returning.");
    }

    public String toString () {
	return "GUI { logic=" + logic + " }";
    }

}
