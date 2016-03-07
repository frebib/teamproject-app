package d2.teamproject.algorithm.search.datastructures;

import java.util.ArrayDeque;
import java.util.Collection;

/**
 * Stores a {@code Collection} of items in a Stack
 * {@link BaseDataStructure}
 * The implementation actually uses a {@link java.util.ArrayDeque} in place of a
 * {@link java.util.Stack}
 * @param <E>
 * @inheritDoc
 */
public class SearchStack<E> extends ArrayDeque<E> implements BaseDataStructure<E> {
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
    public BaseDataStructure<E> copy() {
        SearchStack<E> temp = new SearchStack<E>(this);
        SearchStack<E> temp2 = new SearchStack<E>(this);
        temp.clear();
        for (int i = this.size() - 1; i >= 0; i--) {
            temp.add(temp2.getHead());
        }
        return temp;
    }
}
