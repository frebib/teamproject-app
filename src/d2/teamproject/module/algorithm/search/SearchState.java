package d2.teamproject.module.algorithm.search;

import d2.teamproject.module.algorithm.search.datastructures.BaseDataStructure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A state to represent the state of search from {@link SearchStream}
 * at any given point in time. Provides a look into all data that represents the state
 * @param <E> Element that nodes in search represent
 * @param <L> A frontier set that changes how the search is performed
 */
public class SearchState<E, L extends BaseDataStructure<Node<E>>> {
    private boolean isComplete = false;
    private final L frontier;
    private final Set<Node<E>> visited;
    private final List<Node<E>> path;

    public SearchState(L frontier, Set<Node<E>> visited, List<Node<E>> path) {
        this.frontier = (L) frontier.copy();
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
     * Gets the frontier {@link BaseDataStructure} from the search
     */
    public L getFrontier() {
        return frontier;
    }

    /**
     * Gets the visited set from the search
     */
    public Set<Node<E>> getVisited() {
        return visited;
    }

    /**
     * Gets the path found by the search.
     * @return a path or null if {@code isComplete()} returns false
     */
    public List<Node<E>> getPath() {
        return path;
    }
}
