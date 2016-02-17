package d2.teamproject.test;

import java.util.LinkedHashMap;
import java.util.Map;

import d2.teamproject.algorithm.search.Node;

public class Graph<A> {
	// Keep the implementation of maps open, by using the Map interface:
	private Map<A, Node<A>> nodes;

	public Graph() {
		nodes = new LinkedHashMap<A, Node<A>>();
	}

	/**
	 *
	 * @param c 
	 * @return A 
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

	public Map<A, Node<A>> getNodes() {
		return nodes;
	}
}