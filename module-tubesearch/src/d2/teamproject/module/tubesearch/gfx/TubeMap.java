package d2.teamproject.module.tubesearch.gfx;

import d2.teamproject.PARTH;
import d2.teamproject.algorithm.search.Node;
import d2.teamproject.algorithm.search.datastructures.SearchCollection;
import d2.teamproject.module.tubesearch.TubeConnection;
import d2.teamproject.module.tubesearch.TubeLine;
import d2.teamproject.module.tubesearch.TubeSearchController;
import d2.teamproject.module.tubesearch.TubeStation;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import static d2.teamproject.PARTH.LOG;

/**
 * @author Luke Taher
 */
public class TubeMap extends Pane {
    private static final double PANSPEED = 0.6;
    private static final double ZOOMSPEED = 0.0025;
    private static final double ZOOMMIN = 0.5;
    private static final double ZOOMMAX = 6;

    private static final double CURVE_RADIUS = 0.012;
    private static final double LINE_WIDTH = 0.0047;
    private static final double INNER_LINE_WIDTH = 0.0012;
    private static final double CIRCLE_RADIUS = LINE_WIDTH * 0.9;
    private static final double WALK_WIDTH = 0.005;
    private static final double WALK_STROKE = 0.0018;

    private TubeSearchController controller;
    private Group map;

    private HashMap<TubeStation, Circle> circleMap;

    private double scaleX = PARTH.WIDTH * 1.4, scaleY;
    private double width, height;

    private boolean isDrag;
    private Point2D mouseDown, translateStart;
    private double zoomFactor = 1;

    public TubeMap(TubeSearchController controller, double aspectRatio, double width, double height) {
        this.controller = controller;
        this.scaleY = scaleX / aspectRatio;
        this.width = width;
        this.height = height;
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
            // TODO: Always ensure model is onscreen
            zoomFactor += e.getDeltaY() * ZOOMSPEED;
            zoomFactor = Math.min(Math.max(zoomFactor, ZOOMMIN), ZOOMMAX);
            map.setScaleX(zoomFactor);
            map.setScaleY(zoomFactor);
        });

        Group lines = new Group();
        Group walks = new Group();
        Group nodes = new Group();
        lines.setMouseTransparent(true);
        walks.setMouseTransparent(true);
        map = new Group(lines, nodes, walks);
        Set<TubeConnection> connections = controller.getLinks();

        for (TubeConnection conn : connections) {
            // We'll draw the walk lines further down
            if (conn.getLine().getId().equals("walk"))
                continue;

            if (conn.getCurvePoints().isEmpty()) {
                // Draw line
                Line line = new Line(conn.getFrom().getX() * scaleX, conn.getFrom().getY() * scaleY,
                        conn.getTo().getX() * scaleX, conn.getTo().getY() * scaleY);
                line.setStrokeWidth(LINE_WIDTH * scaleX);
                line.setStroke(conn.getLine().getColour());
                lines.getChildren().add(line);

                // Draw line inner (if it has one)
                if (conn.getLine().getInnerColour() != null) {
                    Line inner = new Line(conn.getFrom().getX() * scaleX, conn.getFrom().getY() * scaleY,
                            conn.getTo().getX() * scaleX, conn.getTo().getY() * scaleY);
                    inner.setStrokeWidth(INNER_LINE_WIDTH * scaleX);
                    inner.setStroke(conn.getLine().getInnerColour());
                    lines.getChildren().add(inner);
                }
            } else {
                List<Point2D> points = conn.getLinePoints();

                // Given 2 points that represent a line, this returns the
                // point shortened by a fixed amount from point b
                BiFunction<Point2D, Point2D, Point2D> shorten = (a, b) -> {
                    Point2D delta = b.subtract(a);
                    double len = a.distance(b);
                    double ratio = (len - CURVE_RADIUS) / len;
                    return a.add(delta.multiply(ratio));
                };

                List<PathElement> pathElems = new ArrayList<>();

                Point2D start = points.get(0);
                pathElems.add(new MoveTo(start.getX() * scaleX, start.getY() * scaleY));

                Point2D lastPoint = points.get(0);
                for (int i = 1; i < points.size() - 1; i++) {
                    Point2D m = points.get(i);
                    Point2D b = points.get(i + 1);

                    Point2D curveStart = shorten.apply(lastPoint, m);
                    Point2D curveEnd = shorten.apply(b, m);

                    pathElems.add(new LineTo(curveStart.getX() * scaleX, curveStart.getY() * scaleY));
                    pathElems.add(new QuadCurveTo(m.getX() * scaleX, m.getY() * scaleY, curveEnd.getX() * scaleX, curveEnd.getY() * scaleY));
                    lastPoint = curveEnd;
                }

                Point2D end = points.get(points.size() - 1);
                pathElems.add(new LineTo(end.getX() * scaleX, end.getY() * scaleY));

                Path path = new Path(pathElems);
                path.setStrokeWidth(LINE_WIDTH * scaleX);
                path.setStroke(conn.getLine().getColour());
                lines.getChildren().add(path);

                if (conn.getLine().getInnerColour() != null) {
                    path = new Path(pathElems);
                    path.setStrokeWidth(INNER_LINE_WIDTH * scaleX);
                    path.setStroke(conn.getLine().getInnerColour());
                    lines.getChildren().add(path);
                }
            }
        }

        connections.stream()
                .filter(conn -> conn.getLine().getId().equals("walk"))
                .forEach(conn -> {
                    // Draw lower line
                    Line line = new Line(conn.getFrom().getX() * scaleX, conn.getFrom().getY() * scaleY,
                            conn.getTo().getX() * scaleX, conn.getTo().getY() * scaleY);
                    line.setStrokeWidth(WALK_WIDTH * scaleX);
                    line.setStroke(conn.getLine().getColour());
                    lines.getChildren().add(line);

                    // Draw over line
                    Line inner = new Line(conn.getFrom().getX() * scaleX, conn.getFrom().getY() * scaleY,
                            conn.getTo().getX() * scaleX, conn.getTo().getY() * scaleY);
                    inner.setStrokeWidth((WALK_WIDTH - WALK_STROKE * 2) * scaleX);
                    inner.setStroke(conn.getLine().getInnerColour());
                    walks.getChildren().add(inner);
                });

//        Rectangle map = new Rectangle(2048.0 * 0.535, 1333.0 * 0.6, Color.AQUA);
//        map.setTranslateX(20);
//        map.setTranslateY(30);
//        map.setFill(new ImagePattern(skybox));

        // loop stations, create node graphics and add to hashmap
        circleMap = new HashMap<>();
        TubeLine walkLine = controller.getLineMap().get("walk");
        for (TubeStation stn : controller.getStationMap().values()) {
            Circle c = new Circle(stn.getX() * scaleX, stn.getY() * scaleY, CIRCLE_RADIUS * scaleX);
            c.setOnMouseClicked(e -> controller.onStationClick(stn));
            c.setOnMouseEntered(this::stationMouseOver);
            c.setOnMouseExited(this::stationMouseOver);
            c.setStrokeWidth(WALK_STROKE * scaleX);
            c.setStroke(walkLine.getColour());
            c.setFill(walkLine.getInnerColour());
            c.setUserData(stn);
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

    private void stationMouseOver(MouseEvent mouseEvent) {
        boolean mouseOver = mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED;
        TubeStation hoverStation = (TubeStation) ((Circle) mouseEvent.getTarget()).getUserData();

        LOG.fine("Mouse is %s %s", mouseOver ? "over" : "out", hoverStation.getName());
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