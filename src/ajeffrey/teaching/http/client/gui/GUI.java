package ajeffrey.teaching.http.client.gui;

import ajeffrey.teaching.http.client.logic.Logic;
import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.io.JTextAreaIO;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.io.PrintWriter;

/**
 * The GUI for a simple HTTP client.
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public interface GUI {

    /**
     * Start the GUI.
     */
    public void start ();

    /**
     * A factory for building GUIs
     */
    public static GUIFactory factory = new GUIFactoryImpl ();

}

class GUIFactoryImpl implements GUIFactory {

    public GUI build (final Logic logic) {
	return new GUIImpl (logic);
    }

}

class GUIImpl implements GUI {

    protected final JFrame mainFrame = new JFrame ("HTTP Client");
    protected final JPanel urlPanel = new JPanel ();
    protected final JPanel getStopPanel = new JPanel ();
    protected final JPanel topPanel = new JPanel ();
    protected final JPanel quitPanel = new JPanel ();
    protected final JButton quitButton = new JButton ("Quit");
    protected final JTextArea textArea = new JTextArea (25, 40);
    protected final JScrollPane textPanel = new JScrollPane (textArea);
    protected final JTextField urlField = new JTextField ();
    protected final JButton getButton = new JButton ("Get");
    protected final JButton stopButton = new JButton ("Stop");

    protected final ActionListener quitActionListener =
	new ActionListener () { public void actionPerformed (ActionEvent ev) {
	    Debug.out.println ("quitActionListener: Exiting!");
	    System.exit (0);
	} };

    protected final ActionListener getActionListener =
	new ActionListener () { public void actionPerformed (ActionEvent ev) {
	    Debug.out.println ("getActionListener: Starting");
	    get ();
	    Debug.out.println ("getActionListener: Returning");
	} };

    protected final ActionListener stopActionListener =
	new ActionListener () { public void actionPerformed (ActionEvent ev) {
	    Debug.out.println ("stopActionListener: Starting");
	    stop ();
	    Debug.out.println ("stopActionListener: Returning");
	} };

    protected final PrintWriter textAreaPrintWriter = 
	JTextAreaIO.singleton.buildPrintWriter (textArea);

    protected final Logic logic;

    protected GUIImpl (final Logic logic) {
	this.logic = logic;
	logic.setPrintWriter (textAreaPrintWriter);
	Debug.out.println ("GUIImpl: Built");
    }

    protected void get () {
	final String url = urlField.getText ();
	Debug.out.println ("GUIImpl.get: Starting.");
	Debug.out.println ("GUIImpl.get: Calling logic.stop ()");
	logic.stop ();
	Debug.out.println ("GUIImpl.get: Calling textArea.clear ()");
	textArea.setText ("");
	Debug.out.println ("GUIImpl.get: Calling logic.get (" + url + ")");
	logic.get (url);
	Debug.out.println ("GUIImpl.get: Returning.");
    }

    protected void stop () {
	Debug.out.println ("GUIImpl.stop: Starting.");
	logic.stop ();
	Debug.out.println ("GUIImpl.stop: Returning.");
    }

    public void start () {
	Debug.out.println ("GUIImpl.start: Starting.");
	Debug.out.println ("GUIImpl.start: Customizing components.");
	topPanel.setLayout (new BorderLayout ());
	urlPanel.setLayout (new BorderLayout ());
	quitPanel.setLayout (new FlowLayout (FlowLayout.RIGHT));
	textArea.setEditable (false);
	textPanel.setHorizontalScrollBarPolicy 
	    (JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	textPanel.setVerticalScrollBarPolicy 
	    (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	urlPanel.setBorder (BorderFactory.createEmptyBorder (5,5,5,5));
	quitButton.addActionListener (quitActionListener);
	getButton.addActionListener (getActionListener);
	stopButton.addActionListener (stopActionListener);
	urlField.addActionListener (getActionListener);
	Debug.out.println ("GUIImpl.start: Adding components.");
	getStopPanel.add (getButton);
	getStopPanel.add (stopButton);
	urlPanel.add (urlField);
	topPanel.add (urlPanel);
	topPanel.add (BorderLayout.EAST, getStopPanel);
	quitPanel.add (quitButton);
	mainFrame.getContentPane ().add 
	    (BorderLayout.WEST, Box.createHorizontalStrut (5));
	mainFrame.getContentPane ().add 
	    (BorderLayout.EAST, Box.createHorizontalStrut (5));
	mainFrame.getContentPane ().add (BorderLayout.NORTH, topPanel);
	mainFrame.getContentPane ().add (textPanel);
	mainFrame.getContentPane ().add (BorderLayout.SOUTH, quitPanel);
	Debug.out.println ("GUIImpl.start: Showing frame.");
	mainFrame.pack ();
	mainFrame.setVisible (true);
	Debug.out.println ("GUIImpl.start: Returning.");
    }

}
