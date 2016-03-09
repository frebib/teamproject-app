package d2.teamproject.algorithm.search.datastructures;

import java.util.ArrayDeque;
import java.util.Collection;

/**
 * Stores a {@code Collection} of items in a Queue {@link SearchCollection}
 * @param <E> @inheritDoc
 */
public class SearchQueue<E> extends ArrayDeque<E> implements SearchCollection<E> {
    public SearchQueue(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public E getHead() {
        return poll();
    }

    @Override
    public SearchCollection<E> copy() {
        return new SearchQueue<>(this);
    }
}
