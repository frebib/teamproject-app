package d2.teamproject.algorithm.sorting;

import java.awt.*;

public class CompareSortState<E> extends BoundSortState<E> {
    private int compareA, compareB;
    private boolean isSwap;

    public CompareSortState(ListSortState<E> listState, int pivot, int lower, int upper, int compareA, int compareB, boolean isSwap) {
        super(listState, pivot, lower, upper);
        this.compareA = compareA;
        this.compareB = compareB;
        this.isSwap = isSwap;
    }

    public Point getCompares() {
        return new Point(compareA, compareB);
    }

    public boolean isSwap() {
        return isSwap;
    }
}
