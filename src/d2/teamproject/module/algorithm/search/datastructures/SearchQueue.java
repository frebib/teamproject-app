package d2.teamproject.module.algorithm.search.datastructures;

import java.util.ArrayDeque;

/**
 * Stores a {@code Collection} of items in a Queue {@link d2.teamproject.module.algorithm.search.datastructures.DataStructure}
 *
 * @param <A> @inheritDoc
 */
public class SearchQueue<A> extends ArrayDeque<A> implements DataStructure<A> {
    @Override
    public A getHead() {
        return poll();
    }
}
