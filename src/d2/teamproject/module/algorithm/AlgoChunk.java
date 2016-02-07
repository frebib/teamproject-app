package d2.teamproject.module.algorithm;

import java.util.List;

public interface AlgoChunk<T> extends AlgoStream<T> {
    T getNth(int n);
    List<T> getAll();
}