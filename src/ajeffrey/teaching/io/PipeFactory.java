package ajeffrey.teaching.io;

import java.io.Reader;
import java.io.Writer;
import java.io.InputStream;
import java.io.OutputStream;

public interface PipeFactory {

    public Pipe build (Reader in, Writer out);

    public Pipe build (InputStream in, OutputStream out);

}
