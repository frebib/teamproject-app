package d2.teamproject.module.algorithm.search.datastructures;

import java.util.ArrayDeque;
import java.util.Collection;

/**
 * Stores a {@code Collection} of items in a Queue {@link BaseDataStructure}
 *
 * @param <E> @inheritDoc
 */
public class SearchQueue<E> extends ArrayDeque<E> implements BaseDataStructure<E> {
    public SearchQueue(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public E getHead() {
        return poll();
    }

	@Override
	public BaseDataStructure<E> copy() {
		return new SearchQueue<>(this);
	}
}
