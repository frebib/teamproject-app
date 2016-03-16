package d2.teamproject.algorithm.search;

import d2.teamproject.algorithm.search.Node;
import d2.teamproject.algorithm.search.SearchStream;
import d2.teamproject.algorithm.search.datastructures.SearchPriorityQueue;

import java.util.function.BiFunction;

public class DijkstraSearchStream<E> extends SearchStream<E> {
    public DijkstraSearchStream(Node<E> start, Node<E> goal) {
        super(null, start, goal);
        setFrontier(new SearchPriorityQueue<>((a, b) -> (int) (getF(a) - getF(b))));
        this.setHeuristicFn(null);
    }

    /**
     * Does nothing. Dijkstras search uses no heuristic function
     * @param heuristicFn pointless parameter
     * @return the {@code DijkstraSearchStream} instance
     */
    @Override
    public SearchStream<E> setHeuristicFn(BiFunction<E, E, Double> heuristicFn) {
        return super.setHeuristicFn((a, b) -> 0d);
    }
}