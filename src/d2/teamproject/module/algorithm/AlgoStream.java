package d2.teamproject.module.algorithm;

public interface AlgoStream<T> {
    void initialise();
    void reset();
    T getNext();
    T getPrevious();
    boolean hasNext();
    boolean hasPrevious();
}
