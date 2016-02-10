package d2.teamproject.module.algorithm.search;

import d2.teamproject.module.algorithm.search.datastructures.SearchPriorityQueue;

public class AStarSearchStream<E> extends SearchStream<E, SearchPriorityQueue<Node<E>>> {
    public AStarSearchStream(Node<E> start, Node<E> goal) {
        super(new SearchPriorityQueue<>(new NodeComparer<>()), start, goal);
        // TODO: Set proper AStarSearchStream functions
        this.setCostFn((a, b) -> 0f);
        this.setHeuristicFn((a, b) -> 0f);
    }
}
