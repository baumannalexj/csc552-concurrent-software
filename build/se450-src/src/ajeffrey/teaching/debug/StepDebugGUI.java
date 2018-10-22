package ajeffrey.teaching.debug;

import ajeffrey.teaching.io.JTextAreaIO;
import ajeffrey.teaching.observer.Observer;

import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * A GUI for step debugging.
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public interface StepDebugGUI extends Observer {

    /**
     * Start the GUI.
     */
    public void start ();

    /**
     * Stop the GUI.
     */
    public void stop ();

    /**
     * Get the output stream for writing to the GUI.
     * @return an output stream which prints to the GUI.
     */
    public PrintStream getPrintStream ();

    /**
     * A factory for building GUIs.
     */
    public static StepDebugGUIFactory factory = 
	new StepDebugGUIFactoryImpl ();

}

class StepDebugGUIFactoryImpl implements StepDebugGUIFactory {

    // It appears that the Win9x implementation of JScrollPane
    // is not thread safe, so we have to do some locking here.

    protected final Object lock = new Object ();
    
    public StepDebugGUI build (final StepDebugLogic logic) {
        synchronized (lock) {
            return new StepDebugGUIImpl (logic);
        }
    }

}

class StepDebugGUIImpl implements StepDebugGUI {

    // Get round a bug with final fields in JDK1.2
    // protected final StepDebugLogic logic;
    protected StepDebugLogic logic;

    protected final JFrame mainFrame = new JFrame ();
    protected final JPanel buttonPanel = new JPanel ();
    protected final JPanel bottomPanel = new JPanel ();
    protected final JTextArea textArea = new JTextArea (25, 40);
    protected final JScrollPane textPanel = new JScrollPane (textArea);
    protected final JButton runButton = new JButton ("Run");
    protected final JButton stepButton = new JButton ("Step");
    protected final JButton pauseButton = new JButton ("Pause");
    protected final JLabel statusLabel = new JLabel ("Initializing...");

    protected final PrintStream out = 
	JTextAreaIO.singleton.buildPrintStream (textArea);

    protected final ActionListener runActionListener =
	new ActionListener () { public void actionPerformed (ActionEvent ev) {
	    logic.runMode ();
	} };

    protected final ActionListener stepActionListener =
	new ActionListener () { public void actionPerformed (ActionEvent ev) {
	    logic.stepMode ();
	} };

    protected final ActionListener pauseActionListener =
	new ActionListener () { public void actionPerformed (ActionEvent ev) {
	    logic.pauseMode ();
	} };

    protected StepDebugGUIImpl (final StepDebugLogic logic) {
	this.logic = logic;
    }

    public void update () {
	statusLabel.setText (logic.getModeName ());
    }

    public PrintStream getPrintStream () {
	return out;
    }

    public void start () {
	update ();
	logic.attach (this);
	bottomPanel.setLayout (new BorderLayout ());
	buttonPanel.setLayout (new FlowLayout (FlowLayout.RIGHT));
	textArea.setEditable (false);
	textPanel.setHorizontalScrollBarPolicy 
	    (JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	textPanel.setVerticalScrollBarPolicy 
	    (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	runButton.addActionListener (runActionListener);
	stepButton.addActionListener (stepActionListener);
	pauseButton.addActionListener (pauseActionListener);
	buttonPanel.add (runButton);
	buttonPanel.add (stepButton);
	buttonPanel.add (pauseButton);
	bottomPanel.add (statusLabel);
	bottomPanel.add (BorderLayout.EAST, buttonPanel);
	mainFrame.getContentPane ().add (textPanel);
	mainFrame.getContentPane ().add (BorderLayout.SOUTH, bottomPanel);
	mainFrame.setTitle ("Debugging " + logic.getThreadName ());
	mainFrame.pack ();
	mainFrame.setVisible (true);
    }

    public void stop () {
	logic.detach (this);
	mainFrame.dispose ();
    }

}
