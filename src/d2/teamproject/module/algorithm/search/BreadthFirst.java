package d2.teamproject.module.algorithm.search;

import d2.teamproject.module.algorithm.search.datastructures.SearchQueue;

import java.util.List;

/**
 * Searches a Node collection using Breadth-First @link{d2.teamproject.module.algorithm.search}
 */

public class BreadthFirst {
    /**
     * Finds a Node in a {@link Graph} using Breadth First Search
     *
     * @param start The {@link Node} to start searching from
     * @param goal  The {@link Node} to searching for
     * @return a {@link Node} from {@link Node} {@code start} to {@link Node} {@code goal}
     */
    public static <A> Node<A> findNodeFrom(Node<A> start, Node<A> goal) {
        SearchFunction<A> nothing = (a, b) -> 0f;
        return Search.findNodeFrom(start, goal, new SearchQueue<Node<A>>(), nothing, nothing);
    }

    /**
     * Finds a path between connected {@link Node}{@code s} using Breadth First Search
     *
     * @param start The {@link Node} to start path-finding from
     * @param goal  The {@link Node} to searching for
     * @return a Path from {@link Node} {@code start} to {@link Node} {@code goal}
     */
    public static <A> List<Node<A>> findPathFrom(Node<A> start, Node<A> goal) {
        SearchFunction<A> nothing = (a, b) -> 0f;
        return Search.findPathFrom(start, goal, new SearchQueue<Node<A>>(), nothing, nothing);
    }
}