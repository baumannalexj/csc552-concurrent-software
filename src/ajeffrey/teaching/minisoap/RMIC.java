package ajeffrey.teaching.minisoap;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.lang.reflect.Method;

public class RMIC {

    public static void main (String[] args) throws Exception {
	for (int i=0; i<args.length; i++) {
	    Class contract = Class.forName (args[i]);
	    String fileName = args[i].replace ('.','/');
	    PrintWriter stubOut = new PrintWriter (new FileOutputStream (fileName + "STUB.java"));
	    generateStub (contract, stubOut);
	    stubOut.close ();
	}
    }

    static void generateStub (Class contract, PrintWriter out) {
	String fullName = contract.getName ();
	Method[] methods = contract.getDeclaredMethods ();
	String name = fullName.substring (fullName.lastIndexOf ('.') + 1);
	out.println (contract.getPackage () + ";");
	out.println ("import ajeffrey.teaching.minisoap.Host;");
	out.println ("import ajeffrey.teaching.minisoap.Stub;");
	out.println ("import ajeffrey.teaching.minisoap.RMI;");
	out.println ("public class " + name + "STUB implements Stub, " + name + " {");
	out.println ("  public Host host = Host.localhost;");
	out.println ("  public String objectId = Stub.factory.uniqueId ();");
	out.println ("  public Host getHost () { return host; }");
	out.println ("  public String getObjectId () { return objectId; }");
	for (int i=0; i<methods.length; i++) {
	    generateStubMethod (methods[i], out);
	}
	out.println ("}");
    }

    static void generateStubMethod (Method method, PrintWriter out) {
	Class returnType = method.getReturnType ();
	Class[] paramTypes = method.getParameterTypes ();
	Class[] excepTypes = method.getExceptionTypes ();
	String name = method.getName ();
	out.print ("  static final String[] " + name + "ParamTypeIds = new String[] { ");
	for (int i=0; i<paramTypes.length; i++) {
	    if (i>0) { out.print (", "); }
	    out.print ("\"" + paramTypes[i].getName () + "\"");
	}
	out.println (" };");
	out.print ("  public " + returnType.getName () + " " + name + " (");
	for (int i=0; i<paramTypes.length; i++) {
	    if (i > 0) { out.print (", "); }
	    out.print (paramTypes[i].getName () + " arg" + i);
	}
	out.print (")");
	for (int i=0; i<excepTypes.length; i++) {
	    if (i > 0) { out.print (", "); }
	    else { out.print (" throws "); }
	    out.print (excepTypes[i].getName ());
	}
	out.println (" {");
	out.print ("    Object[] args = new Object [] { ");
        for (int i=0; i<paramTypes.length; i++) {
	    if (i > 0) { out.print (", "); }
	    out.print ("arg" + i);
	}
	out.println (" };");
	if (returnType.getName ().equals ("void")) {
	    out.println ("    try { RMI.singleton.remoteCall (host, objectId, \"" + name + "\", " + name + "ParamTypeIds, args); }");
	} else {
	    out.println ("    try { return (" + returnType.getName () + ")(RMI.singleton.remoteCall (host, objectId, \"" + name + "\", " + name + "ParamTypeIds, args)); }");
	}
	for (int i=0; i<excepTypes.length; i++) {
	    out.println ("    catch (" + excepTypes[i].getName ()  + " ex) { throw ex; }");
	}
	out.println ("    catch (Exception ex) { throw new RuntimeException (\"Uncaught \" + ex); }");
	out.println ("  }");
    }

}
