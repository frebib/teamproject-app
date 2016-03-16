package d2.teamproject.algorithm.search;

import d2.teamproject.algorithm.search.Node;
import d2.teamproject.algorithm.search.SearchStream;
import d2.teamproject.algorithm.search.datastructures.SearchQueue;

public class BreadthFirstSearchStream<E> extends SearchStream<E> {
    public BreadthFirstSearchStream(Node<E> start, Node<E> goal) {
        super(new SearchQueue<>(), start, goal);
    }
}