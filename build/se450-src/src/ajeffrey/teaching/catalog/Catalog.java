package ajeffrey.teaching.catalog;

import ajeffrey.teaching.util.Dictionary;

import java.util.StringTokenizer;

public interface Catalog {

    public Catalog addTitle (String title);
    public ResultSet search (String keyword);

    public static final Catalog empty = new CatalogImpl (Dictionary.empty);

}

class CatalogImpl implements Catalog {

    final Dictionary contents;

    CatalogImpl (Dictionary contents) {
	this.contents = contents;
    }

    public Catalog addTitle (String title) {
	final Book book = new BookImpl (title);
	final String[] keywords = book.getKeywords ();
	Dictionary contents = this.contents;
	for (int i=0; i<keywords.length; i++) {
	    final ResultSetImpl results = 
		(ResultSetImpl)(search (keywords[i]));
	    contents = contents.add (keywords[i], results.add (book));
	}
	return new CatalogImpl (contents);
    }
    
    public ResultSet search (String keyword) {
	final ResultSet results = (ResultSet)(contents.get (keyword));
	if (results == null) { return ResultSetImpl.empty; }
	else { return results; }
    }

}

class ResultSetImpl implements ResultSet {

    final static ResultSet empty = 
	new ResultSetImpl (Dictionary.empty, Dictionary.empty, Dictionary.empty, 0);

    final Dictionary keywords;
    final Dictionary books;
    final Dictionary contents;
    final int size;

    ResultSetImpl (Dictionary keywords, Dictionary books, Dictionary contents, int size) { 
	this.keywords = keywords;
	this.books = books;
	this.contents = contents; 
	this.size = size;
    }

    public int size () { return size; }

    public Book[] getBooks () {
	final Object[] bookTitles = books.getKeys ();
	final Book[] result = new Book[bookTitles.length];
	for (int i=0; i<bookTitles.length; i++) {
	    result[i] = (Book)(books.get (bookTitles[i]));
	}
	return result;
    }
    
    public ResultSet refine (String keyword) {
	if (keywords.containsKey (keyword)) { 
	    return this; 
	} else {
	    final ResultSet result = (ResultSet)(contents.get (keyword));
	    if (result == null) {
		return new ResultSetImpl 
		    (keywords.add (keyword, keyword), Dictionary.empty, Dictionary.empty, 0);
	    } else { 
		return result; 
	    }
	}
    }

    ResultSet add (Book book) {
	if (this.books.containsKey (book.getTitle ())) {
	    return this;
	} else {
	    final String[] keywords = book.getKeywords ();
	    Dictionary contents = this.contents;
	    for (int i=0; i<keywords.length; i++) {
		if (!this.keywords.containsKey (keywords[i])) {
		    ResultSetImpl results = 
			(ResultSetImpl)(refine (keywords[i]));
		    contents = contents.add (keywords[i],results.add (book));
		}
	    }
	    return new ResultSetImpl 
		(this.keywords, 
		 this.books.add (book.getTitle (), book), 
		 contents, 
		 this.size+1);
	}
    }

}

class BookImpl implements Book {

    final String title;
    final String[] keywords;

    BookImpl (String title) {
	final StringTokenizer st = new StringTokenizer (title);
	final String[] keywords = new String [st.countTokens ()];
	for (int i=0; i<keywords.length; i++) {
	    keywords[i] = st.nextToken ();
	}
	this.title = title;
	this.keywords = keywords;
    }

    public String getTitle () { return title; } 
    public String[] getKeywords () { return keywords; }

}
