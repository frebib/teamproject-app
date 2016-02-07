package d2.teamproject.module.algorithm.search.datastructures;

import sun.security.krb5.internal.crypto.Aes128;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Stores a {@code Collection} of items in a PriorityQueue {@link d2.teamproject.module.algorithm.search.datastructures.DataStructure}
 *
 * @param <A> @inheritDoc
 */
public class SearchPriorityQueue<A> extends PriorityQueue<A> implements DataStructure<A> {
    public SearchPriorityQueue(Comparator<A> comparer) {
        super(comparer);
    }
    public SearchPriorityQueue(Collection<? extends A> c) {
        super(c);
    }

    @Override
    public A getHead() {
        return poll();
    }
}
