package bo.dev.davidz.imageguess;

/**
 * Interface definition to define a callback to be invoked when the server returns an answer of type T.
 */
public interface ServerAnswerListener<T> {
    public void processServerAnswer(T answer);
}
