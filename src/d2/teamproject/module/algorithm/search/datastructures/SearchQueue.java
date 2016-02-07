package d2.teamproject.module.algorithm.search.datastructures;

import java.util.ArrayDeque;
import java.util.Collection;

/**
 * Stores a {@code Collection} of items in a Queue {@link d2.teamproject.module.algorithm.search.datastructures.DataStructure}
 *
 * @param <A> @inheritDoc
 */
public class SearchQueue<A> extends ArrayDeque<A> implements DataStructure<A> {
    public SearchQueue(Collection<? extends A> c) {
        super(c);
    }

    @Override
    public A getHead() {
        return poll();
    }
}
