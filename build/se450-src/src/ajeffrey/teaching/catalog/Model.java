package ajeffrey.teaching.catalog;

import ajeffrey.teaching.debug.Debug;

public interface Model {

    public void search (String keyword);
    public void refine (String keyword);
    public int numTitles ();

    public void findTitles ();
    public String[] getTitles ();
    public boolean hasTitles ();

    public void addBook (String book);

    public ModelFactory factory = new ModelFactoryImpl ();

}

class ModelFactoryImpl implements ModelFactory {

    public Model build () { return new ModelImpl (); }

}

class ModelImpl implements Model {

    Catalog catalog = Catalog.empty;
    ResultSet resultSet = null;

    String[] titles = null;

    public void search (String keyword) {
	Debug.out.println ("Searching for " + keyword);
	resultSet = catalog.search (keyword);
	titles = null;
    }

    public void refine (String keyword) {
	Debug.out.println ("Refining search for " + keyword);
	if (this.resultSet != null) { 
	    this.resultSet = this.resultSet.refine (keyword);
	}
	this.titles = null;
    }

    public void findTitles () {
	Debug.out.println ("Finding titles");
	if (resultSet != null) {
	    final Book[] books = resultSet.getBooks ();
	    final String[] titles = new String[books.length];
	    for (int i=0; i<books.length; i++) {
		titles[i] = books[i].getTitle ();
	    }
	    this.titles = titles;
	}
    }

    public int numTitles () {
	if (resultSet == null) {
	    return 0;
	} else {
	    return resultSet.size ();
	}
    }

    public String[] getTitles () {
	return titles;
    }
    
    public boolean hasTitles () {
	return titles != null && titles.length > 0;
    }

    public void addBook (String book) {
	Debug.out.println ("Adding book " + book);
	catalog = catalog.addTitle (book);
    }

}
