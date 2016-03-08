package d2.teamproject.module.tubesearch;

import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Set;

public class TubeSearchView extends VisualisationView {
    private TubeSearchController controller;

    private Scene scene;
    private Group root;
    private PerspectiveCamera camera;
    private Double initialCameraXPosition;
    private ArrayList<SequentialTransition> transitions;

    public TubeSearchView(TubeSearchController controller) {
        this.controller = controller;
    }

    public void initialise() {
//        primaryStage.setTitle("Tubicaaaaal Br0");
        double pageWidth = 1000;
        double pageHeight = 750;
        double coordOffsetX = pageWidth;
        double coordOffsetY = pageHeight;
        root = new Group();
        scene = new Scene(root, pageWidth, pageHeight);
        Group lines = new Group();
        Group nodes = new Group();
//        root.setTranslateX(-375);
//        root.setTranslateY(-375);
        Set<TubeConnection> connections = controller.getLinks();
        transitions = new ArrayList<>();
        Circle dot = new Circle(6, Color.BLACK);
        for (TubeConnection conn : connections) {
            if (true) {
//            if (!conn.hasSublines()) {
                Line line = new Line(conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY,
                        conn.getTo().getX() * coordOffsetX, conn.getTo().getY() * coordOffsetY);
                line.setStrokeWidth(5);
                line.setStroke(conn.getLine().getColour());
                lines.getChildren().add(line);
//
//                SequentialTransition transition = new SequentialTransition();
//                PathTransition d1 = dotTransition(dot, conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY,
//                        conn.getTo().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY, 0.2);
//                PathTransition d2 = dotTransition(dot, conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY,
//                        conn.getKey().getPosX() * coordOffsetX, conn.getKey().getPosY() * coordOffsetY, 0.4);
//                PathTransition d3 = dotTransition(dot, conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY,
//                        conn.getKey().getPosX() * coordOffsetX, conn.getKey().getPosY() * coordOffsetY, 0.6);
//                PathTransition d4 = dotTransition(dot, conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY,
//                        conn.getKey().getPosX() * coordOffsetX, conn.getKey().getPosY() * coordOffsetY, 0.8);

//                transition.getChildren().addAll(d1, d2, d3, d4);
//                transitions.add(transition);
                System.out.println(conn.getFrom());
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

        root.getChildren().add(lines);
        root.getChildren().add(dot);
        for (TubeConnection conn : connections) {
            Circle c = new Circle(conn.getFrom().getX() * coordOffsetX, conn.getFrom().getY() * coordOffsetY, 7);
            c.setStrokeWidth(5);
            c.setStroke(Color.BLACK);
            c.setFill(Color.WHITE);
            nodes.getChildren().add(c);
        }
//        Circle c = new Circle(0, 0, 7);
//        c.setStrokeWidth(5);
//        c.setStroke(Color.BLACK);
//        c.setFill(Color.WHITE);
//        nodes.getChildren().add(c);
//        transitions.get(10).play();
        root.getChildren().add(nodes);

        initialCameraXPosition = -375.0;
        camera = new PerspectiveCamera();
        camera.setFieldOfView(40);
        camera.setTranslateX(initialCameraXPosition);
        camera.setTranslateY(scene.getHeight() / -2);
        camera.setRotationAxis(new Point3D(0, 1, 0));
    }

    public PathTransition dotTransition(Node n, double x1, double y1, double x2, double y2, double time) {
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
//        if (point1 < point2)
//            return 5;
//        else if (point1 > point2)
//            return -5;
//        else
//            return 0;
    }

    @Override
    public BaseController getController() {
        return controller;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}