package ajeffrey.teaching.date;

import ajeffrey.teaching.debug.Debug;
import java.util.Date;

public interface DateFactory {

    public Date build ();

    public static final DateFactory singleton = new DateFactoryImpl ();

}

class DateFactoryImpl implements DateFactory {

    public Date build () {
	Debug.out.println ("DateFactory.build: Starting...");
	Date result = new Date (); 
	Debug.out.println ("DateFactory.build: Returning: " + result);
        return result;
    }

}
