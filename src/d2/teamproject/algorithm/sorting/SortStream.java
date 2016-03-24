package d2.teamproject.algorithm.sorting;

import d2.teamproject.algorithm.AlgoStream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Provides a base stream implementation to manage state I/O
 * @param <E> Type of data stored in the list
 * @author Joseph Groocock
 */
public abstract class SortStream<E> implements AlgoStream<SortState<E>> {
    public static class Sorter<E> {
        private BiFunction<List<E>, Comparator<E>, SortStream<E>> constructor;
        private String name;

        public Sorter(BiFunction<List<E>, Comparator<E>, SortStream<E>> constructor, String name) {
            this.constructor = constructor;
            this.name = name;
        }

        public SortStream<E> get(List<E> list, Comparator<E> comparator) {
            return constructor.apply(list, comparator);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    protected final ArrayList<E> list;
    protected final Comparator<E> comparator;
    protected final ArrayList<SortState<E>> states;

    private int stateIndex = 0;

    public SortStream(List<E> list, Comparator<E> comparator) {
        this.list = new ArrayList<>(list);
        this.comparator = comparator;
        states = new ArrayList<>();
    }

    @Override
    public abstract SortStream<E> initialise();

    /**
     * Gets the sorted list from the output of the algorithm
     * Equally, getting the list from the last state would return a copy of the same list
     * @return the sorted list
     */
    public List<E> getSortedList() {
        return list;
    }

    @Override
    public SortState<E> getCurrent() {
        return states.get(stateIndex);
    }

    @Override
    public SortState<E> getNext() {
        if (hasNext())
            return states.get(++stateIndex);
        return null;
    }

    @Override
    public SortState<E> getPrevious() {
        if (hasPrevious())
            return states.get(--stateIndex);
        return null;
    }

    @Override
    public SortState<E> getNth(int n) {
        if (hasNth(n))
            return states.get(n);
        return null;
    }

    /**
     * @return gets the index of the currently active state
     */
    public int getStateIndex() {
        return stateIndex;
    }

    @Override
    public boolean hasNext() {
        return stateIndex + 1 < states.size();
    }

    @Override
    public boolean hasPrevious() {
        return stateIndex > 0;
    }

    @Override
    public boolean hasNth(int n) {
        return n >= 0 && n < states.size();
    }

    @Override
    public List<SortState<E>> getAll() {
        return states;
    }
}