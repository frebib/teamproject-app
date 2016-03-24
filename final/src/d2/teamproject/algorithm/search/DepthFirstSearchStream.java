package d2.teamproject.algorithm.search;

import d2.teamproject.algorithm.search.datastructures.SearchStack;

import java.util.function.BiFunction;

/**
 * Implements a {@link SearchStream} using Depth-First search
 * @param <E> @inheritDoc
 *
 * @author Joseph Groocock
 */
public class DepthFirstSearchStream<E> extends SearchStream<E> {
    public DepthFirstSearchStream(Node<E> start, Node<E> goal) {
        super(new SearchStack<>(), start, goal);
        this.setHeuristicFn(null);
    }

    /**
     * Does nothing. Depth-First search uses no heuristic function
     * @param heuristicFn pointless parameter
     *
     * @return the {@code DepthFirstSearchStream} instance
     */
    @Override
    public SearchStream<E> setHeuristicFn(BiFunction<E, E, Double> heuristicFn) {
        return super.setHeuristicFn((a, b) -> 0d);
    }
}