package d2.teamproject.algorithm.search;

import d2.teamproject.algorithm.AlgoStream;
import d2.teamproject.algorithm.search.datastructures.SearchCollection;

import java.util.*;
import java.util.function.BiFunction;

/**
 * @param <E>
 *
 * @author Joseph Groocock
 */
public abstract class SearchStream<E> implements AlgoStream<SearchState<Node<E>>> {
    public static class Searcher<E> {
        private BiFunction<Node<E>, Node<E>, SearchStream<E>> constructor;
        private Class clazz;
        private String name;

        public Searcher(BiFunction<Node<E>, Node<E>, SearchStream<E>> constructor, Class clazz, String name) {
            this.constructor = constructor;
            this.clazz = clazz;
            this.name = name;
        }

        public SearchStream<E> get(Node<E> start, Node<E> goal) {
            return constructor.apply(start, goal);
        }

        public Class getClazz() {
            return clazz;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private Node<E> start, goal;
    private BiFunction<E, E, Double> costFn, heuristicFn;

    private SearchCollection<Node<E>> frontier;
    private final Set<Node<E>> visited;
    private final Map<Node<E>, Node<E>> successors;
    private final Map<Node<E>, Double> costMap, heuristicMap;

    private final List<SearchState<Node<E>>> allStates;

    public SearchStream(SearchCollection<Node<E>> frontier, Node<E> start, Node<E> goal) {
        this.start = start;
        this.goal = goal;

        this.frontier = frontier;
        this.visited = new HashSet<>();
        this.successors = new LinkedHashMap<>();
        this.costMap = new LinkedHashMap<>();
        this.heuristicMap = new LinkedHashMap<>();

        this.allStates = new ArrayList<>();
    }

    public Node<E> getStart() {
        return start;
    }

    public Node<E> getGoal() {
        return goal;
    }

    protected SearchStream<E> setFrontier(SearchCollection<Node<E>> frontier) {
        this.frontier = frontier;
        return this;
    }

    /**
     * Sets the start location for search
     * @param start the start location
     */
    public SearchStream<E> setStart(Node<E> start) {
        this.start = start;
        return this;
    }
    /**
     * Sets the start location for search
     * @param goal the goal location
     */
    public SearchStream<E> setGoal(Node<E> goal) {
        this.goal = goal;
        return this;
    }
    /**
     * Sets the cost function for the search, changing the behaviour of the search algorithm
     * @param costFn
     *
     * @return the SearchStream object
     */
    public SearchStream<E> setCostFn(BiFunction<E, E, Double> costFn) {
        this.costFn = costFn;
        return this;
    }
    /**
     * Sets the heuristic function for the search, changing the behaviour of the search algorithm
     * @return the SearchStream object
     */
    public SearchStream<E> setHeuristicFn(BiFunction<E, E, Double> heuristicFn) {
        this.heuristicFn = heuristicFn;
        return this;
    }

    /**
     * Runs the graph search and generates the entire search tree.
     */
    @Override
    public SearchStream<E> initialise() {
        frontier.clear();
        visited.clear();
        successors.clear();
        allStates.clear();

        setHeuristic(start, heuristicFn.apply(start.getContents(), goal.getContents()));
        setCost(start, 0);
        frontier.add(start);
        return this;
    }

    @Override
    public SearchState<Node<E>> getNext() {
        return genNextState();
    }

    @Override
    public SearchState<Node<E>> getPrevious() {
        if (!hasPrevious()) // Prevent a crash if no previous state
            return null;
        return allStates.get(allStates.size() - 1);
    }

    @Override
    public SearchState<Node<E>> getCurrent() {
        // TODO: Sort this crap out. It's not good
        return getPrevious();
    }

    @Override
    public SearchState<Node<E>> getNth(int n) {
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
        return hasPrevious() ? !getPrevious().isComplete() : start != null && goal != null && !frontier.isEmpty();
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
    public List<SearchState<Node<E>>> getAll() {
        while (hasNext()) genNextState();
        return allStates;
    }

    public double getCost(Node<E> node) {
        return (costMap.containsKey(node)) ? costMap.get(node) : 0;
    }

    public double getHeuristic(Node<E> node) {
        return (heuristicMap.containsKey(node)) ? heuristicMap.get(node) : 0;
    }

    public double getF(Node<E> node) {
        return getCost(node) + getHeuristic(node);
    }

    private void setHeuristic(Node<E> node, double heuristic) {
        heuristicMap.put(node, heuristic);
    }

    private void setCost(Node<E> node, double cost) {
        costMap.put(node, cost);
    }

    private SearchState<Node<E>> genNextState() {
        if (costFn == null || heuristicFn == null)
            throw new NullPointerException("Cost and Heuristic functions must be set!");

        if (!hasNext()) return null;

        Node<E> node = frontier.getHead();
        visited.add(node);

        if (node.contentsEquals(goal)) {     // At this point we reconstruct the path followed from the visited Map
            successors.put(start, null);              // Add start Node as it will be first element in list (last one to be added)

            List<Node<E>> path = new ArrayList<>();
            while (node != null) {                    // Iterate through the nodes in the visited map
                path.add(node);                       // Add the current node to the resulting path
                node = successors.get(node);          // Get the parent of the node from the Key-Value
            }                                         // pair in the Map using the node as the key
            Collections.reverse(path);

            // Return the final state with a path
            SearchState<Node<E>> state = new SearchState<>(frontier, visited, path);
            allStates.add(state);
            return state;
        } else {
            for (Node<E> suc : node.getSuccessors()) {
                if (visited.contains(suc))
                    continue;
                double costVal = getCost(node) + costFn.apply(node.getContents(), suc.getContents());
                if (!frontier.contains(suc) || costVal < getCost(suc)) {
                    setHeuristic(suc, heuristicFn.apply(suc.getContents(), goal.getContents()));
                    setCost(suc, costVal);

                    successors.put(suc, node);    // Set the node as visited
                    frontier.add(suc);            // Add successor to frontier to allow it to be searched from
                }
            }
        }
        SearchState<Node<E>> state = new SearchState<>(frontier, visited, null);
        allStates.add(state);
        return state;
    }
}
