package ajeffrey.teaching.hash.client;

import java.awt.Button;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.Serializable;

import ajeffrey.teaching.hash.iface.Hash;

public interface GUI {

    public void start ();

    public static final GUIFactory factory = new GUIFactoryImpl ();

}

class GUIFactoryImpl implements GUIFactory {

    public GUI build (final Hash hashTable) {
	return new GUIImpl (hashTable);
    }

}

class GUIImpl extends Frame implements GUI {

  final Hash hashTable;

  protected GUIImpl (final Hash hashTable) {
    super ("HashClient");
    this.hashTable = hashTable;
  }

  protected void putInHashTable (final String key, final String value) {
    try {
      hashTable.put (key,value);
    } catch (Exception e) {
      e.printStackTrace ();
    }
  }

  protected String getFromHashTable (final String key) {
    try {
      final Serializable result = hashTable.get (key);
      if (result == null) {
	return "<no value>";
      } else {
	return (String)result;
      }
    } catch (Exception e) {
      e.printStackTrace ();
      return "<exception>";
    }
  }

  public void start () {
    setLayout (new FlowLayout (FlowLayout.CENTER));
    final TextField keyField = new TextField (10);
    final TextField valueField = new TextField (10);
    final Button put = new Button ("Put");
    final Button get = new Button ("Get");
    final Button quit = new Button ("Quit");
    put.addActionListener (new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        Cursor current = getCursor ();
        setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));
	putInHashTable (keyField.getText (), valueField.getText ());
        setCursor (current);
      }
    });
    get.addActionListener (new ActionListener () {
      public void actionPerformed (ActionEvent e) {
        final Cursor current = getCursor ();
        setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));
	valueField.setText (getFromHashTable (keyField.getText ()));
        setCursor (current);
      }
    });
    quit.addActionListener (new ActionListener () {
      public void actionPerformed (ActionEvent e) {
	System.exit (0);
      }
    });
    add (keyField);
    add (valueField);
    add (put);
    add (get);
    add (quit);
    setSize (400,60);
    show ();
  }

}
