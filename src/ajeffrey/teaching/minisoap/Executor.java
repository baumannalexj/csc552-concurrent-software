package ajeffrey.teaching.minisoap;

import java.util.HashMap;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import ajeffrey.teaching.debug.Debug;

public interface Executor {

    public void executeLocal (Task task);
    public void executeRemote (Task task) throws IOException;

    public Socket getSocket ();

    public static final ExecutorFactory factory = new ExecutorFactoryImpl ();

}

class ExecutorFactoryImpl implements ExecutorFactory, Runnable {

    final Thread thread = new Thread (this);
    final HashMap flyweight = new HashMap ();
    final ServerSocket serverSocket;

    ExecutorFactoryImpl () { this (0); }
    
    ExecutorFactoryImpl (int port) { try {
	Debug.out.println ("ExecutorFactoryImpl: building server socket on port " + port);
        serverSocket = new ServerSocket (port);
	Debug.out.println ("ExecutorFactoryImpl: starting thread");
        thread.start ();
	Debug.out.println ("ExecutorFactoryImpl: done"); 
   } catch (IOException ex) {
	Debug.out.println ("ExecutorFactoryImpl: failed");
	throw new RuntimeException ("ExecutorFactory failed to initialize: " + ex);
    } }
    
    public Executor build (Host host) throws IOException {
	Executor result = (Executor)(flyweight.get (host));
	if (result == null) {
	    Debug.out.println ("ExecutorFactoryImpl.build: creating new socket for " + host);
	    Socket socket = new Socket (host.getName (), host.getPort ());
	    result = new ExecutorImpl (socket);
	    flyweight.put (host, result);
	    Debug.out.println ("ExecutorFactoryImpl.build: done.");
	}
	return result;
    }

    public ServerSocket getServerSocket () { return serverSocket; }

    public void run () {
	try {
	    while (true) {
		Debug.out.println ("ExecutorFactoryImpl.run: waiting");
		Socket socket = serverSocket.accept ();
		Debug.out.println ("ExecutorFactoryImpl.run: got connection");
		Executor exec = new ExecutorImpl (socket);
	    }
	} catch (IOException ex) {
	    Debug.out.println ("ExecutorFactoryImpl.run: died.");	
	    throw new RuntimeException ("ExecutorFactory died: " + ex);
	}
    }

}

class ExecutorImpl implements Executor, Runnable {

    final Thread thread = new Thread (this);
    final Socket socket;
    final SoapReader in;
    final SoapWriter out;

    ExecutorImpl (Socket socket) throws IOException {
	this.socket = socket;
	this.in = SoapReader.factory.build (socket.getInputStream ());
	this.out = RMISoapWriter.factory.build (socket.getOutputStream ());
	this.thread.start ();
    }

    public void executeLocal (Task task) {
	Debug.out.println ("ExecutorImpl.executeLocal: running local task " + task);
	task.run (this);
	Debug.out.println ("ExecutorImpl.executeLocal: done");
    }

    final SoapWriter debug = RMISoapWriter.factory.build (System.err);

    public void executeRemote (Task task) throws IOException {
	Debug.out.println ("ExecutorImpl.executeRemote: running remote task " + task);
	debug.serialize (task);
	debug.flush ();
	out.serialize (task);
	out.flush ();
	Debug.out.println ("ExecutorImpl.executeRemote: done");
    }

    public Socket getSocket () { return socket; }

    public void run () { try {
	while (true) {
	    Debug.out.println ("ExecutorImpl.run: waiting");
	    Task task = (Task)(in.unserialize ());
	    if (task == null) { return; }
	    executeLocal (task);
	}
    } catch (IOException ex) {
	Debug.out.println ("ExecutorImpl: failed " + ex);
    } finally {
	try { socket.close (); }
	catch (IOException ex) { Debug.out.println ("ExecutorImpl: failed to close socket " + ex); }
    } }

}
