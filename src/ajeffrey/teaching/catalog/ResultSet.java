package ajeffrey.teaching.catalog;

public interface ResultSet {

    public ResultSet refine (String keyword);
    public Book[] getBooks ();
    public int size ();

}
