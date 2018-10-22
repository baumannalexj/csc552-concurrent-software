package ajeffrey.teaching.minisoap;

import java.io.OutputStream;
import java.io.Writer;

public interface SoapWriterFactory {

    public SoapWriter build (final OutputStream out);

    public SoapWriter build (final Writer out);

}
