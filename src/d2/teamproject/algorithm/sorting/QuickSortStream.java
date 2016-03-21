package d2.teamproject.algorithm.sorting;

import java.util.Comparator;
import java.util.List;

/**
 * @author Otonye Bestman
 * Provides a stream of states representing each stage of sort provided by QuickSort on a given list.
 * The Stream is pre-generated and cannot be changed once initialised.
 * Note: Initialisation may take some time dependant on the parameters
 * @param <E> @inheritdoc
 */
public class QuickSortStream<E> extends SortStream<E> {
    private ListSortState<E> lastListState;

    /**
     * Creates a new QuickSortStream provided with a list to sort and create states for
     * @param list       a list to sort
     * @param comparator comparison object used to sort list
     */
    public QuickSortStream(List<E> list, Comparator<E> comparator) {
        super(list, comparator);
    }

    /**
     * Sorts the list and generates all states during the list sort process
     * This process may take a long time depending on the size of the list in question
     */
    @Override
    public QuickSortStream<E> initialise() {
        if (list == null || list.size() < 1)
            return this;

        lastListState = new ListSortState<>(list);
        states.add(lastListState);
        quickSort(0, list.size() - 1);
        states.add(new ListSortState<>(list, true));
        return this;
    }

    private void quickSort(int min, int max) {
        int i = min;
        int j = max;

        // Use middle value as the pivot
        int pivot = min + (max - min) / 2;
        E pivotElem = list.get(pivot);
        states.add(new PartitionSortState<>(lastListState, pivot, min, max));

        while (i < j) {
            // Compare either side of pivot and count towards pivot
            while (comparator.compare(list.get(i), pivotElem) < 0)
                states.add(new CompareSortState<>(lastListState, pivot, min, max, i++, pivot, false));
            while (comparator.compare(list.get(j), pivotElem) > 0)
                states.add(new CompareSortState<>(lastListState, pivot, min, max, j--, pivot, false));

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

            // Swap values that are on the wrong
            // side of the pivot then skip over
            // them with i++ and j--
            E temp = list.get(i);
            list.set(i++, list.get(j));
            list.set(j--, temp);

            lastListState = new ListSortState<>(list);
            states.add(lastListState);
        }

        // TODO: Represent the return from the recursive call on PartitionSortState<>
        // Recurse into either side of the pivot
        if (min < j) quickSort(min, j);
        if (i < max) quickSort(i, max);
    }
}