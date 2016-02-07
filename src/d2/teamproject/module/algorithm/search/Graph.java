package d2.teamproject.module.algorithm.search;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a Collection of {@link Node}{@code s} linked to their contents
 *
 * We represent a graph as table of pairs (contents, node with that contents).
 * This assumes that each node has a unique contents.
 * This is a minimal class so that a graph can be created.
 *
 * @param <A> Element stored in the {@link Node}{@code s}
 */
public class Graph<A> {
    // Keep the implementation of maps open, by using the Map interface:
    private Map<A, Node<A>> nodes;

    /**
     * Constructs a new empty {@link Graph}
     */
    public Graph() {
        nodes = new LinkedHashMap<>();
    }

    /**
     * Find or create {@link Node} with a given contents c
     *
     * @param c Contents of the {@link Node} to find
     * @return A {@link Node} with contents c
     */
    public Node<A> nodeWith(A c) {
        Node<A> node; // Deliberately uninitialized.
        if (nodes.containsKey(c))
            node = nodes.get(c);
        else
            nodes.put(c, node = new Node<A>(c));
        return node;
    }

    /**
     * Gets {@link Node}{@code s} in the {@link Graph}
     *
     * @return {@link Node}{@code s} from the {@link Graph}
     */
    public Map<A, Node<A>> getNodes() {
        return nodes;
    }
}
