package ajeffrey.teaching.catalog;

import ajeffrey.teaching.debug.Debug;

public class Main {

    public static void main (String[] args) {
	Debug.out.addPrintStream (System.err);
	final Model model = Model.factory.build ();
	final View view = View.factory.build (model);
        view.start ();
    }
}
