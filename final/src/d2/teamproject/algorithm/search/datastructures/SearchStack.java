package d2.teamproject.algorithm.search.datastructures;

import java.util.ArrayDeque;
import java.util.Collection;

/**
 * Stores a {@code Collection} of items in a Stack
 * {@link SearchCollection}
 * The implementation actually uses a {@link java.util.ArrayDeque} in place of a
 * {@link java.util.Stack}
 * @param <E>
 * @inheritDoc
 */
public class SearchStack<E> extends ArrayDeque<E> implements SearchCollection<E> {
    public SearchStack() {
    }
    public SearchStack(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public E getHead() {
        return pop();
    }

    @Override
    public boolean add(E e) {
        push(e);
        return true;
    }

    @Override
    public SearchCollection<E> copy() {
        return new SearchStack<>(new SearchStack<>(this));
    }
}
