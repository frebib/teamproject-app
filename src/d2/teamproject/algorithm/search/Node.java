package d2.teamproject.algorithm.search;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Node that carries a contents and it's successors
 * @param <E> Payload of the {@link Node}
 *
 * @author Joseph Groocock
 */
public class Node<E> {
    private final List<Node<E>> successors;

    /**
     * The Object that the {@link Node} represents
     */
    protected E contents;

    public Node(E contents) {
        this.contents = contents;
        this.successors = new LinkedList<>();
    }
    /**
     * Creates a new Node with a contents
     */
    protected Node() {
        this.successors = new LinkedList<>();
    }

    /**
     * Adds a successor
     * @param s Successor to the {@link Node}
     */
    public void addSuccessor(Node<E> s) {
        if (!successors.contains(s))
            successors.add(s);
    }

    /**
     * Gets a {@link List} of all successors
     * @return The successors to the {@link Node}
     */
    public List<Node<E>> getSuccessors() {
        return successors;
    }

    /**
     * Gets the contents that the {@code Node} represents
     * @return the contents
     */
    public E getContents() {
        return contents;
    }

    /**
     * Compares the contents to Element {@code c}
     * @param c Element to compare to {@link Node} contents
     *
     * @return true if the contents are .equal()
     */
    public boolean contentsEquals(Node<E> c) {
        return contents.equals(c.contents);
    }

    @Override
    public String toString() {
        return "Node{" + contents + '}';
    }
}
