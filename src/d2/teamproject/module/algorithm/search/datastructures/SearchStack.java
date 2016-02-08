package d2.teamproject.module.algorithm.search.datastructures;

import java.util.ArrayDeque;
import java.util.Collection;

/**
 * Stores a {@code Collection} of items in a Stack {@link d2.teamproject.module.algorithm.search.datastructures.DataStructure} The implementation actually uses a {@link java.util.ArrayDeque} in place of a {@link java.util.Stack}
 *
 * @param <A> @inheritDoc
 */
public class SearchStack<A> extends ArrayDeque<A> implements DataStructure<A> {
    public SearchStack(Collection<? extends A> c) {
        super(c);
    }

    @Override
    public A getHead() {
        return pop();
    }

    @Override
    public boolean add(A a) {
        push(a);
        return true;
    }
    
    @Override
	public DataStructure<A> copy() {
		return new SearchStack<A>(this);
	}

}
