package d2.teamproject.algorithm.sorting;

import d2.teamproject.algorithm.AlgoStream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Otonye
 * @class BubbleSortStream
 */

public class BubbleSortStream<E extends Comparable<E>> implements AlgoStream<SortState<E>> {
	private List<E> list;
	private Comparator<E> comparator;

	private int stateIndex = 0;
	private final List<SortState<E>> states;
	boolean swap;
	private ListSortState<E> lastListState;

	/**
	 * Creates a new QuickSortStream provided with a list to sort and create
	 * states for
	 * 
	 * @param list
	 *            a list to sort
	 * @param comparator
	 *            comparison object used to sort list
	 */
	public BubbleSortStream(List<E> list, Comparator<E> comparator) {
		this.list = new ArrayList<>(list);
		this.comparator = comparator;
		states = new ArrayList<>();
	}

	public List<E> sort(List<E> items) {
		for (int i = 0; i < items.size() - 1; i++) {
			for (int k = items.size() - 1; k > i; k--) {
				swap = items.get(i).compareTo(items.get(k)) > 0;
				if (swap) {
					E store = items.get(i);
					items.set(i, items.get(k));
					items.set(k, store);
					lastListState = new ListSortState<>(list);
					states.add(lastListState);
				}
				states.add(new CompareSortState<>(lastListState, 0, 0, list.size() - 1, i, k, swap));

			}
		}
		return items;
	}

	public List<E> getSortedList() {
		return list;
	}

	@Override
	public AlgoStream<SortState<E>> initialise() {
		if (list == null || list.size() < 1)
			return this;

		lastListState = new ListSortState<>(list);
		states.add(lastListState);
		sort(list);
		states.add(new ListSortState<>(list, true));
		return this;
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
