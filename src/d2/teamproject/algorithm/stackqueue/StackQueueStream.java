package d2.teamproject.algorithm.stackqueue;

import d2.teamproject.algorithm.AlgoStream;
import d2.teamproject.algorithm.search.datastructures.BaseDataStructure;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class StackQueueStream<T, L extends BaseDataStructure<T>> implements
		AlgoStream<L> {
	// TODO: Remove unused & deprecated classes

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
	public L getNext() {
		current.getHead();
		L permCur = (L) current.copy();
		states.add(permCur);
		return (L) current.copy();
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

	// TODO: Implement!
	@Override
	public L getNth(int n) {
		return null;
	}

	@Override
	public boolean hasNth(int n) {
		return false;
	}

	@Override
	public List<L> getAll() {
		return null;
	}
}
