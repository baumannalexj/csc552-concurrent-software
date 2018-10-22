package ajeffrey.teaching.minisoap;

import java.lang.reflect.Field;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;

import ajeffrey.teaching.debug.Debug;

import com.macfaq.io.SafePrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.WeakHashMap;

public interface SoapWriter {
   
    public void serialize (Object obj) throws IOException;
    public void flush () throws IOException;
    public void close () throws IOException;

    public static SoapWriterFactory factory = new SoapWriterFactoryImpl ();

}

class SoapWriterFactoryImpl implements SoapWriterFactory {

    public SoapWriter build (final OutputStream out) {
	try {
	    return build (new OutputStreamWriter (out, "UTF-8"));
	} catch (final UnsupportedEncodingException ex) {
	    throw new SoapException ("This shouldn't happen " + ex);
	}
    }

    public SoapWriter build (final Writer out) {
	return build (new SafePrintWriter (out, "\n"));
    }

    public SoapWriter build (final SafePrintWriter out) {
	return new SoapWriterImpl (out);
    }

}

class SoapWriterImpl implements SoapWriter {

    protected final SafePrintWriter out;
    // NEW:
    protected final WeakHashMap cache = new WeakHashMap ();
    protected int numHandles = 0;
    // END NEW.

    protected SoapWriterImpl (final SafePrintWriter out) {
	this.out = out;
	Debug.out.println ("SoapWriter: Built");
    }

    public void flush () throws IOException {
	out.flush ();
    }

    public void close () throws IOException {
	out.close ();
    }

    public void serialize (final Object obj) throws IOException {
	// Debug.out.println ("SoapWriter.serialize: Starting");
	final Class objClass = obj.getClass ();
	// Debug.out.println ("SoapWriter.serialize: objClass = " + objClass);
	final Field[] objFields = objClass.getFields ();
	// NEW:
	String id = (String)(cache.get (obj));
	// Debug.out.println ("SoapWriter.serialize: id = " + id);
	if (id != null) {
	    out.println ("<Handle href=\"#" + id + "\"/>");
	    // Debug.out.println ("SoapWriter.serialize: Sent handle");
	    return;
	} else {
	    id = "handle" + (++numHandles);
	    cache.put (obj, id);
	    // Debug.out.println ("SoapWriter.serialize: Added " + id + " to cache");
	}
	// END NEW.
	try {
	    if (objClass.isArray ()) {
		// Debug.out.println ("SoapWriter.serialize: array " + obj);
		Class type = objClass.getComponentType ();
		int length = Array.getLength (obj);
		out.println ("<Array class=\"" + type.getName () + "\" length=\"" + length + "\" id=\"" + id + "\">");
		for (int i=0; i<length; i++) {
		    serialize (type, Array.get (obj, i));
		}
		out.println ("</Array>");
	    } else {
		// MODIFIED TO SEND handle:
		out.println ("<Object class=\"" + objClass.getName () + "\" id=\"" + id + "\">");
		// REST IS UNCHANGED
		for (int i=0; i < objFields.length; i++) {
		    // Debug.out.println ("SoapWriter.serialize: field[" + i + "] = " + objFields[i]);
		    if (!Modifier.isStatic (objFields[i].getModifiers ())) {
			final Class fieldType = objFields[i].getType ();
			final Object fieldContents = objFields[i].get (obj);
			serialize (fieldType, fieldContents);
		    }
		}
		out.println ("</Object>");
	    }
	} catch (final IllegalAccessException ex) {
	    // Debug.out.println ("SoapWriter.serialize: Caught " + ex);
	    throw new SoapException ("Caught " + ex);	    
	}
	// Debug.out.println ("SoapWriter.serialize: Returning");
    }

    void serialize (Class type, Object obj) throws IOException {
	if (type.isPrimitive ()) {
	    // Debug.out.println ("SoapWriter.serialize: primitive " + object);
	    out.println ("<" + type + " value=\"" + obj + "\"/>");
	} else if (obj == null) {
	    out.println ("<Null/>");
	}  else if (obj instanceof String) {
	    // Debug.out.println ("SoapWriter.serialize: string " + object);
	    out.println ("<String value=\"" + obj + "\"/>");
	} else {
	    // Debug.out.println ("SoapWriter.serialize: recursive call");
	    serialize (obj);
	}
    }

}
