package d2.teamproject.algorithm.search;

import java.util.Comparator;

/**
 * Compares two {@link Node}{@code s} according to their f() value
 *
 * @param <E> Element represented by the {@link Node}
 */
public class NodeComparer<E> implements Comparator<Node<E>> {
    @Override
    public int compare(Node<E> n1, Node<E> n2) {
        return (int) (n1.getF() - n2.getF());
    }
}