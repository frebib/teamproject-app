package d2.teamproject.module.algorithm.search;

import java.util.Comparator;

/**
 * Compares two {@link Node}{@code s} according to their f() value
 *
 * @param <A> Element represented by the {@link Node}
 */
public class NodeComparer<A> implements Comparator<Node<A>> {
    @Override
    public int compare(Node<A> n1, Node<A> n2) {
        return (int) (n1.getF() - n2.getF());
    }
}