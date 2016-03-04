package d2.teamproject.algorithm.search;

import d2.teamproject.algorithm.search.datastructures.SearchPriorityQueue;

public class AStarSearchStream<E> extends SearchStream<E, SearchPriorityQueue<Node<E>>> {
    public AStarSearchStream() {
        this(null, null);
    }
    public AStarSearchStream(Node<E> start, Node<E> goal) {
        super(new SearchPriorityQueue<>(new NodeComparator<>()), start, goal);
    }
}
