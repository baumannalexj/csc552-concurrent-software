package ajeffrey.teaching.jack;

import java.io.PrintWriter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;

import java.awt.BorderLayout;

import ajeffrey.teaching.io.TextAreaIO;
import ajeffrey.teaching.debug.Debug;

/**
 * A GUI for the Jack application.
 * @version 1.0.1
 * @author Alan Jeffrey
 * @see GUIFactory
 */
public interface GUI {

    /**
     * Start the GUI.
     */
    public void start ();

    /**
     * A factory for building GUIs.
     */
    public static GUIFactory factory = new GUIFactoryImpl ();

}

class GUIFactoryImpl implements GUIFactory {
    
    public GUI build (final LogicFactory logicFactory) { 
	return new GUIImpl (logicFactory); 
    }

}

class GUIImpl implements GUI {

    // Get round a bug with final fields in JDK1.2
    // protected final Logic logic;
    // protected final LogicFactory logicFactory;
    protected Logic logic;
    protected LogicFactory logicFactory;

    protected final TextArea textArea = new TextArea ();
    protected final Button suspendButton = new Button ("Suspend");
    protected final Button resumeButton = new Button ("Resume");
    protected final Button newButton = new Button ("New");
    protected final Button quitButton = new Button ("Quit");
    protected final Panel buttonPanel = new Panel ();
    protected final Frame mainFrame = new Frame ();

    protected final ActionListener suspendActionListener =
	new ActionListener () { public void actionPerformed (ActionEvent ev) {
	    Debug.out.println ("suspendActionListener: Starting");
	    logic.suspend ();
	    Debug.out.println ("suspendActionListener: Returning");
	} };

    protected final ActionListener resumeActionListener =
	new ActionListener () { public void actionPerformed (ActionEvent ev) {
	    Debug.out.println ("resumeActionListener: Starting");
	    logic.resume ();
	    Debug.out.println ("resumeActionListener: Returning");
	} };

    protected final ActionListener newActionListener =
	new ActionListener () { public void actionPerformed (ActionEvent ev) {
	    Debug.out.println ("newActionListener: Starting");
	    Debug.out.println ("newActionListener: Creating...");
	    final GUI newGUI = new GUIImpl (logicFactory);
	    Debug.out.println ("newActionListener: Starting...");
	    newGUI.start ();
	    Debug.out.println ("newActionListener: Returning");
	} };

    protected final ActionListener quitActionListener =
	new ActionListener () { public void actionPerformed (ActionEvent ev) {
	    Debug.out.println ("quitActionListener: Exiting!");
	    System.exit (0);
	} };

    protected final PrintWriter textAreaPrintWriter
	= TextAreaIO.singleton.buildPrintWriter (textArea);

    GUIImpl (final LogicFactory logicFactory) {
	this.logicFactory = logicFactory;
	this.logic = logicFactory.build ();
	logic.setPrintWriter (textAreaPrintWriter);
	Debug.out.println ("GUIImpl: Built");
    }

    public void start () {
	Debug.out.println ("GUIImpl.start: Starting");
	Debug.out.println ("GUIImpl.start: Customizing components");
	mainFrame.setSize (300,300);
	suspendButton.addActionListener (suspendActionListener);
	resumeButton.addActionListener (resumeActionListener);
	newButton.addActionListener (newActionListener);
	quitButton.addActionListener (quitActionListener);
	Debug.out.println ("GUIImpl.start: Adding components");
	buttonPanel.add (suspendButton);
	buttonPanel.add (resumeButton);
	buttonPanel.add (newButton);
	buttonPanel.add (quitButton);
	mainFrame.add (textArea);
	mainFrame.add (BorderLayout.SOUTH, buttonPanel);
	Debug.out.println ("GUIImpl.start: Showing frame");
	mainFrame.setVisible (true);
	Debug.out.println ("GUIImpl.start: Starting business logic");
	logic.start ();
	Debug.out.println ("GUIImpl.start: Returning");
    }

}
