package d2.teamproject.module.algorithm.stackqueue;

import java.util.ArrayList;

import d2.teamproject.module.algorithm.AlgoStream;
import d2.teamproject.module.algorithm.search.datastructures.BaseDataStructure;

public class StackQueueStream<T, L extends BaseDataStructure<T>> implements
		AlgoStream<L> {

	private L current;
	private ArrayList<L> states;

	public StackQueueStream(L sq) {
		this.current = sq;
	}

	@Override
	public void initialise() {
		states = new ArrayList<L>();
		L permCur = (L) current.copy();
		states.add(permCur);
	}

	@Override
	public void reset() {
		states.clear();
		states.add(current);
	}

	@Override
	public L getNext() {
		L temp = (L) current.copy();
		current.getHead();
		L permCur = (L) current.copy();
		states.add(permCur);
		return temp;
	}

	public void add(T item) {
		current.add(item);
		L permCur = (L) current.copy();
		states.add(permCur);
	}

	@Override
	public L getPrevious() {
		if (!hasPrevious())
			return null;
		return states.get(states.size() - 2);
	}

	@Override
	public boolean hasNext() {
		return !current.isEmpty();
	}

	@Override
	public boolean hasPrevious() {
		return states.size() > 0;
	}
}
