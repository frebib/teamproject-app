package d2.teamproject.module.algorithm;

public interface AlgoChunk<T> extends AlgoStream<T> {
    T getNth(int n);
    List<T> getAll();
}
