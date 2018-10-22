package ajeffrey.teaching.test;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.minisoap.SoapReader;
import java.io.IOException;

import java.net.Socket;
import java.net.ServerSocket;

public class TestSoapServer {

    public static void main (final String[] args) throws IOException {
	//	Debug.out.setPrintStream (System.err);
	final ServerSocket serverSocket = new ServerSocket (2000);
	while (true) {
	    final Socket socket = serverSocket.accept ();
	    final SoapReader in = SoapReader.factory.build (socket.getInputStream ());
	    final Object obj = in.unserialize ();
	    System.out.println (obj);
	    socket.close ();
	}
    }

}

