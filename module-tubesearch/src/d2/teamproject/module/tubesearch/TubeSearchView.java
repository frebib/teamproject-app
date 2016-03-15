package d2.teamproject.module.tubesearch;

import d2.teamproject.algorithm.search.AStarSearchStream;
import d2.teamproject.algorithm.search.Node;
import d2.teamproject.algorithm.search.SearchState;
import d2.teamproject.algorithm.search.SearchStream;
import d2.teamproject.algorithm.search.datastructures.SearchCollection;
import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.*;
import java.util.function.BiFunction;

/**
 * @author Parth Chandratreya
 */
public class TubeSearchView extends VisualisationView {

    private TubeSearchController controller;
    private SubScene scene;
    private Group root;
    private PerspectiveCamera camera;
    private Double initialCameraXPosition;
    private ArrayList<SequentialTransition> transitions;
    private Map<String, TubeStation> stationMap;
//    private ArrayList<FillTransition> currentTransitions;
//    private ArrayList<FillTransition> frontierTransitions;
    private SequentialTransition frontierSequence;
    private SequentialTransition currentSequence;
    private double pageWidth;
    private double pageHeight;
    private HashMap<TubeStation, Circle> circleMap;
    private javafx.scene.image.Image skybox;

    public TubeSearchView(TubeSearchController controller, int width, int height, Image skybox) {
        this.controller = controller;
        this.skybox = skybox;
        pageWidth = width;
        pageHeight = height;
    }

    public void initialise() {
        double coordOffsetX = pageWidth;
        double coordOffsetY = pageHeight;
        root = new Group();
        scene = new SubScene(root, pageWidth, pageHeight);
        Group lines = new Group();
        Group nodes = new Group();
        Set<TubeConnection> connections = controller.getLinks();
        stationMap = controller.getStationMap();
        transitions = new ArrayList<>();
        frontierSequence = new SequentialTransition();
        currentSequence = new SequentialTransition();

        Circle dot = new Circle(6, Color.BLACK);
        for (TubeConnection conn : connections) {
            if (true) {
//            if (!conn.hasSublines()) {
                Line line = new Line(conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY,
                        conn.getTo().getX() * coordOffsetX, conn.getTo().getY() * coordOffsetY);
                line.setStrokeWidth(5);
                line.setStroke(conn.getLine().getColour());
                lines.getChildren().add(line);

                SequentialTransition transition = new SequentialTransition();
                PathTransition d1 = dotTransition(dot, conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY,
                        conn.getTo().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY, 0.2);
                PathTransition d2 = dotTransition(dot, conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY,
                        conn.getTo().getX() * coordOffsetX, conn.getTo().getY() * coordOffsetY, 0.4);
                PathTransition d3 = dotTransition(dot, conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY,
                        conn.getTo().getX() * coordOffsetX, conn.getTo().getY() * coordOffsetY, 0.6);
                PathTransition d4 = dotTransition(dot, conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY,
                        conn.getTo().getX() * coordOffsetX, conn.getTo().getY() * coordOffsetY, 0.8);

                transition.getChildren().addAll(d1, d2, d3, d4);
                transitions.add(transition);
//                System.out.println(conn.getFrom());
            } else {
//                List<Point2D> points = conn.getSublines();
//
//                double prevX = 0;
//                double prevY = 0;
//                double point1X, point1Y, point2X, point2Y;
//                double lineOffset1X, lineOffset1Y, lineOffset2X, lineOffset2Y;
//
//                point1X = conn.getFrom().getX() * coordOffsetX;
//                point1Y = conn.getFrom().getY() * coordOffsetY;
//                point2X = points.get(0).getX() * coordOffsetX;
//                point2Y = points.get(0).getY() * coordOffsetY;
//                lineOffset1X = 0;
//                lineOffset1Y = 0;
//                lineOffset2X = calcOffset(point2X, point1X);
//                lineOffset2Y = calcOffset(point2Y, point1Y);
//                prevX = point2X + lineOffset2X;
//                prevY = point2Y + lineOffset2Y;
//
//                Line line = new Line(point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y);
//                line.setStrokeWidth(5);
//                line.setStroke(conn.getValue().getColour());
//                lines.getChildren().add(line);
//
//
//                SequentialTransition transition = new SequentialTransition();
//                PathTransition d1 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.2);
//                PathTransition d2 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.4);
//                PathTransition d3 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.6);
//                PathTransition d4 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.8);
//
//                transition.getChildren().addAll(d1, d2, d3, d4);
//
//
//                System.out.println(points.size());
//                for (int i = 0; i < points.size() - 1; i++) {
//
//                    point1X = points.get(i).getX() * coordOffsetX;
//                    point1Y = points.get(i).getY() * coordOffsetY;
//                    point2X = points.get(i + 1).getX() * coordOffsetX;
//                    point2Y = points.get(i + 1).getY() * coordOffsetY;
//                     offset for a point on its second line is itself and next
//                     offset for a point on its first line is itself with prev point
//                    lineOffset1X = calcOffset(point1X, prevX);
//                    lineOffset1Y = calcOffset(point1Y, prevY);
//                    lineOffset2X = calcOffset(point2X + lineOffset1X, point1X);
//                    lineOffset2Y = calcOffset(point2Y + lineOffset1Y, point1Y);
//
//                        System.out.println("X1:" + (point1X+lineOffset1X));
//                        System.out.println("Y1:" + (point1Y+lineOffset1Y));
//                        System.out.println("X2:" + (prevX));
//                        System.out.println("Y2:" + (prevY));
//                        System.out.println("C1:" + (point1X));
//                        System.out.println("C2:" + (point1Y));
//                        QuadCurve c = genCurve(Color.ALICEBLUE, point1X+lineOffset1X, point1Y+lineOffset1Y,
//                                                prevX, prevY, point1X, point1Y);
//                        root.getChildren().add(c);
//
//                    prevX = point2X + lineOffset2X;
//                    prevY = point2Y + lineOffset2Y;
//
//                    Line line2 = new Line(point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y);
//                    line2.setStrokeWidth(5);
//                    line2.setStroke(conn.getValue().getColour());
//                    lines.getChildren().add(line2);
//
//                    PathTransition d5 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.2);
//                    PathTransition d6 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.4);
//                    PathTransition d7 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.6);
//                    PathTransition d8 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.8);
//
//                    transition.getChildren().addAll(d5, d6, d7, d8);

//                }
//                point1X = points.get(points.size() - 1).getX() * coordOffsetX;
//                point1Y = points.get(points.size() - 1).getY() * coordOffsetY;
//                point2X = conn.getKey().getPosX() * coordOffsetX;
//                point2Y = conn.getKey().getPosY() * coordOffsetY;
//                lineOffset1X = calcOffset(point1X, prevX);
//                lineOffset1Y = calcOffset(point1Y, prevY);
//                lineOffset2X = 0;
//                lineOffset2Y = 0;
//
//                Line line2 = new Line(point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y);
//                line2.setStrokeWidth(5);
//                line2.setStroke(conn.getValue().getColour());
//                lines.getChildren().add(line2);
//
//                PathTransition d5 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.2);
//                PathTransition d6 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.4);
//                PathTransition d7 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.6);
//                PathTransition d8 = dotTransition(dot, point1X + lineOffset1X, point1Y + lineOffset1Y, point2X + lineOffset2X, point2Y + lineOffset2Y, 0.8);
//
//                transition.getChildren().addAll(d5, d6, d7, d8);
//
//                transitions.add(transition);
            }
        }

        Rectangle map = new Rectangle(2048.0*0.535,1333.0*0.6,Color.AQUA);
        map.setTranslateX(20);
        map.setTranslateY(30);
        map.setFill(new ImagePattern(skybox));

        root.getChildren().addAll(map,lines,dot);


        // loop stations, create node graphics and add to hashmap
        int x = 0;
        TubeStation start = null;
        TubeStation goal = null;
        circleMap = new HashMap<>();
        for (TubeStation stn : stationMap.values()) {
            Circle c = new Circle(stn.getX() * coordOffsetX, stn.getY() * coordOffsetY, 7);
            c.setStrokeWidth(5);
            c.setStroke(Color.BLACK);
            c.setFill(Color.WHITE);
            circleMap.put(stn, c);

            nodes.getChildren().add(c);
            if (x == 15) {
                goal = stn;
            } else if (x == 0) {
                start = stn;
            }
            x++;
        }

        root.getChildren().add(nodes);

        //create search

        SearchStream<TubeStation> stream = new AStarSearchStream<>(start, goal);

        // Euclidean distance between 2 nodes
        BiFunction<TubeStation, TubeStation, Double> euclidean = (a, b) -> Math.sqrt(
                Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));

        // Euclidean distance between 2 adjacent nodes, plus total cost of previous node
        BiFunction<TubeStation, TubeStation, Double> costFn = (a, b) ->
                stream.getCost(a) + euclidean.apply(a, b);

        stream.setCostFn(costFn)
              .setHeuristicFn(euclidean)
              .initialise();

        List<SearchState<Node<TubeStation>>> search = stream.getAll();

        //create animations for search

        for (SearchState<Node<TubeStation>> state : search) {
            animateFrontier(state.getFrontier());
            animatePath(state.getVisited());
        }

        ParallelTransition searchTransition = new ParallelTransition();
        searchTransition.getChildren().addAll(frontierSequence, currentSequence);
        searchTransition.play();





        initialCameraXPosition = 0.0;
        camera = new PerspectiveCamera();
        camera.setFieldOfView(40);
        camera.setTranslateX(initialCameraXPosition);
        camera.setTranslateY(0);
        camera.setRotationAxis(new Point3D(0, 1, 0));

        scene.setCamera(camera);

    }

    public PathTransition dotTransition(Circle n, double x1, double y1, double x2, double y2, double time) {
        PathTransition transition = new PathTransition();
        transition.setDuration(Duration.seconds(time));
        transition.setNode(n);

        javafx.scene.shape.Path p = new javafx.scene.shape.Path();
        p.getElements().add(new MoveTo(x1, y1));
        p.getElements().add(new LineTo(x2, y2));

        transition.setPath(p);
        return transition;
    }

    private double calcOffset(double point1, double point2) {
        return Math.signum(-Double.compare(point1, point2)) * 5; //<- will do the same thing..
    }

    private void animateFrontier(SearchCollection<Node<TubeStation>> fStations) {
        ParallelTransition pt = new ParallelTransition();
        for (Node<TubeStation> s : fStations)
        {
            Circle c = circleMap.get(s.getContents());
            FillTransition ft = new FillTransition(Duration.millis(1000), c, Color.WHITE, Color.YELLOW);
            ft.setCycleCount(4);
            ft.setAutoReverse(true);
            pt.getChildren().add(ft);
        }
        frontierSequence.getChildren().add(pt);
    }

    private void animatePath(Set<Node<TubeStation>> pStations) {
        ParallelTransition pt = new ParallelTransition();
        for (Node<TubeStation> p : pStations)
        {
            Circle c = circleMap.get(p.getContents());
            FillTransition ft = new FillTransition(Duration.millis(1000), c, Color.WHITE, Color.AQUAMARINE);
            ft.setCycleCount(4);
            ft.setAutoReverse(true);
            pt.getChildren().add(ft);
        }
        currentSequence.getChildren().add(pt);
    }

    @Override
    public BaseController getController() {
        return controller;
    }

    public SubScene getSubScene() {
        return scene;
    }
}