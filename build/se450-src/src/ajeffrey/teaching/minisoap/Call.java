package ajeffrey.teaching.minisoap;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import ajeffrey.teaching.debug.Debug;

public class Call implements Task {

    public String objectId;
    public String methodId;
    public String[] argTypeIds;
    public Object[] args;
    public String resultId;

    public Call () {} // Needed for serialization.

    public Call (String objectId, String methodId, String[] argTypeIds, Object[] args, String resultId) {
	this.objectId = objectId; 
	this.methodId = methodId; 
	this.argTypeIds = argTypeIds;
        this.args = args; 
	this.resultId = resultId;
    }

    public void run (Executor exec) {
	try {
	    Remote object = (Remote)(Stub.lookup.get (objectId));
	    Class clazz = Class.forName (object.getRemoteInterface ());
	    Class[] argTypes = new Class[argTypeIds.length];
	    for (int i=0; i<argTypes.length; i++) {
		argTypes[i] = Class.forName (argTypeIds[i]);
	    }
	    Method method = clazz.getDeclaredMethod (methodId, argTypes);
	    Object result = method.invoke (object, args);
	    Task returnResult = new Return (resultId, result);
	    exec.executeRemote (returnResult);
	} catch (InvocationTargetException reflected) {
	    Throwable ex = reflected.getTargetException ();
	    // BUG: this exception should be returned to the caller!
	    Debug.out.println ("Call: failed " + ex);
	} catch (Exception ex) {
	    // BUG: this exception should be returned to the caller!
	    Debug.out.println ("Call: failed " + ex);
	}
    }

    public String toString () { 
	String result = "call " + objectId + "." + methodId + " (";
	for (int i=0; i<args.length; i++) {
	    if (i>0) { result = result + ", "; }
	    result = result + args[i]; 
	}
	result = result + ") to " + resultId; 
	return result;
    }

}

