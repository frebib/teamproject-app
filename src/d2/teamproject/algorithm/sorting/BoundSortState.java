package d2.teamproject.algorithm.sorting;

import java.util.List;

public class BoundSortState<E> implements SortState<E> {
    private int pivot, lower, upper;
    private ListSortState<E> listState;

    public BoundSortState(ListSortState<E> listState, int pivot, int lower, int upper) {
        this.pivot = pivot;
        this.lower = lower;
        this.upper = upper;
        this.listState = listState;
    }

    public int getPivot() {
        return pivot;
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }

    @Override
    public List<E> getList() {
        return listState.getList();
    }

    @Override
    public boolean isComplete() {
        return listState.isComplete();
    }
}
