package d2.teamproject.algorithm.search;

import d2.teamproject.algorithm.search.datastructures.SearchCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A state to represent the state of search from {@link SearchStream}
 * at any given point in time. Provides a look into all data that represents the state
 * @param <E> Element that nodes in search represent
 */
public class SearchState<E> {
    private boolean isComplete = false;
    private final SearchCollection<E> frontier;
    private final Set<E> visited;
    private final List<E> path;

    public SearchState(SearchCollection<E> frontier, Set<E> visited, List<E> path) {
        this.frontier = frontier.copy();
        this.visited = new HashSet<>(visited);
        this.path = path;
        this.isComplete = (path != null);
    }

    /**
     * Gets whether the path search has finished
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Gets the frontier {@link SearchCollection} from the search
     */
    public SearchCollection<E> getFrontier() {
        return frontier;
    }

    /**
     * Gets the visited set from the search
     */
    public Set<E> getVisited() {
        return visited;
    }

    /**
     * Gets the path found by the search.
     * @return a path or null if {@code isComplete()} returns false
     */
    public List<E> getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "SearchState{" +
                "isComplete=" + isComplete +
                ", frontier=" + frontier +
                ", visited=" + visited +
                ", path=" + path +
                '}';
    }
}