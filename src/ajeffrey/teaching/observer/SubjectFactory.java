package ajeffrey.teaching.observer;

/**
 * A factory for building subjects.
 * @author Alan Jeffrey
 * @version 1.0.2
 */
public interface SubjectFactory {

    /**
     * Build a new subject.
     * @return a new subject
     */
    public Subject build ();

}
