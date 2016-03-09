package d2.teamproject.algorithm.search;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Node that carries a contents and it's successors
 * @param <E> Payload of the {@link Node}
 */
public class Node<E> {
    private final List<Node<E>> successors;
    private double heuristic, cost;

    /**
     * The Object that the {@link Node} represents
     */
    protected E contents;

    /**
     * Creates a new Node with a contents
     * @param contents Object for the Node to represent
     */
    public Node(E contents) {
        this.contents = contents;
        this.successors = new LinkedList<>();

        this.cost = Float.POSITIVE_INFINITY;
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
     * Gets the stored {@code Cost} for searching
     * @return The {@code Cost} value
     */
    public double getCost() {
        return cost;
    }

    /**
     * Gets the {@code Heuristic} value for searching
     * @return The {@code Heuristic} value
     */
    public double getHeuristic() {
        return heuristic;
    }

    /**
     * Gets the combined value of {@code cost} and {@code heuristic} values
     * Explanation: https://en.wikipedia.org/wiki/A*_search_algorithm#Description
     * @return {@code cost} + {@code heuristic}
     */
    public double getF() {
        return cost + heuristic;
    }

    /**
     * Sets the {@code cost} value
     * @param cost {@code Cost} value
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Sets the {@code Heuristic} value
     * @param heuristic {@code Heuristic} value
     */
    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Compares the contents to Element {@code c}
     * @param c Element to compare to {@link Node} contents
     * @return true if the contents are .equal()
     */
    public boolean contentsEquals(E c) {
        return contents.equals(c);
    }

    @Override
    public String toString() {
        return "Node{" + contents + '}';
    }
}
