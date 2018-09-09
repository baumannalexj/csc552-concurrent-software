package ajeffrey.teaching.minisoap;

import java.io.IOException;
import java.net.ServerSocket;

public interface ExecutorFactory {

    public Executor build (Host host) throws IOException;

    public ServerSocket getServerSocket ();

}
