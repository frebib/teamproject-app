package d2.teamproject.algorithm.sorting;

import d2.teamproject.algorithm.AlgoStream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

        lastListState = new ListSortState<E>(list);
        states.add(lastListState);
        quickSort(0, list.size() - 1);
        states.add(new ListSortState<E>(list, true));
    }

    private void quickSort(int lowerIndex, int upperIndex) {
        int i = lowerIndex;
        int j = upperIndex;

        // Use middle value as the pivot point
        int pivotIndex = lowerIndex + (upperIndex - lowerIndex) / 2;
        E pivot = list.get(pivotIndex);
        states.add(new BoundSortState<E>(lastListState, pivotIndex, lowerIndex, upperIndex));

        // Divide into two arrays
        while (i <= j) {
            // Compare either side of pivot and count towards pivot
            while (comparator.compare(list.get(i), pivot) < 0)
                states.add(new CompareSortState<E>(lastListState, pivotIndex, lowerIndex, upperIndex, i++, pivotIndex, false));
            while (comparator.compare(list.get(j), pivot) > 0)
                states.add(new CompareSortState<E>(lastListState, pivotIndex, lowerIndex, upperIndex, j--, pivotIndex, false));

            if (i <= j) {
                states.add(new CompareSortState<E>(lastListState, pivotIndex, lowerIndex, upperIndex, i, j, true));
                lastListState = new ListSortState<E>(list);
                // Swap values then move index
                // to next position on both sides
                exchangeNumbers(i++, j--);
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < upperIndex)
            quickSort(i, upperIndex);
    }

    private void exchangeNumbers(int i, int j) {
        E temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
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