package d2.teamproject.algorithm.sorting;

import d2.teamproject.algorithm.AlgoStream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Provides a stream of states representing each stage of sort provided by QuickSort on a given list.
 * The Stream is pre-generated and cannot be changed once initialised.
 * Note: Initialisation may take some time dependant on the parameters
 * @param <E> Type of data stored in the list
 */
public class QuickSortStream<E> implements AlgoStream<SortState<E>> {
    private List<E> list;
    private Comparator<E> comparator;

    private int stateIndex = 0;
    private final List<SortState<E>> states;

    private ListSortState<E> lastListState;

    /**
     * Creates a new QuickSortStream provided with a list to sort and create states for
     * @param list a list to sort
     * @param comparator comparison object used to sort list
     */
    public QuickSortStream(List<E> list, Comparator<E> comparator) {
        this.list = list;
        this.comparator = comparator;
        states = new ArrayList<>();
    }

    /**
     * Sorts the list and generates all states during the list sort process
     * This process may take a long time depending on the size of the list in question
     */
    @Override
    public void initialise() {
        if (list == null || list.size() < 1)
            return;

        lastListState = new ListSortState<>(list);
        states.add(lastListState);
        quickSort(0, list.size() - 1);
        states.add(new ListSortState<>(list, true));
    }

    private void quickSort(int min, int max) {
        int i = min;
        int j = max;

        // Use middle value as the pivot
        int pivot = min + (max - min) / 2;
        E pivotElem = list.get(pivot);
        states.add(new BoundSortState<>(lastListState, pivot, min, max));

        while (i < j) {
            // Compare either side of pivot and count towards pivot
            while (comparator.compare(list.get(i), pivotElem) < 0)
                if (++i != pivot)
                    states.add(new CompareSortState<>(lastListState, pivot, min, max, i, pivot, false));
            while (comparator.compare(list.get(j), pivotElem) > 0)
                if (--j != pivot)
                    states.add(new CompareSortState<>(lastListState, pivot, min, max, j, pivot, false));

            // If the counters have passed each
            // other, we can't swap any more
            if (i >= j) {
                if (i == j) {
                    i++;
                    j--;
                }
                continue;
            }

            states.add(new CompareSortState<>(lastListState, pivot, min, max, i, j, true));
            lastListState = new ListSortState<>(list);
            states.add(lastListState);

            // Swap values that are on the wrong
            // side of the pivot then skip over
            // them with i++ and j--
            E temp = list.get(i);
            list.set(i++, list.get(j));
            list.set(j--, temp);
        }

        // Recurse into either side of the pivot
        if (min < j) quickSort(min, j);
        if (i < max) quickSort(i, max);
    }

    /**
     * Gets the sorted list from the output of the algorithm
     * Equally, getting the list from the last state would return a copy of the same list
     * @return the sorted list
     */
    public List<E> getSortedList() {
        return list;
    }

    @Override
    public SortState<E> getNext() {
        if (hasNext())
            return states.get(++stateIndex);
        return null;
    }

    @Override
    public SortState<E> getPrevious() {
        if (hasPrevious())
            return states.get(--stateIndex);
        return null;
    }

    @Override
    public SortState<E> getNth(int n) {
        if (hasNth(n))
            return states.get(n);
        return null;
    }

    @Override
    public boolean hasNext() {
        return stateIndex + 1 < states.size();
    }

    @Override
    public boolean hasPrevious() {
        return stateIndex > 0;
    }

    @Override
    public boolean hasNth(int n) {
        return n >= 0 && n < states.size();
    }

    @Override
    public List<SortState<E>> getAll() {
        return states;
    }
}