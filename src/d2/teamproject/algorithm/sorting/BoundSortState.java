package d2.teamproject.algorithm.sorting;

import java.util.List;

/**
 * Represents the state of a list sort and a bounded region within the list
 * @param <E> Type of data stored in the list
 */
public class BoundSortState<E> implements SortState<E> {
    private int pivot, lower, upper;
    private ListSortState<E> listState;

    public BoundSortState(ListSortState<E> listState, int pivot, int lower, int upper) {
        this.pivot = pivot;
        this.lower = lower;
        this.upper = upper;
        this.listState = listState;
    }

    /**
     * Gets the list pivot
     * @return pivot index , else -1 if no pivot or sort doesn't support pivoting
     */
    public int getPivot() {
        return pivot;
    }

    /**
     * Gets lower bound of the list boundary
     * @return lower bound or -1 if not set
     */
    public int getLower() {
        return lower;
    }

    /**
     * Gets upper bound of the list boundary
     * @return upper bound or -1 if not set
     */
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

    @Override
    public String toString() {
        return "BoundSortState{" +
                "pivot=" + pivot +
                ", lower=" + lower +
                ", upper=" + upper +
                ", listState=" + listState +
                '}';
    }
}