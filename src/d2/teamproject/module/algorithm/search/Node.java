package d2.teamproject.module.algorithm.search;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Represents a Node that carries a contents and it's successors
 *
 * @param <A> Payload of the {@link Node}
 */
public class Node<A> {
    private final Collection<Node<A>> successors;
    private float heuristic, cost;

    /**
     * The Object that the {@link Node} represents
     */
    public final A contents;

    /**
     * Creates a new Node with a contents
     *
     * @param contents Object for the Node to represent
     */
    public Node(A contents) {
        this.contents = contents;
        this.successors = new LinkedHashSet<Node<A>>();

        this.cost = Float.POSITIVE_INFINITY;
    }

    /**
     * Adds a successor
     *
     * @param s Successor to the {@link Node}
     */
    public void addSuccessor(Node<A> s) {
        successors.add(s);
    }

    /**
     * Gets a {@link Collection} of all successors
     *
     * @return The successors to the {@link Node}
     */
    public Collection<Node<A>> getSuccessors() {
        return successors;
    }

    /**
     * Gets the stored {@code Cost} for searching
     *
     * @return The {@code Cost} value
     */
    public float getCost() {
        return cost;
    }

    /**
     * Gets the {@code Heuristic} value for searching
     *
     * @return The {@code Heuristic} value
     */
    public float getHeuristic() {
        return heuristic;
    }

    /**
     * Gets the combined value of {@code cost} and {@code heuristic} values
     *
     * @return {@code cost} + {@code heuristic}
     */
    public float getF() {
        return cost + heuristic;
    }

    /**
     * Sets the {@code cost} value
     *
     * @param cost {@code Cost} value
     */
    public void setCost(float cost) {
        this.cost = cost;
    }

    /**
     * Sets the {@code Heuristic} value
     *
     * @param heuristic {@code Heuristic} value
     */
    public void setHeuristic(float heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Compares the contents to Element {@code c}
     *
     * @param c Element to compare to {@link Node} contents
     * @return true if the contents are .equal()
     */
    public boolean contentsEquals(A c) {
        return contents.equals(c);
    }

    @Override
    public String toString() {
        return "Node" + contents.toString();
    }
}
