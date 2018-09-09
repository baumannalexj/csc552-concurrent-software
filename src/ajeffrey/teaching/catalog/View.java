package ajeffrey.teaching.catalog;

import ajeffrey.teaching.debug.Debug;

import java.awt.Frame;
import java.awt.Panel;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Button;
import java.awt.TextArea;
import java.awt.GridLayout;
import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public interface View {

    public void start ();
    public void refresh ();

    public static ViewFactory factory = new ViewFactoryImpl ();

}

class ViewFactoryImpl implements ViewFactory {

    public View build (Model model) { return new ViewImpl (model); }

}

class ViewImpl implements View {

    final Model model;

    final Frame frame = new Frame ("Catalog");

    final Label keywordLabel = new Label ("Keyword:");
    final TextField keywordField = new TextField ();
    final Label newBookLabel = new Label ("New Book:");
    final TextField newBookField = new TextField ();
    final Button newBookButton = new Button ("Add Book");
    final Button searchButton = new Button ("New Search");
    final Button refineButton = new Button ("Refine Search");
    final Button showButton = new Button ("Show Results");
    final Button quitButton = new Button ("Quit");
    final TextArea resultsArea = new TextArea ();

    final ActionListener quitListener = new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		Debug.out.println ("Quit button pressed");
		System.exit (0);
	    }
	};

    final ActionListener searchListener = new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		Debug.out.println ("Search button pressed");
		model.search (keywordField.getText ());
		refresh ();
	    }
	};

    final ActionListener refineListener = new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		Debug.out.println ("Refine button pressed");
		model.refine (keywordField.getText ());
		refresh ();
	    }
	};

    final ActionListener showListener = new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		Debug.out.println ("Show button pressed");
		model.findTitles ();
		refresh ();
	    }
	};

    final ActionListener newBookListener = new ActionListener () {
	    public void actionPerformed (ActionEvent ev) {
		Debug.out.println ("NewBook button pressed");
		model.addBook (newBookField.getText ());
		refresh ();
	    }
	};

    ViewImpl (Model model) { this.model = model; }

    public void start () {
	searchButton.addActionListener (searchListener);
	refineButton.addActionListener (refineListener);
	showButton.addActionListener (showListener);
	quitButton.addActionListener (quitListener);
	newBookButton.addActionListener (newBookListener);
	final Panel keywordPanel = new Panel (new BorderLayout ());
	final Panel buttonPanel = new Panel (new GridLayout (1,4));
	final Panel toolPanel = new Panel (new GridLayout (2,1));
	final Panel newBookPanel = new Panel (new BorderLayout ());
	keywordPanel.add ("West", keywordLabel);
	keywordPanel.add ("Center", keywordField);
	buttonPanel.add (searchButton);
	buttonPanel.add (refineButton);
	buttonPanel.add (showButton);
	buttonPanel.add (quitButton);
	toolPanel.add (keywordPanel);
	toolPanel.add (buttonPanel);
	newBookPanel.add ("West", newBookLabel);
	newBookPanel.add ("Center", newBookField);
	newBookPanel.add ("East", newBookButton);
	refresh ();
	frame.add ("North", toolPanel);
	frame.add ("Center", resultsArea);
	frame.add ("South", newBookPanel);
	frame.setSize (500,300);
	frame.show (); 
    }

    public void refresh () {
	final int numTitles = model.numTitles ();
	resultsArea.setEditable (false);
	resultsArea.setText ("");
	resultsArea.append ("Number of results: " + numTitles);
	refineButton.setEnabled (numTitles > 0);
	showButton.setEnabled (numTitles > 0);
	if (model.hasTitles ()) {
	    Debug.out.println ("Displaying results");
	    final String[] results = model.getTitles ();
	    for (int i=0; i<results.length; i++) {
		resultsArea.append ("\n");
		resultsArea.append (results[i]);
	    }
	}
    }

}
