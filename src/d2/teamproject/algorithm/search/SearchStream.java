package d2.teamproject.algorithm.search;

import d2.teamproject.algorithm.AlgoStream;
import d2.teamproject.algorithm.search.datastructures.BaseDataStructure;

import java.util.*;

public abstract class SearchStream<E, L extends BaseDataStructure<Node<E>>> implements AlgoStream<SearchState<E, L>> {

//    /**
//     * A {@link SearchFunction} that calculates a Euclidean distance between two {@link Node}{@code s}
//     */
//    public static final SearchFunction<E> euclidean = (a, b) -> (float) Math.sqrt(
//            Math.pow(a.contents.getX() - b.contents.getX(), 2) +
//            Math.pow(a.contents.getY() - b.contents.getY(), 2));
//    /**
//     * A {@link SearchFunction} that calculates a Manhattan distance between two {@link Node}{@code s}
//     */
//    public static final SearchFunction<E> manhattan = (a, b) -> (float)
//           (Math.abs(a.contents.getX() - b.contents.getX()) +
//            Math.abs(a.contents.getY() - b.contents.getY()));

    private Node<E> start, goal;
    private SearchFunction<E> cost, heuristic;

    private final L frontier;
    private final Set<Node<E>> visited;
    private final Map<Node<E>, Node<E>> successors;

    private final List<SearchState<E, L>> allStates;

    public SearchStream(L frontier) {
        this(frontier, null, null);
    }

    public SearchStream(L frontier, Node<E> start, Node<E> goal) {
        this.start = start;
        this.goal = goal;

        this.frontier = frontier;
        this.visited = new HashSet<>();
        this.successors = new LinkedHashMap<>();

        this.allStates = new ArrayList<>();
    }

    /**
     * Sets the start location for search
     * @param start the start location
     */
    public SearchStream<E, L> setStart(Node<E> start) {
        this.start = start;
        return this;
    }
    /**
     * Sets the start location for search
     * @param goal the goal location
     */
    public SearchStream<E, L> setGoal(Node<E> goal) {
        this.goal = goal;
        return this;
    }
    /**
     * Sets the cost function for the search, changing the behaviour of the search algorithm
     * @return the SearchStream object
     */
    public SearchStream<E, L> setCostFn(SearchFunction<E> cost) {
        this.cost = cost;
        return this;
    }
    /**
     * Sets the heuristic function for the search, changing the behaviour of the search algorithm
     * @return the SearchStream object
     */
    public SearchStream<E, L> setHeuristicFn(SearchFunction<E> heuristic) {
        this.heuristic = heuristic;
        return this;
    }

    /**
     * Runs the graph search and generates the entire search tree.
     */
    @Override
    public void initialise() {
        frontier.clear();
        visited.clear();
        successors.clear();

        allStates.clear();

        start.setHeuristic(heuristic.apply(start, goal));
        start.setCost(0);
        frontier.add(start);
    }

    @Override
    public void reset() {
        initialise();
    }

    @Override
    public SearchState<E, L> getNext() {
        return genNextState();
    }

    @Override
    public SearchState<E, L> getPrevious() {
        if (allStates.size() < 2) // Prevent a crash if no previous state
            return null;
        return allStates.get(allStates.size() - 1);
    }

    @Override
    public SearchState<E, L> getNth(int n) {
        if (n < 0 || n > allStates.size())
            return null;
        if (n == allStates.size())
            return getNext();
        return allStates.get(n);
    }

    @Override
    public boolean hasNext() {
        // Has a next value unless the path has reached the
        // goal or there are no more nodes to explore
        return !((start != null && goal != null && start.contentsEquals(goal.contents)) || frontier.isEmpty());
    }

    @Override
    public boolean hasPrevious() {
        return !allStates.isEmpty();
    }

    @Override
    public boolean hasNth(int n) {
        return (n > 0 && allStates.size() > n || (hasNext() && n == allStates.size()));
    }
    @Override
    public List<SearchState<E, L>> getAll() {
        return allStates;

    }

    private SearchState<E, L> genNextState() {
        if (cost == null || heuristic == null)
            throw new NullPointerException("Cost and Heuristic functions must be set!");

        if (!hasNext()) return null;

        Node<E> node = frontier.getHead();
        visited.add(node);

        if (node.contentsEquals(goal.contents)) {     // At this point we reconstruct the path followed from the visited Map
            successors.put(start, null);              // Add start Node as it will be first element in list (last one to be added)

            List<Node<E>> path = new ArrayList<>();
            while (node != null) {                    // Iterate through the nodes in the visited map
                path.add(node);                       // Add the current node to the resulting path
                node = successors.get(node);          // Get the parent of the node from the Key-Value
            }                                         // pair in the Map using the node as the key
            return new SearchState<>(frontier, visited, path);
        }
        else {
            for (Node<E> suc : node.getSuccessors()) {
                if (visited.contains(suc))
                    continue;
                float costVal = node.getCost() + cost.apply(node, suc);
                if (!frontier.contains(suc) || costVal < suc.getCost()) {
                    suc.setHeuristic(heuristic.apply(suc, goal));
                    suc.setCost(costVal);

                    successors.put(suc, node);    // Set the node as visited
                    frontier.add(suc);            // Add successor to frontier to allow it to be searched from
                }
            }
        }
        SearchState<E, L> state = new SearchState<>(frontier, visited, null);
        allStates.add(state);
        return state;
    }
}