package d2.teamproject.algorithm.search;

import d2.teamproject.algorithm.search.datastructures.SearchPriorityQueue;

/**
 * Implements a {@link SearchStream} using A* search
 * @param <E> @inheritDoc
 */
public class AStarSearchStream<E> extends SearchStream<E> {
    public AStarSearchStream(Node<E> start, Node<E> goal) {
        super(null, start, goal);
        setFrontier(new SearchPriorityQueue<>((a, b) -> (int) (getF(a) - getF(b))));
    }
}