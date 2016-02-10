package d2.teamproject.module.algorithm.search.datastructures;

import sun.security.krb5.internal.crypto.Aes128;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Stores a {@code Collection} of items in a PriorityQueue {@link BaseDataStructure}
 *
 * @param <E> @inheritDoc
 */
public class SearchPriorityQueue<E> extends PriorityQueue<E> implements BaseDataStructure<E> {
    public SearchPriorityQueue(Comparator<E> comparer) {
        super(comparer);
    }
    public SearchPriorityQueue(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public E getHead() {
        return poll();
    }

    @Override
	public BaseDataStructure<E> copy() {
		return new SearchPriorityQueue<E>(this);
	}
}
