package d2.teamproject.algorithm.sorting;

import java.awt.*;

/**
 * Represents the state of a list sort at the time of comparison
 * If the values are swapped then list representation is from before the swap
 * @param <E> Type of data stored in the list
 */
public class CompareSortState<E> extends BoundSortState<E> {
    private int compareA, compareB;
    private boolean isSwap;

    public CompareSortState(ListSortState<E> listState, int pivot, int lower, int upper, int compareA, int compareB, boolean isSwap) {
        super(listState, pivot, lower, upper);
        this.compareA = compareA;
        this.compareB = compareB;
        this.isSwap = isSwap;
    }

    /**
     * Gets a pair of two values being compared
     * @return a pair of integers representing the two compared values
     */
    public Point getCompares() {
        return new Point(compareA, compareB);
    }

    /**
     * Gets whether the two values have been swapped after comparison
     * @return true if the values were swapped
     */
    public boolean isSwap() {
        return isSwap;
    }
}
