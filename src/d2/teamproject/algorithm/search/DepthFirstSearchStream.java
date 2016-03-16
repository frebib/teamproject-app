package d2.teamproject.algorithm.search;

import d2.teamproject.algorithm.search.Node;
import d2.teamproject.algorithm.search.SearchStream;
import d2.teamproject.algorithm.search.datastructures.SearchStack;

public class DepthFirstSearchStream<E> extends SearchStream<E> {
    public DepthFirstSearchStream(Node<E> start, Node<E> goal) {
        super(new SearchStack<>(), start, goal);
    }
}