package d2.teamproject.algorithm.sorting;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state of a list during a list sort
 * and whether the sort is completed
 * @param <E> Type of data stored in the list
 *
 * @author Joseph Groocock
 */
public class ListSortState<E> implements SortState<E> {
    private List<E> list;
    private boolean isComplete;

    /**
     * Copies the represented list, also sets sort as not complete
     * @param list list to represent
     */
    public ListSortState(List<E> list) {
        this(list, false);
    }

    /**
     * Copies the represented list
     * @param list       list to represent
     * @param isComplete whether the list sort has been completed
     */
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

    @Override
    public String toString() {
        return "ListSortState{" +
                "list=" + list +
                ", isComplete=" + isComplete +
                '}';
    }
}