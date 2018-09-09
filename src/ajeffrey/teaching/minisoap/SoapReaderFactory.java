package ajeffrey.teaching.minisoap;

import java.io.InputStream;
import java.io.Reader;

public interface SoapReaderFactory {

    public SoapReader build (final InputStream in);

}
