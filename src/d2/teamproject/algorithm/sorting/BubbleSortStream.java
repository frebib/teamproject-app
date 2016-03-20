package d2.teamproject.algorithm.sorting;

import java.util.Comparator;
import java.util.List;

/**
 * @author Otonye
 * @class BubbleSortStream
 */
public class BubbleSortStream<E> extends SortStream<E> {
    private ListSortState<E> lastListState;

    /**
     * Creates a new QuickSortStream provided with a list to sort and create states for
     * @param list       a list to sort
     * @param comparator comparison object used to sort list
     */
    public BubbleSortStream(List<E> list, Comparator<E> comparator) {
        super(list, comparator);
    }

    @Override
    public SortStream<E> initialise() {
        if (list == null || list.size() < 1)
            return this;

        lastListState = new ListSortState<>(list);
        states.add(lastListState);
        sort(list);
        states.add(new ListSortState<>(list, true));
        return this;
    }

    private List<E> sort(List<E> items) {
        for (int i = 0; i < items.size(); i++) {
            for (int k = i; k < items.size() - 1; k++) {
                boolean swap = comparator.compare(items.get(k), items.get(k+1)) > 0;
                states.add(new CompareSortState<>(lastListState, 0, 0, list.size() - 1, k, k+1, swap));
                if (swap) {
                    E store = items.get(k+1);
                    items.set(k+1, items.get(k));
                    items.set(k, store);

                    lastListState = new ListSortState<>(list);
                    states.add(lastListState);
                }
            }
        }
        return items;
    }
}