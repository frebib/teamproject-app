package d2.teamproject.test;

import d2.teamproject.algorithm.search.AStarSearchStream;
import d2.teamproject.algorithm.search.Node;
import d2.teamproject.algorithm.search.SearchState;
import d2.teamproject.algorithm.search.SearchStream;
import d2.teamproject.algorithm.search.datastructures.SearchPriorityQueue;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;

public class SearchTest {
    /**
     * A {@link BiFunction} that calculates a Euclidean distance between two {@link Node}{@code s}
     */
    public static final BiFunction<Point, Point, Double> euclidean = (a, b) -> Math.sqrt(
            Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));

    /**
     * A {@link BiFunction} that calculates a Manhattan distance between two {@link Node}{@code s}
     */
    public static final BiFunction<Point, Point, Double> manhattan = (a, b) ->
            Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());

    @Test
    public void hasPathAStar() {
        Graph<Point> graph = sampleGraph();
        Random r = new Random();

        // Pick 2 random points in the graph.
        // Every node has at least 1 path to any other
        List<Node<Point>> val = new ArrayList<>(graph.nodes.values());
        Node<Point> start = val.get(r.nextInt(val.size())),
                    end   = val.get(r.nextInt(val.size()));

        SearchStream<Point, SearchPriorityQueue<Node<Point>>> stream =
                new AStarSearchStream<>(start, end)
                .setCostFn(manhattan)
                .setHeuristicFn(euclidean)
                .initialise();

        SearchState state = null;
        while (stream.hasNext())
            state = stream.getNext();

        // last state should have a path provided the graph does
        Assert.assertNotNull(state.getPath());

        // Should have no more states
        Assert.assertNull(stream.getNext());
    }

    private static class Graph<A> {
        public Map<A, Node<A>> nodes = new LinkedHashMap<>();

        public Node<A> nodeWith(A c) {
            Node<A> node;
            if (nodes.containsKey(c))
                node = nodes.get(c);
            else
                nodes.put(c, node = new Node<>(c));
            return node;
        }
    }

    private static Graph<Point> sampleGraph() {
        int[][] values = new int[][]{
                {0, 0, 1, 0, 0, 1},
                {0, 1, 0, 0, 1, 1, 0, 2},
                {0, 2, 0, 3, 0, 1},
                {0, 3, 0, 2, 0, 4},
                {0, 4, 0, 3, 0, 5},
                {0, 5, 0, 6, 1, 5, 0, 4},
                {0, 6, 1, 6, 0, 5},
                {1, 0, 0, 0, 1, 1, 2, 0},
                {1, 1, 1, 2, 2, 1, 1, 0, 0, 1},
                {1, 2, 2, 2, 1, 1, 1, 3},
                {1, 3, 1, 2, 1, 4, 2, 3},
                {1, 4, 2, 4, 1, 5, 1, 3},
                {1, 5, 1, 4, 2, 5, 1, 6, 0, 5},
                {1, 6, 0, 6, 1, 5, 2, 6},
                {2, 0, 3, 0, 2, 1, 1, 0},
                {2, 1, 2, 2, 1, 1, 2, 0, 3, 1},
                {2, 2, 1, 2, 2, 1, 2, 3, 3, 2},
                {2, 3, 2, 2, 2, 4, 3, 3, 1, 3},
                {2, 4, 1, 4, 2, 5, 2, 3, 3, 4},
                {2, 5, 2, 4, 1, 5, 2, 6, 3, 5},
                {2, 6, 3, 6, 2, 5, 1, 6},
                {3, 0, 2, 0, 3, 1},
                {3, 1, 3, 0, 4, 1, 2, 1, 3, 2},
                {3, 2, 2, 2, 4, 2, 3, 1},
                {3, 3, 2, 3, 3, 4},
                {3, 4, 2, 4, 3, 3},
                {3, 5, 3, 6, 2, 5, 4, 5},
                {3, 6, 2, 6, 3, 5},
                {4, 0},
                {4, 1, 4, 2, 5, 1, 3, 1},
                {4, 2, 4, 1, 5, 2, 3, 2},
                {4, 3},
                {4, 4},
                {4, 5, 5, 5, 3, 5},
                {4, 6},
                {5, 0},
                {5, 1, 4, 1, 5, 2, 6, 1},
                {5, 2, 4, 2, 5, 1, 6, 2},
                {5, 3},
                {5, 4},
                {5, 5, 4, 5, 6, 5},
                {5, 6},
                {6, 0, 7, 0, 6, 1},
                {6, 1, 6, 0, 5, 1, 6, 2, 7, 1},
                {6, 2, 5, 2, 6, 1, 7, 2},
                {6, 3, 7, 3, 6, 4},
                {6, 4, 6, 3, 7, 4},
                {6, 5, 5, 5, 6, 6, 7, 5},
                {6, 6, 7, 6, 6, 5},
                {7, 0, 6, 0, 7, 1, 8, 0},
                {7, 1, 8, 1, 7, 0, 6, 1, 7, 2},
                {7, 2, 7, 3, 8, 2, 6, 2, 7, 1},
                {7, 3, 6, 3, 7, 2, 7, 4, 8, 3},
                {7, 4, 7, 3, 8, 4, 6, 4, 7, 5},
                {7, 5, 8, 5, 7, 6, 7, 4, 6, 5},
                {7, 6, 6, 6, 7, 5, 8, 6},
                {8, 0, 8, 1, 7, 0, 9, 0},
                {8, 1, 8, 2, 9, 1, 7, 1, 8, 0},
                {8, 2, 8, 1, 7, 2, 8, 3},
                {8, 3, 8, 2, 7, 3, 8, 4},
                {8, 4, 8, 5, 8, 3, 7, 4},
                {8, 5, 9, 5, 8, 4, 7, 5, 8, 6},
                {8, 6, 8, 5, 7, 6, 9, 6},
                {9, 0, 9, 1, 8, 0},
                {9, 1, 8, 1, 9, 2, 9, 0},
                {9, 2, 9, 1, 9, 3},
                {9, 3, 9, 2, 9, 4},
                {9, 4, 9, 5, 9, 3},
                {9, 5, 8, 5, 9, 4, 9, 6},
                {9, 6, 9, 5, 8, 6}
        };

        Graph<Point> graph = new Graph<>();

        for (int[] element : values) {
            int x = element[0];
            int y = element[1];
            Point c = new Point(x, y);

            Node<Point> node = graph.nodeWith(c);
            for (int i = 2; i < element.length; i += 2) {
                int sx = element[i];
                int sy = element[i + 1];
                Point sc = new Point(sx, sy);
                Node<Point> s = graph.nodeWith(sc);
                node.addSuccessor(s);
            }
        }

        return graph;
    }
}
