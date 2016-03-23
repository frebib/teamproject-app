package d2.teamproject.algorithm.search;

import d2.teamproject.algorithm.search.datastructures.SearchQueue;

import java.util.function.BiFunction;

/**
 * @param <E> @inheritDoc
 *
 * @author Joseph Groocock
 */
public class BreadthFirstSearchStream<E> extends SearchStream<E> {
    public BreadthFirstSearchStream(Node<E> start, Node<E> goal) {
        super(new SearchQueue<>(), start, goal);
        this.setHeuristicFn(null);
    }

    /**
     * Does nothing. Breadth-First search uses no heuristic function
     * @param heuristicFn pointless parameter
     * @return the {@code DepthFirstSearchStream} instance
     */
    @Override
    public SearchStream<E> setHeuristicFn(BiFunction<E, E, Double> heuristicFn) {
        return super.setHeuristicFn((a, b) -> 0d);
    }
}