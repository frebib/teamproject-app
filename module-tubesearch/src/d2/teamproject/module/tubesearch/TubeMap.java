package d2.teamproject.module.tubesearch;

import d2.teamproject.PARTH;
import d2.teamproject.algorithm.search.Node;
import d2.teamproject.algorithm.search.datastructures.SearchCollection;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Luke Taher
 */
public class TubeMap extends Pane {
    private static final double PANSPEED = 0.6;
    private static final double ZOOMSPEED = 0.0025;
    private static final double ZOOMMIN = 0.5;
    private static final double ZOOMMAX = 6;

    private TubeSearchController controller;
    private Group map;

    private HashMap<TubeStation, Circle> circleMap;

    private double scaleX = PARTH.WIDTH, scaleY;
    private double width, height;

    private boolean isDrag;
    private Point2D mouseDown, translateStart;
    private double zoomFactor = 1;

    public TubeMap(TubeSearchController controller, double aspectRatio, double width, double height) {
        this.controller = controller;
        this.scaleY = scaleX / aspectRatio;
        this.width = width;
        this.height = height;
        this.scaleX *= 1.4;
        this.scaleY *= 1.4;
    }

    public void initialise() {
        SubScene scene = new SubScene(this, width, height);
        scene.setFill(Color.WHITE);

        this.setOnMousePressed(e -> {
            isDrag = e.isPrimaryButtonDown() && !(e.getTarget() instanceof Circle);
            mouseDown = new Point2D(e.getX(), e.getY());
            translateStart = new Point2D(map.getTranslateX(), map.getTranslateY());
        });
        this.setOnMouseReleased(e -> isDrag = false);
        this.setOnMouseDragged(e -> {
            if (!isDrag) return;
            map.setTranslateX(translateStart.getX() - (mouseDown.getX() - e.getX()) * PANSPEED);
            map.setTranslateY(translateStart.getY() - (mouseDown.getY() - e.getY()) * PANSPEED);
        });
        this.setOnScroll(e -> {
            zoomFactor += e.getDeltaY() * ZOOMSPEED;
            zoomFactor = Math.min(Math.max(zoomFactor, ZOOMMIN), ZOOMMAX);
            map.setScaleX(zoomFactor);
            map.setScaleY(zoomFactor);
        });

        Group lines = new Group();
        Group nodes = new Group();
        map = new Group(lines, nodes);
        Set<TubeConnection> connections = controller.getLinks();

        for (TubeConnection conn : connections) {
            if (true) {
//            if (!conn.hasSublines()) {
                // TODO: Draw composite colour lines
                Line line = new Line(conn.getFrom().getX() * scaleX, conn.getFrom().getY() * scaleY,
                        conn.getTo().getX() * scaleX, conn.getTo().getY() * scaleY);
                line.setStrokeWidth(0.005 * scaleX);
                line.setStroke(conn.getLine().getColour());
                lines.getChildren().add(line);

//                SequentialTransition transition = new SequentialTransition();
//                PathTransition d1 = dotTransition(dot, conn.getFrom().getX() * scaleX, conn.getFrom().getY() * scaleY,
//                        conn.getTo().getX() * scaleX, conn.getFrom().getY() * scaleY, 0.2);
//                PathTransition d2 = dotTransition(dot, conn.getFrom().getX() * scaleX, conn.getFrom().getY() * scaleY,
//                        conn.getTo().getX() * scaleX, conn.getTo().getY() * scaleY, 0.4);
//                PathTransition d3 = dotTransition(dot, conn.getFrom().getX() * scaleX, conn.getFrom().getY() * scaleY,
//                        conn.getTo().getX() * scaleX, conn.getTo().getY() * scaleY, 0.6);
//                PathTransition d4 = dotTransition(dot, conn.getFrom().getX() * scaleX, conn.getFrom().getY() * scaleY,
//                        conn.getTo().getX() * scaleX, conn.getTo().getY() * scaleY, 0.8);
//
//                transition.getChildren().addAll(d1, d2, d3, d4);
//                System.out.println(conn.getFrom());
            } else {
//                List<Point2D> points = conn.getSublines();
//
//                double prevX = 0;
//                double prevY = 0;
//                double point1X, point1Y, point2X, point2Y;
//                double lineOffset1X, lineOffset1Y, lineOffset2X, lineOffset2Y;
//
//                point1X = conn.getFrom().getX() * scaleX;
//                point1Y = conn.getFrom().getY() * scaleY;
//                point2X = points.get(0).getX() * scaleX;
//                point2Y = points.get(0).getY() * scaleY;
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
//                    point1X = points.get(i).getX() * scaleX;
//                    point1Y = points.get(i).getY() * scaleY;
//                    point2X = points.get(i + 1).getX() * scaleX;
//                    point2Y = points.get(i + 1).getY() * scaleY;
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
//                point1X = points.get(points.size() - 1).getX() * scaleX;
//                point1Y = points.get(points.size() - 1).getY() * scaleY;
//                point2X = conn.getKey().getPosX() * scaleX;
//                point2Y = conn.getKey().getPosY() * scaleY;
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

//        Rectangle map = new Rectangle(2048.0 * 0.535, 1333.0 * 0.6, Color.AQUA);
//        map.setTranslateX(20);
//        map.setTranslateY(30);
//        map.setFill(new ImagePattern(skybox));

        // loop stations, create node graphics and add to hashmap
        circleMap = new HashMap<>();
        for (TubeStation stn : controller.getStationMap().values()) {
            Circle c = new Circle(stn.getX() * scaleX, stn.getY() * scaleY, 7);
            c.setOnMouseClicked(e -> controller.onStationClick(stn));
            c.setStrokeWidth(5);
            c.setStroke(Color.BLACK);
            c.setFill(Color.WHITE);
            circleMap.put(stn, c);

            nodes.getChildren().add(c);
        }

        this.getChildren().add(map);

        Bounds size = map.getBoundsInLocal();
        map.setTranslateX(-(width - size.getWidth()) / 2);
        map.setTranslateY((height - size.getHeight()) / 2);

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(40);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setRotationAxis(new Point3D(0, 1, 0));

        scene.setCamera(camera);
    }

    public PathTransition dotTransition(Circle n, double x1, double y1, double x2, double y2, double time) {
        PathTransition transition = new PathTransition();
        transition.setDuration(Duration.seconds(time));
        transition.setNode(n);

        Path p = new Path(new MoveTo(x1, y1), new LineTo(x2, y2));

        transition.setPath(p);
        return transition;
    }

    public ParallelTransition animateFrontier(SearchCollection<Node<TubeStation>> fStations) {
        ParallelTransition pt = new ParallelTransition();
        for (Node<TubeStation> s : fStations) {
            Circle c = circleMap.get(s.getContents());
            FillTransition ft = new FillTransition(Duration.millis(400), c, Color.BLACK, Color.YELLOWGREEN);
            ft.setCycleCount(3);
            StrokeTransition st = new StrokeTransition(Duration.millis(400), c, (Color) c.getStroke(), Color.SLATEBLUE);
            pt.getChildren().addAll(ft, st);
        }
        return pt;
    }

    public ParallelTransition animateVisited(Set<Node<TubeStation>> vStations) {
        ParallelTransition pt = new ParallelTransition();
        for (Node<TubeStation> p : vStations) {
            Circle c = circleMap.get(p.getContents());
            FillTransition ft = new FillTransition(Duration.millis(200), c, (Color) c.getFill(), Color.LIGHTGRAY);
            StrokeTransition st = new StrokeTransition(Duration.millis(200), c, (Color) c.getStroke(), Color.DARKGREY);
            pt.getChildren().addAll(ft, st);
        }
        return pt;
    }

    public SequentialTransition animateFinalPath(List<Node<TubeStation>> path) {
        SequentialTransition sqt = new SequentialTransition();
        for (Node<TubeStation> s : path) {
            Circle c = circleMap.get(s.getContents());
            ParallelTransition pt = new ParallelTransition();
            FillTransition ft = new FillTransition(Duration.millis(200), c, (Color) c.getFill(), Color.RED);
            StrokeTransition st = new StrokeTransition(Duration.millis(200), c, (Color) c.getStroke(), Color.GREEN);
            pt.getChildren().addAll(ft, st);
            sqt.getChildren().add(pt);
        }
        return sqt;
    }
}