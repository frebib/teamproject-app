package d2.teamproject.module.planets.gfx;

import d2.teamproject.algorithm.sorting.CompareSortState;
import d2.teamproject.algorithm.sorting.PartitionSortState;
import d2.teamproject.module.planets.Planet;
import d2.teamproject.util.ListUtil;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Parth Chandratreya
 */
public class SolarSystem {
    private static final Duration COMPARE_ANIM_TIME = new Duration(300);
    private static final Duration SWAP_ANIM_TIME = new Duration(500);
    private static final Duration FADE_ANIM_TIME = new Duration(500);

    public static final int GAP = 60;

    private final SubScene scene;
    private final Group root;
    private final PerspectiveCamera camera;
    private final Map<Planet, PlanetRenderer> rendererMap;
    private List<PlanetRenderer> planetRenderers;
    private float cumulativeDist = 0;
    private final Point3D initCameraAxis, initCameraTrans;
    private final double initCameraRot = 42;

    private Planet zoomed;
    private List<PlanetRenderer> unFocused;
    private Number planetRotationSpeed;

    /**
     * Uses a list of planets to create the models for the scene, adds information of the planet to the scene
     * Creates multiple rectangles used to simulate the stars in the background
     * Adds event handlers to each planet to zoom on click and show information
     * @param planets       A list of Planet used to create Planet Renderers used in the scene
     * @param width         the width of the scene
     * @param height        the height of the scene
     * @param skyboxTexture An image used for the background
     */
    public SolarSystem(List<Planet> planets, double width, double height, Image skyboxTexture) {
        rendererMap = new LinkedHashMap<>(planets.size());
        for (Planet planet : planets) {
            PlanetRenderer r = new PlanetRenderer(planet, cumulativeDist);
            cumulativeDist += (r.getRadius() * 2) + GAP;
            rendererMap.put(planet, r);
        }
        setPlanetOrder(planets);

        root = new Group();
        scene = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK); /* Black background */
        createSkyboxSections(skyboxTexture);

        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.001);
        camera.setFarClip(100000);
        camera.setFieldOfView(30);

        initCameraAxis = new Point3D(-0.28, -0.32, 0.00);
        initCameraTrans = new Point3D(1150, -350, -600);
        camera.setTranslateX(initCameraTrans.getX());
        camera.setTranslateY(initCameraTrans.getY());
        camera.setTranslateZ(initCameraTrans.getZ());
        camera.setRotationAxis(initCameraAxis);
        camera.setRotate(initCameraRot);

        root.getChildren().add(camera);
        root.getChildren().addAll(planetRenderers.stream()
                .map(PlanetRenderer::getModel)
                .collect(Collectors.toList()));    // TODO: Get rid of this planetRenderers

        for (PlanetRenderer p : planetRenderers) {
            p.onClick(e -> {
                if (p.getPlanet() == zoomed) {   // TODO: Better click checking needs to be added
                    zoomed = null;
                    zoomOut().play(); /* Zoom out the camera*/
                } else {
                    zoomed = p.getPlanet();
                    zoomIn(p).play();   /* Zoom in the camera */
                }
            });
        }

        scene.setCamera(camera);
    }

    /**
     * Moves a camera close to a planet position, giving a zoom in effect
     * @return A transition to be played
     */
    private Transition zoomIn(PlanetRenderer renderer) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1.2), camera);
        double onX = renderer.getModel().getLocalToSceneTransform().getTx();
        tt.setToX(onX);
        tt.setToY(0);
        tt.setToZ(-(renderer.getRadius() * 4.5));

        RotateTransition rt = new RotateTransition(Duration.seconds(1.2), camera);
        rt.setAxis(camera.getRotationAxis());
        rt.setToAngle(0);
        return new ParallelTransition(tt, rt);
    }

    /**
     * Returns a camera to its original position
     * @return A transition to be played
     */
    private Transition zoomOut() {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1.2), camera);
        tt.setToX(initCameraTrans.getX());
        tt.setToY(initCameraTrans.getY());
        tt.setToZ(initCameraTrans.getZ());

        RotateTransition rt = new RotateTransition(Duration.seconds(1.2), camera);
        rt.setAxis(initCameraAxis);
        rt.setToAngle(initCameraRot);
        return new ParallelTransition(tt, rt);
    }

    /**
     * Creates a comparison transition of 2 planets specified by the {@link CompareSortState}
     * @param state     indicies for choosing the planets to compare
     * @param isReverse
     * @return the started transition object
     */
    public Transition transitionCompare(CompareSortState<Planet> state, boolean isReverse) {
        Point p = state.getCompares();
        Node pr1 = planetRenderers.get(Math.max(p.x, p.y)).getModel();
        Node pr2 = planetRenderers.get(Math.min(p.x, p.y)).getModel();
        return getCompareTransition(pr1, pr2, isReverse);
    }

    /**
     * Makes a transition to swap two nodes in an arc, shifting all
     * planets in between equally to maintain the constant spacing
     * @param state indicies for choosing the planets to compare
     * @return a transition to move the planets
     */
    public Transition makeSwapTransition(CompareSortState<Planet> state) {
        Point p = state.getCompares();
        int lo = Math.min(p.x, p.y),
                hi = Math.max(p.x, p.y);
        PlanetRenderer pr1 = planetRenderers.get(hi);
        PlanetRenderer pr2 = planetRenderers.get(lo);
        Node pm1 = pr1.getModel();
        Node pm2 = pr2.getModel();

        List<PlanetRenderer> toMove = planetRenderers.subList(lo, hi + 1);

        double diff = pr1.getRadius() - pr2.getRadius();
        double height = 0;

        // Move all planets in between
        List<Transition> planetShifts = null;
        if (toMove.size() > 2) {
            planetShifts = toMove
                    .subList(1, toMove.size() - 1)
                    .stream()
                    .map(PlanetRenderer::getModel)
                    .map(m -> {
                        TranslateTransition tr = new TranslateTransition(SWAP_ANIM_TIME, m);
                        tr.setByX(diff * 2);
                        return tr;
                    }).collect(Collectors.toList());

            height = toMove.stream()
                    .map(PlanetRenderer::getModel)
                    .map(Node::getBoundsInParent)
                    .map(Bounds::getHeight)
                    .reduce(0d, Math::max);
        } else {
            height = Math.max(pr1.getRadius(), pr2.getRadius()) + GAP / 2;
        }

        Transition upper = new PathTransition(SWAP_ANIM_TIME, getSwapPath(pm2, pm1, height, diff, false), pm2),
                lower = new PathTransition(SWAP_ANIM_TIME, getSwapPath(pm1, pm2, height, diff, true), pm1),
                upRet = getCompareTransition(pm1, pm2, true),
                loRet = getCompareTransition(pm1, pm2, true),
                first = new ParallelTransition(upRet, loRet);

        ParallelTransition secnd = new ParallelTransition(upper, lower);
        if (planetShifts != null) secnd.getChildren().addAll(planetShifts);

        return new SequentialTransition(first, secnd);
    }

    private Transition getCompareTransition(Node planet1, Node planet2, boolean isReverse) {
        TranslateTransition p1comp = new TranslateTransition(COMPARE_ANIM_TIME, planet1),
                p2comp = new TranslateTransition(COMPARE_ANIM_TIME, planet2);
        double toZ = isReverse ? 0 : -100;
        p1comp.setToZ(toZ);
        p2comp.setToZ(toZ);
        return new ParallelTransition(p1comp, p2comp);
    }

    /**
     * Creates a path that swaps two planets in a loop animation
     * @param from   the first node to swap with the second
     * @param to     the second node to swap with the first
     * @param height the height both planets must reach to clear surrounding obstacles
     * @param offset the offset to move both planets for aligning to surrounding obstacles
     * @param flip   set true if the path should be on the top side
     * @return a path for the planets to follow
     */
    private Path getSwapPath(Node from, Node to, double height, double offset, boolean flip) {
        double fromX = from.getTranslateX(),
                fromY = from.getTranslateY(),
                toX = to.getTranslateX() + offset,
                half = Math.abs(toX - fromX) / 2,
                arc = Math.min(height, half);

        PathElement move, line1, line2, line3, curve1, curve2;
        move = new MoveTo(fromX, fromY);
        line3 = new LineTo(toX, 0);
        if (flip) {
            line1 = new LineTo(fromX, height - arc);
            curve1 = new QuadCurveTo(fromX, height, fromX - arc, height);
            line2 = new LineTo(toX + arc, height);
            curve2 = new QuadCurveTo(toX, height, toX, height - arc);
        } else {
            line1 = new LineTo(fromX, -height + arc);
            curve1 = new QuadCurveTo(fromX, -height, fromX + arc, -height);
            line2 = new LineTo(toX - arc, -height);
            curve2 = new QuadCurveTo(toX, -height, toX, -height + arc);
        }
        return new Path(move, line1, curve1, line2, curve2, line3);
    }

    /**
     * Creates a rectangle in the given position with a image as the background,
     * then fades the shape in and out for a certain amount of time
     * @param xPos The x position where the rectangle should go
     * @param yPos The y position where the rectangle should go
     * @param sb   The background image of rectangle
     * @param time The duration of the fade transition
     */
    private void skyboxSection(double xPos, double yPos, Image sb, double time) {
        Rectangle rect = new Rectangle(500, 500);
        rect.setTranslateX(xPos);
        rect.setTranslateY(yPos);
        rect.setTranslateZ(200);
        rect.setFill(new ImagePattern(sb));
//        rect.setFill(Color.GRAY); /* comment in to check random fade out */

        FadeTransition transition = new FadeTransition(Duration.millis(time), rect);
        transition.setFromValue(1.0);
        transition.setToValue(0.70);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
        root.getChildren().add(rect);
    }

    /**
     * @return either 2000,3000,4000 randomly as a double
     */
    private double randomNumber() {
        Random x = new Random();
        int y = x.nextInt(2) + 2;
        return (double) y * 1000;
    }

    // TODO: Clean up skyboxes, make smaller and more random
    private void createSkyboxSections(Image skyboxTexture) {
        skyboxSection(-170, -750, skyboxTexture, randomNumber());               /* Skybox for the left hand side of the screen */
        skyboxSection(-170, -250, skyboxTexture, randomNumber());
        skyboxSection(-170, 250, skyboxTexture, randomNumber());
        skyboxSection(330, -750, skyboxTexture, randomNumber());                /* Skybox for the center of the screen */
        skyboxSection(330, -250, skyboxTexture, randomNumber());
        skyboxSection(330, 250, skyboxTexture, randomNumber());
        skyboxSection(830, -750, skyboxTexture, randomNumber());                /* Skybox for the right hand side of the screen*/
        skyboxSection(830, -250, skyboxTexture, randomNumber());
        skyboxSection(830, 250, skyboxTexture, randomNumber());
    }

    /**
     * @return the SubScene
     */
    public SubScene getScene() {
        return scene;
    }

    public void setPlanetOrder(List<Planet> list) {
        planetRenderers = list.stream().map(rendererMap::get).collect(Collectors.toList());
    }

    // Providing what this horrible language couldn't... in 3 lines...
    public static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }
    public void setPartition(PartitionSortState<Planet> partition) {
        List<PlanetRenderer> prevUnFocused = unFocused;
        unFocused = planetRenderers.stream()
                .filter(p -> {
                    int i = planetRenderers.indexOf(p);
                    return i < partition.getLower() || i > partition.getUpper();
                }).collect(Collectors.toList());

        System.out.printf("Pivot:   %d\nOutside: ", partition.getPivot());
        ListUtil.printList(unFocused.stream()
                .map(PlanetRenderer::getPlanet)
                .map(Planet::getName)
                .collect(Collectors.toList()));

        ParallelTransition pt = new ParallelTransition();
        // Move previously unfocused elements forward
        if (prevUnFocused != null)
            prevUnFocused.stream()
                    .filter(not(unFocused::contains))
                    .map(PlanetRenderer::getModel)
                    .map(m -> {
                        TranslateTransition tt = new TranslateTransition(FADE_ANIM_TIME, m);
                        tt.setToZ(0);
                        return tt;
                    }).forEach(t -> pt.getChildren().add(t));

        // Move newly unfocused elements backwards
        unFocused.stream()
                .map(PlanetRenderer::getModel)
                .map(m -> {
                    TranslateTransition tt = new TranslateTransition(FADE_ANIM_TIME, m);
                    tt.setToZ(100);
                    return tt;
                }).forEach(t -> pt.getChildren().add(t));

        // Perform the transition
        pt.playFromStart();
    }

    public void setFinished() {
        if (unFocused == null)
            return;

        ParallelTransition pt = new ParallelTransition();
        unFocused.stream()
                .map(PlanetRenderer::getModel)
                .map(m -> {
                    TranslateTransition tt = new TranslateTransition(FADE_ANIM_TIME, m);
                    tt.setToZ(0);
                    return tt;
                }).forEach(t -> pt.getChildren().add(t));
        pt.playFromStart();
    }
    public void setPlanetRotationSpeed(double speed) {
        for (PlanetRenderer renderer : planetRenderers) {
            renderer.setRotationSpeed(speed);
        }
    }
}