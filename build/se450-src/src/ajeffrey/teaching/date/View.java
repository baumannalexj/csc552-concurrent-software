package ajeffrey.teaching.date;

import ajeffrey.teaching.debug.Debug;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public interface View {

    public void start ();

    public static ViewFactory factory = new ViewFactoryImpl ();

}

class ViewFactoryImpl implements ViewFactory {

    public View build (Model model) { return new ViewImpl (model); }

}

class ViewImpl implements View {

    final Model model;

    final JFrame frame = new JFrame ("Date");

    final JLabel connectLabel = new JLabel ("Date Server:");
    final JTextField connectField = new JTextField ();
    final JButton connectButton = new JButton ("Connect");

    final JLabel dateLabel = new JLabel ("--- --- -- --:--:-- --- ----", JLabel.CENTER);

    final JButton dateButton = new JButton ("Refresh");
    final JButton quitButton = new JButton ("Quit");

    final ActionListener connectListener = new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		try {
		    Debug.out.println ("Connect button pressed");
		    model.connect (connectField.getText ());
		    Debug.out.println ("Connect button finished");
		    JOptionPane.showMessageDialog (frame, "Connection Succeeded");
		} catch (Exception ex) {
		    JOptionPane.showMessageDialog (frame, ex.toString (), "Connection Failed", JOptionPane.ERROR_MESSAGE);
		}
	    }
	};

    final ActionListener dateListener = new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		try {
		    Debug.out.println ("Date button pressed");
		    dateLabel.setText (model.getDate ().toString ());
		    Debug.out.println ("Date button finished");
		} catch (Exception ex) {
		    JOptionPane.showMessageDialog (frame, ex.toString (), "Refresh Failed", JOptionPane.ERROR_MESSAGE);
		}
	    }
	};

    final ActionListener quitListener = new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		Debug.out.println ("Quit button pressed");
		System.exit (0);
	    }
	};

    ViewImpl (Model model) { this.model = model; }

    public void start () {
	connectButton.addActionListener (connectListener);
	dateButton.addActionListener (dateListener);
	quitButton.addActionListener (quitListener);
	final JPanel connectPanel = new JPanel (new BorderLayout ());
	final JPanel buttonPanel = new JPanel (new BorderLayout ());
	connectPanel.add ("West", connectLabel);
	connectPanel.add ("Center", connectField);
	connectPanel.add ("East", connectButton);
	buttonPanel.add ("West", dateButton);
	buttonPanel.add ("East", quitButton);
	frame.getContentPane ().add ("North", connectPanel);
	frame.getContentPane ().add ("Center", dateLabel);
	frame.getContentPane ().add ("South", buttonPanel);
	frame.setSize (300,100);
	frame.setVisible (true); 
    }

}
