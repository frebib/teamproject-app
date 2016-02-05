package d2.teamproject.module.algorithm.search.datastructures;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Stores a {@code Collection} of items in a PriorityQueue {@link d2.teamproject.module.algorithm.search.datastructures.DataStructure}
 *
 * @param <A> @inheritDoc
 */
public class SearchPriorityQueue<A> extends PriorityQueue<A> implements DataStructure<A> {
    private static final long serialVersionUID = 2623101708315097645L;

    public SearchPriorityQueue(Comparator<A> comparer) {
        super(comparer);
    }

    @Override
    public A getHead() {
        return poll();
    }
}
