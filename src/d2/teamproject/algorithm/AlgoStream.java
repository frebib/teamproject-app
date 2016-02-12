package d2.teamproject.algorithm;

import java.util.List;

public interface AlgoStream<T> {
    void initialise();
    T getNext();
    T getPrevious();
    T getNth(int n);
    boolean hasNext();
    boolean hasPrevious();
    boolean hasNth(int n);
    List<T> getAll();
}