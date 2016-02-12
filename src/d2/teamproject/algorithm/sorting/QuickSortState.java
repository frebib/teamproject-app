package d2.teamproject.algorithm.sorting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Use {@link SortState} and implementing classes to represent
 * states of a list sort in a more memory efficient way.
 *
 * Completely represents the state of a QuickSort
 * in a very memory-inefficient way.. oh well
 * @param <E> Type of data stored in the list
 */
@Deprecated
public class QuickSortState<E> {
    // TODO: Split into subclasses to save memory
	private int pivot, lower, upper;
    private int compareA = -1, compareB = -1;
    boolean isComplete = false, isSwap = false;
    private List<E> list;

    public QuickSortState(List<E> list, int pivot, int lower, int upper) {
        this.list = new ArrayList<>(list);
        this.pivot = pivot;
        this.lower = lower;
        this.upper = upper;

        this.compareA = -1;
    }

    public QuickSortState(List<E> list, int pivot, int lower, int upper, int compareA, int compareB) {
        this.list = new ArrayList<>(list);
        this.pivot = pivot;
        this.lower = lower;
        this.upper = upper;
        this.compareA = compareA;
        this.compareB = compareB;
    }

    public QuickSortState<E> setSwap() {
        this.isSwap = true;
        return this;
    }

    public QuickSortState<E> setComplete() {
        this.isComplete = true;
        return this;
    }

    public List<E> getList() {
        return list;
    }

    public boolean hasPivot() {
        return pivot != -1;
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

    public boolean isSwap() {
        return isSwap;
    }

    public Point getCompares() {
        if (compareA == -1 || compareB == -1)
            return null;
        return new Point(compareA, compareB);
    }

    public boolean isComplete() {
        return isComplete;
    }
}
