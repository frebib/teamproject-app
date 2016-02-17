package d2.teamproject.test;

import java.util.LinkedHashMap;
import java.util.Map;

import d2.teamproject.algorithm.search.Node;

public class Graph<A> {
	// Keep the implementation of maps open, by using the Map interface:
	private Map<A, Node<A>> nodes;

	/**
	 * Constructs a new empty {@link Graph}
	 */
	public Graph() {
		nodes = new LinkedHashMap<A, Node<A>>();
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
		else {
			node = new Node<A>(c);
			nodes.put(c, node);
		}
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