package d2.teamproject.module.algorithm.search.datastructures;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Stores a {@code Collection} of items in a Stack
 * {@link BaseDataStructure}
 * The implementation actually uses a {@link java.util.ArrayDeque} in place of a
 * {@link java.util.Stack}
 *
 * @param <A>
 *            @inheritDoc
 */
public class SearchStack<A> extends ArrayDeque<A> implements BaseDataStructure<A> {
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
	public BaseDataStructure<A> copy() {
		SearchStack<A> temp = new SearchStack<A>(this);
		SearchStack<A> temp2 = new SearchStack<A>(this);
		temp.clear();
		for (int i = this.size() - 1; i >= 0; i--) {
			temp.add(temp2.getHead());
		}
		return temp;
	}
}
