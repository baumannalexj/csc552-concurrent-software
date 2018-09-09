package ajeffrey.teaching.minisoap;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.io.IOException;
import ajeffrey.teaching.debug.Debug;

public interface RMI {

    public Object remoteCall (Host host, String objectId, String methodId, String[] argTypeIds, Object[] args) throws Exception;

    public static final RMI singleton = new RMIImpl ();

}

class RMIImpl implements RMI {

    public Object remoteCall (Host host, String objectId, String methodId, String[] argTypeIds, Object[] args) throws Exception {
	Result result = new Result ();
	Task call = new Call (objectId, methodId, argTypeIds, args, result.resultId);
	Executor exec = Executor.factory.build (host);
	exec.executeRemote (call);
	return result.get ();
    }

}

class Result {

    static final HashMap lookup = new HashMap ();
    static int numResults;

    final String resultId = "result" + ++numResults;
    boolean waiting = true;
    Object returned;

    Result () { lookup.put (resultId, this); }

    synchronized void returnResult (Object returned) { 
	this.returned = returned; this.waiting = false; notifyAll (); 
    }

    synchronized Object get () throws Exception { 
	while (waiting) { wait (); }
	return returned;
    }

}
