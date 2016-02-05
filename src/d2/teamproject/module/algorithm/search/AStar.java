package d2.teamproject.module.algorithm.search;

import d2.teamproject.module.algorithm.search.datastructures.SearchPriorityQueue;

import java.util.List;

/**
 * Searches a Node collection using A* @link{d2.teamproject.module.algorithm.search}
 */

public class AStar {
    /**
     * Finds a Node in a {@link Graph} using A* Search
     *
     * @param start     The {@link Node} to start searching from
     * @param goal      The {@link Node} to searching for
     * @param heuristic A Heuristic to estimate the distance to the goal {@link Node}
     * @param cost      A Cost function to calculate the precise distance from the previous {@link Node}
     * @return a {@link Node} from {@link Node} {@code start} to {@link Node} {@code goal}
     */
    public static <A> Node<A> findNodeFrom(Node<A> start, Node<A> goal, SearchFunction<A> heuristic, SearchFunction<A> cost) {
        return Search.findNodeFrom(start, goal, new SearchPriorityQueue<Node<A>>(new NodeComparer<A>()), heuristic, cost);
    }

    /**
     * Finds a path between connected {@link Node}{@code s} using A* Search
     *
     * @param start     The {@link Node} to start path-finding from
     * @param goal      The {@link Node} to searching for
     * @param heuristic A Heuristic to estimate the distance to the goal {@link Node}
     * @param cost      A Cost function to calculate the precise distance from the previous {@link Node}
     * @return a Path from {@link Node} {@code start} to {@link Node} {@code goal}
     */
    public static <A> List<Node<A>> findPathFrom(Node<A> start, Node<A> goal, SearchFunction<A> heuristic, SearchFunction<A> cost) {
        return Search.findPathFrom(start, goal, new SearchPriorityQueue<Node<A>>(new NodeComparer<A>()), heuristic, cost);
    }
}