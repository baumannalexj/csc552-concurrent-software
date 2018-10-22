package ajeffrey.teaching.test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A class which tests Sun's JScrollPane class.
 * It creates two threads, both of which create a new scroll pane.
 * When run under Windows 9x, JDK1.3, it can cause a null pointer
 * exception (there's a race condition, so it only happens intermittently),
 * for example:
 * <pre>
 * java.lang.NullPointerException
 *      at javax.swing.plaf.metal.BumpBuffer.fillBumpBuffer(Unknown Source)
 *      at javax.swing.plaf.metal.BumpBuffer.&lt;init&gt;(Unknown Source)
 *      at javax.swing.plaf.metal.MetalBumps.createBuffer(Unknown Source)
 *      at javax.swing.plaf.metal.MetalBumps.setBumpColors(Unknown Source)
 *      at javax.swing.plaf.metal.MetalBumps.&lt;init&gt;(Unknown Source)
 *      at javax.swing.plaf.metal.MetalScrollBarUI.installDefaults(Unknown Source)
 *      at javax.swing.plaf.basic.BasicScrollBarUI.installUI(Unknown Source)
 *      at javax.swing.JComponent.setUI(Unknown Source)
 *      at javax.swing.JScrollBar.updateUI(Unknown Source)
 *      at javax.swing.JScrollBar.&lt;init&gt;(Unknown Source)
 *      at javax.swing.JScrollBar.&lt;init&gt;(Unknown Source)
 *      at javax.swing.JScrollPane$ScrollBar.&lt;init&gt;(Unknown Source)
 *      at javax.swing.JScrollPane.createVerticalScrollBar(Unknown Source)
 *      at javax.swing.JScrollPane.&lt;init&gt;(Unknown Source)
 *      at javax.swing.JScrollPane.&lt;init&gt;(Unknown Source)
 *      at ajeffrey.teaching.test.SimpleGUI.&lt;init&gt;(ajeffrey/teaching/test/TestJScrollPane.java:93)
 *      at ajeffrey.teaching.test.TestJScrollPane.run(ajeffrey/teaching/test/TestJScrollPane.java:82)
 *      at java.lang.Thread.run(Unknown Source)
 * </pre>
 * This violates the <i>single thread rule</i> of the Swing API,
 * described in
 * <a href="http://java.sun.com/products/jfc/tsc/articles/threads/threads1.html">Threads and Swing</a>:
 * <blockquote>
 *   Once a Swing component has been realized, all code that 
 *   might affect or depend on the state of that component should be 
 *   executed in the event-dispatching thread.
 * </blockquote>
 * In particular, the constructors for each Swing component are required to be thread-safe.
 * Unfortunately, this isn't true of <code>JScrollPane</code>, because it relies on 
 * <code>javax.swing.plaf.metal.MetalBumps</code>, which in turn uses a hidden 
 * <code>BumpBuffer</code> class, which uses the following method to initialize its 
 * <code>component</code> field to be a <code>Canvas</code> whose parent is <code>frame</code>:
 * <pre>
 *  protected void createComponent() {
 *      if (frame == null) {
 *          frame = new Frame( "bufferCreator" );
 *      }
 *      if (component == null ) {
 *          component = new Canvas();
 *          frame.add( component, BorderLayout.CENTER );
 *      }
 *      // fix for 4185993 (moved this outside if block)
 *       frame.addNotify();
 *  }
 * </pre>
 * Unfortunately, this has a race condition... First thread A executes createComponent(),
 * and sets <code>frame = new Frame(...)</code> and <code>component = new Canvas ()</code>.
 * Then before thread A gets a chance to call <code>frame.add (...)</code>, thread B
 * calls createComponent, finds that neither <code>frame</code> nor <code>component</code>
 * are null, and so returns, without having properly initialized <code>component</code>
 * (it still doesn't have a parent, so calling <code>createImage</code> on it causes a
 * <code>NullPointerException</code>.
 * 
 * @author <a href="http://fpl.cs.depaul.edu/ajeffrey">Alan Jeffrey</a>
 * @version 1.0.0, 30 January 2001
 */
public class TestJScrollPane implements Runnable {

    public static void main (String[] args) {
        // Create two simple GUIs in two different threads, and show them.
        Runnable task = new TestJScrollPane ();
        Thread threadA = new Thread (task);
        Thread threadB = new Thread (task);
        threadA.start ();
        threadB.start ();
    }

    public void run () { new SimpleGUI ().show (); }

}

class SimpleGUI implements Runnable {

    // Build a simple GUI
    protected final JFrame mainFrame = new JFrame ();
    protected final JTextArea textArea = new JTextArea (25, 40);

    // This constructor is not thread-safe:
    protected final JScrollPane textPane = new JScrollPane (textArea);

    public SimpleGUI () {
        // A very simple GUI indeed.
    	  mainFrame.getContentPane ().add (textPane);
        mainFrame.setTitle ("Testing JScrollPane for thread safety");
    }

    public void show () {
        // To be extra goody-goody, show () makes sure the Swing event handler
        // thread calls pack() and setVisible(true).
        javax.swing.SwingUtilities.invokeLater (this);
    }

    public void run () {
        // This should only be called by the event handler thread.
        mainFrame.pack ();
        mainFrame.setVisible (true);
    }

}
