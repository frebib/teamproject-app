package d2.teamproject.algorithm.sorting;

import java.util.List;

public interface SortState<E> {
    List<E> getList();
    boolean isComplete();
}
