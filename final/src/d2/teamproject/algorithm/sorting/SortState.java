package d2.teamproject.algorithm.sorting;

import java.util.List;

/**
 * Represents the state of a list during a list sort
 * @param <E> Type of data stored in the list
 */
public interface SortState<E> {

    /**
     * Gets the represented list
     * @return the list
     */
    List<E> getList();

    /**
     * Gets whether the list sort is complete
     * @return true if complete
     */
    boolean isComplete();
}
