package ajeffrey.teaching.pingpong.client;

import ajeffrey.teaching.debug.Debug;
import ajeffrey.teaching.io.SocketIO;
import com.macfaq.io.SafeBufferedReader;
import com.macfaq.io.SafePrintWriter;

import java.io.IOException;
import java.net.Socket;

/**
 * A Ping-Pong client.
 *
 * @author Alan Jeffrey
 * @version 1.0.1
 */

public class Main {

    public static void main(String[] args) {
        final int numClients;
        Debug.out.addPrintStream(System.err);
        if (args.length == 0) {
            numClients = 10;
        } else {
            numClients = Integer.parseInt(args[0]);
        }
        for (int i = 0; i < numClients; i++) {
            Debug.out.println("Creating client " + i);
            final Client client = new Client();
            client.start();
        }
    }

}

class Client implements Runnable {

    public static final int SLEEP_MODIFER = 0;
    final Thread thread = new Thread(this);

    public void start() {
        thread.start();
    }

    public void run() {
        try {
            while (true) {
                Debug.out.println("Making connection");
                final Socket socket = new Socket("127.0.0.1", 2000);
                final SafePrintWriter writeOut =
                        SocketIO.singleton.buildSafePrintWriter(socket, "\n");
                final SafeBufferedReader readIn =
                        SocketIO.singleton.buildSafeBufferedReader(socket);
                final String welcome = readIn.readLine();
                Debug.out.println("Got " + welcome);
                while (Math.random() > .1) {
                    if (Math.random() > .5) {
                        Debug.out.println("Sending PING");
                        writeOut.println("PING");
                    } else {
                        Debug.out.println("Sending PONG");
                        writeOut.println("PONG");
                    }
                    final String line = readIn.readLine();
                    Debug.out.println("Got " + line);
                    thread.sleep((long) (Math.random() * SLEEP_MODIFER));
                }
                Debug.out.println("Quitting");
                writeOut.println("QUIT");
                socket.close();
                thread.sleep((long) (Math.random() * 2 * SLEEP_MODIFER));
            }
        } catch (final IOException ex) {
            Debug.out.println("Caught " + ex);
        } catch (final InterruptedException ex) {
            Debug.out.println("Caught " + ex);
        }
    }

}
