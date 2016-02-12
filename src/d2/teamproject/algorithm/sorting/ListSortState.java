package d2.teamproject.algorithm.sorting;

import java.util.ArrayList;
import java.util.List;

public class ListSortState<E> implements SortState<E> {
    private List<E> list;
    private boolean isComplete;

    public ListSortState(List<E> list) {
        this(list, false);
    }

    public ListSortState(List<E> list, boolean isComplete) {
        this.list = new ArrayList<E>(list);
        this.isComplete = isComplete;
    }

    @Override
    public List<E> getList() {
        return list;
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}
