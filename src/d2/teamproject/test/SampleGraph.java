package d2.teamproject.test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import d2.teamproject.algorithm.search.Node;
import d2.teamproject.algorithm.sorting.BubbleSort;

public class SampleGraph {
	public static void printGraph(Graph<Point> graph) {
		for (Map.Entry<Point, Node<Point>> e : graph.getNodes().entrySet()) {
			Point c = e.getKey();
			Node<Point> node = e.getValue();

			assert (c.equals(node.contents));

			System.out.print(c + "): ");
			for (Node<Point> s : node.getSuccessors())
				System.out.print(s.contents + "), ");
			System.out.println();
		}
	}

	/*
	 * parseGraph
	 * @param values
	 *
	 */
	public static Graph<Point> parseGraph(int[][] values) {
		Graph<Point> graph = new Graph<Point>();

		for (int[] element : values) {
			assert (element.length >= 2);       
			assert (element.length % 2 == 0);   

			int x = element[0];					
			int y = element[1]; 				
			Point c = new Point(x, y);

			// Find or create node:
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
	/**
	 *getGraph
	 *@return parseGraph
	 */
	public static Graph<Point> getGraph() {
		return parseGraph(getValues());
	}
	
	private static int[][] getValues() {
		return new int[][] {
			{ 0, 0, 1, 0, 0, 1 },
			{ 0, 1, 0, 0, 1, 1, 0, 2 },
			{ 0, 2, 0, 3, 0, 1 },
			{ 0, 3, 0, 2, 0, 4 },
			{ 0, 4, 0, 3, 0, 5 },
			{ 0, 5, 0, 6, 1, 5, 0, 4 },
			{ 0, 6, 1, 6, 0, 5 },
			{ 1, 0, 0, 0, 1, 1, 2, 0 },
			{ 1, 1, 1, 2, 2, 1, 1, 0, 0, 1 },
			{ 1, 2, 2, 2, 1, 1, 1, 3 },
			{ 1, 3, 1, 2, 1, 4, 2, 3 },
			{ 1, 4, 2, 4, 1, 5, 1, 3 },
			{ 1, 5, 1, 4, 2, 5, 1, 6, 0, 5 },
			{ 1, 6, 0, 6, 1, 5, 2, 6 },
			{ 2, 0, 3, 0, 2, 1, 1, 0 },
			{ 2, 1, 2, 2, 1, 1, 2, 0, 3, 1 },
			{ 2, 2, 1, 2, 2, 1, 2, 3, 3, 2 },
			{ 2, 3, 2, 2, 2, 4, 3, 3, 1, 3 },
			{ 2, 4, 1, 4, 2, 5, 2, 3, 3, 4 },
			{ 2, 5, 2, 4, 1, 5, 2, 6, 3, 5 },
			{ 2, 6, 3, 6, 2, 5, 1, 6 },
			{ 3, 0, 2, 0, 3, 1 },
			{ 3, 1, 3, 0, 4, 1, 2, 1, 3, 2 },
			{ 3, 2, 2, 2, 4, 2, 3, 1 },
			{ 3, 3, 2, 3, 3, 4 },
			{ 3, 4, 2, 4, 3, 3 },
			{ 3, 5, 3, 6, 2, 5, 4, 5 },
			{ 3, 6, 2, 6, 3, 5 },
			{ 4, 0 },
			{ 4, 1, 4, 2, 5, 1, 3, 1 },
			{ 4, 2, 4, 1, 5, 2, 3, 2 },
			{ 4, 3 },
			{ 4, 4 },
			{ 4, 5, 5, 5, 3, 5 },
			{ 4, 6 },
			{ 5, 0 },
			{ 5, 1, 4, 1, 5, 2, 6, 1 },
			{ 5, 2, 4, 2, 5, 1, 6, 2 },
			{ 5, 3 },
			{ 5, 4 },
			{ 5, 5, 4, 5, 6, 5 },
			{ 5, 6 },
			{ 6, 0, 7, 0, 6, 1 },
			{ 6, 1, 6, 0, 5, 1, 6, 2, 7, 1 },
			{ 6, 2, 5, 2, 6, 1, 7, 2 },
			{ 6, 3, 7, 3, 6, 4 },
			{ 6, 4, 6, 3, 7, 4 },
			{ 6, 5, 5, 5, 6, 6, 7, 5 },
			{ 6, 6, 7, 6, 6, 5 },
			{ 7, 0, 6, 0, 7, 1, 8, 0 },
			{ 7, 1, 8, 1, 7, 0, 6, 1, 7, 2 },
			{ 7, 2, 7, 3, 8, 2, 6, 2, 7, 1 },
			{ 7, 3, 6, 3, 7, 2, 7, 4, 8, 3 },
			{ 7, 4, 7, 3, 8, 4, 6, 4, 7, 5 },
			{ 7, 5, 8, 5, 7, 6, 7, 4, 6, 5 },
			{ 7, 6, 6, 6, 7, 5, 8, 6 },
			{ 8, 0, 8, 1, 7, 0, 9, 0 },
			{ 8, 1, 8, 2, 9, 1, 7, 1, 8, 0 },
			{ 8, 2, 8, 1, 7, 2, 8, 3 },
			{ 8, 3, 8, 2, 7, 3, 8, 4 },
			{ 8, 4, 8, 5, 8, 3, 7, 4 },
			{ 8, 5, 9, 5, 8, 4, 7, 5, 8, 6 },
			{ 8, 6, 8, 5, 7, 6, 9, 6 },
			{ 9, 0, 9, 1, 8, 0 },
			{ 9, 1, 8, 1, 9, 2, 9, 0 },
			{ 9, 2, 9, 1, 9, 3 },
			{ 9, 3, 9, 2, 9, 4 },
			{ 9, 4, 9, 5, 9, 3 },
			{ 9, 5, 8, 5, 9, 4, 9, 6 },
			{ 9, 6, 9, 5, 8, 6 } };
	}
	public static void main(String[] args) {
       printGraph(getGraph());
    }
}
