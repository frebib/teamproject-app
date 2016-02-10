package d2.teamproject.module.algorithm.search;

import d2.teamproject.module.algorithm.search.datastructures.BaseDataStructure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public boolean isComplete() {
        return isComplete;
    }

    public L getFrontier() {
        return frontier;
    }

    public Set<Node<E>> getVisited() {
        return visited;
    }

    public List<Node<E>> getPath() {
        return path;
    }
}
