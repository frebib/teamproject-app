package d2.teamproject.module.algorithm.search;

import d2.teamproject.module.algorithm.search.datastructures.BaseDataStructure;

import java.util.*;

/**
 * A class to conduct different types of d2.teamproject.module.algorithm.search on a given data structure
 */

public class Search {
    /**
     * Finds a Node in a {@link Graph}
     *
     * @param start     The {@link Node} to start searching from
     * @param goal      The {@link Node} to searching for
     * @param frontier  A {@link d2.teamproject.module.algorithm.search.datastructures.DataStructure} to store the frontier set of the {@link Search}
     * @param heuristic A Heuristic to estimate the distance to the goal {@link Node}
     * @param cost      A Cost function to calculate the precise distance from the previous {@link Node}
     * @return a {@link Node} from {@link Node} {@code start} to {@link Node} {@code goal}
     */
    public static <A, B extends DataStructure<Node<A>>> Node<A> findNodeFrom(Node<A> start, Node<A> goal, B frontier, SearchFunction<A> heuristic, SearchFunction<A> cost) {
        Set<Node<A>> visited = new HashSet<>();
        Node<A> node = null;

        if (start.contentsEquals(goal.contents))
            return null;

        start.setHeuristic(heuristic.apply(start, goal));
        start.setCost(0);
        frontier.add(start);                        // Adds the node to the frontier in the manner specified by the data structure

        while (!frontier.isEmpty()) {
            node = frontier.getHead();                // Get and remove the first element in the manner specified by the data structure
            if (node.contentsEquals(goal.contents))
                return node;        // Return found goal Node
            else
                for (Node<A> suc : node.getSuccessors())
                    if (!visited.contains(suc)) {
                        float costVal = node.getCost() + cost.apply(node, suc);

                        suc.setHeuristic(heuristic.apply(suc, goal));
                        suc.setCost(costVal);

                        frontier.add(suc);                // Add all successors to the frontier set so they can
                        visited.add(suc);                // be searched on a later iteration of this while loop
                    }
        }
        return node;
    }

    /**
     * Finds a path between connected {@link Node}{@code s}
     *
     * @param start     The {@link Node} to start path-finding from
     * @param goal      The {@link Node} to searching for
     * @param frontier  A {@link BaseDataStructure} to store the frontier set of the {@link Search}
     * @param heuristic A Heuristic to estimate the distance to the goal {@link Node}
     * @param cost      A Cost function to calculate the precise distance from the previous {@link Node}
     * @return a Path from {@link Node} {@code start} to {@link Node} {@code goal}
     */
    public static <A, B extends BaseDataStructure<Node<A>>> List<Node<A>> findPathFrom(Node<A> start, Node<A> goal, B frontier, SearchFunction<A> heuristic, SearchFunction<A> cost) {
        Map<Node<A>, Node<A>> successors = new LinkedHashMap<>();
        Set<Node<A>> visited = new HashSet<>();
        Node<A> node = null;

        if (start.contentsEquals(goal.contents))
            return null;

        start.setHeuristic(heuristic.apply(start, goal));
        start.setCost(0);
        frontier.add(start);

        while (!frontier.isEmpty()) {
            node = frontier.getHead();
            visited.add(node);
            if (node.contentsEquals(goal.contents)) {     // At this point we reconstruct the path followed from the visited Map
                successors.put(start, null);              // Add start Node as it will be first element in list (last one to be added)

                List<Node<A>> list = new ArrayList<>();
                while (node != null) {                    // Iterate through the nodes in the visited map
                    list.add(node);                       // Add the current node to the resulting path
                    node = successors.get(node);          // Get the parent of the node from the Key-Value
                }                                         // pair in the Map using the node as the key

                assert (list.size() > 1);                 // It should never be that the only node in the list
                return list;                              // is the start node; that should catch at the start.
            } else
                for (Node<A> suc : node.getSuccessors())
                    if (!visited.contains(suc)) {
                        float costVal = node.getCost() + cost.apply(node, suc);
                        if (!frontier.contains(suc) || costVal < suc.getCost()) {
                            suc.setHeuristic(heuristic.apply(suc, goal));
                            suc.setCost(costVal);

                            successors.put(suc, node);    // Set the node as visited
                            frontier.add(suc);            // Add successor to frontier to allow it to be searched from
                        }
                    }
        }
        return null;
    }
}
