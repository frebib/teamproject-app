package d2.teamproject.module.planets.gfx;

import d2.teamproject.PARTH;
import d2.teamproject.algorithm.sorting.CompareSortState;
import d2.teamproject.module.planets.Planet;
import javafx.animation.*;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SolarSystem {
    private final SubScene scene;
    private final Group root;
    private final PerspectiveCamera camera;
    private final List<PlanetRenderer> planetRenderers;
    private final double initialCameraXPosition;

    private Planet zoomed;
    private final Text planetName = new Text();
    private final Text planetSize = new Text();

    /**
     * Uses a list of planets to create the models for the scene, adds information of the planet to the scene
     * Creates multiple rectangles used to simulate the stars in the background
     * Adds event handlers to each planet to zoom on click and show information
     *
     * @param planets  A list of Planet used to create Planet Renderers used in the scene
     * @param skyboxTexture A image used for the background
     */
    public SolarSystem(List<Planet> planets, Image skyboxTexture) {

        initialCameraXPosition = -60.0;    /* Offset right slightly for Sun - Changed from -140 for gap increase */
        planetRenderers = planets.stream().map(PlanetRenderer::new).collect(Collectors.toList());
        root = new Group();
        scene = new SubScene(root, PARTH.MIN_WIDTH, PARTH.MIN_HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK); /* Black background */
        createSkyboxSections(skyboxTexture);

        camera = new PerspectiveCamera();
        camera.setNearClip(0.001);
        camera.setFieldOfView(40);
        camera.setTranslateX(initialCameraXPosition);
        camera.setTranslateY(scene.getHeight() / -2); /* Sets the camera in the middle of the window */
        camera.setRotationAxis(new Point3D(0, 1, 0));

        root.getChildren().add(camera);
        root.getChildren().addAll(planetRenderers.stream().map(PlanetRenderer::getModel).collect(Collectors.toList())); // TODO: Get rid of this planetRenderers

        Pane planetNameHolder = planetInformation();
        root.getChildren().add(planetNameHolder);

        for (PlanetRenderer p : planetRenderers) {
            p.onClick(e -> {
                if(p.getPlanet() == zoomed) {   // TODO: Better click checking needs to be added
                    zoomed = null;
//                    planetName.setVisible(false);     /* Hide the planets name*/
                    planetNameHolder.setVisible(false);
                    zoomOut().play(); /* Zoom out the camera*/
                }
                else {
                zoomed = p.getPlanet();
                planetNameHolder.setLayoutX(p.getModel().getLocalToSceneTransform().getTx()-130);   /* Move the planet information in front the planet*/
                planetName.setText(p.getPlanet().getName());    /* Set the text to the current planets name */
                planetSize.setText(("\n"+Float.toString(p.getPlanet().getMass())));
                planetNameHolder.setVisible(true);  /* Show the planet information */
                zoomIn(p).play();   /* Zoom in the camera */
                }
            });
        }

        scene.setCamera(camera);
    }

    /**
     * Moves a camera close to a planet position, giving a zoom in effect
     * @return  A transition to be played
     */
    private TranslateTransition zoomIn(PlanetRenderer renderer){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1.2), camera);
        double onX = renderer.getModel().getLocalToSceneTransform().getTx();
        tt.setToZ(1000 - (renderer.getRadius() * 6));
        tt.setToX(-635 + onX);
        return tt;
    }

    /**
     * Returns a camera to its original position
     * @return A transition to be played
     */
    private TranslateTransition zoomOut(){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), camera);
        tt.setToZ(0);
        tt.setToX(initialCameraXPosition);
        return tt;
    }

    /**
     * Moves a node from starting point to the end point along a curve
     *
     * @param n The node to apply the transition on
     * @param x1 The starting x position
     * @param y1 The starting y position
     * @param x2 The end x position
     * @param y2 The end y position
     * @return A curve transition for a node
     */
    private PathTransition moveAlongCurve(Node n, double x1, double y1, double x2, double y2){
        PathTransition transition= new PathTransition();
        transition.setDuration(Duration.seconds(1.5));
        Path path = new Path();
        path.getElements().add(new MoveTo(x1,y1));
        path.getElements().add(new QuadCurveTo(x1,y2,x2,y2));

        transition.setNode(n);
        transition.setPath(path);
        return transition;
    }

    /**
     * Returns a node from starting point to the end point along a curve
     *
     * @param n The node to apply the transition on
     * @param x1 The starting x position
     * @param y1 The starting y position
     * @param x2 The end x position
     * @param y2 The end y position
     * @return A curve transition for a node
     */
    private PathTransition moveBackFinish(Node n, double x1, double y1, double x2, double y2){
        PathTransition transition= new PathTransition();
        transition.setDuration(Duration.seconds(1.5));
        Path path = new Path();
        path.getElements().add(new MoveTo(x1,y1));
        path.getElements().add(new QuadCurveTo(x2,y1,x2,y2));

        transition.setNode(n);
        transition.setPath(path);
        return transition;
    }

    /**
     * Takes in a swap step and then animates the swap or the lack thereof
     *
     * @param state A List of Planet sorting steps
     */
    public void sortSwap(CompareSortState<Planet> state){
        SequentialTransition transition = new SequentialTransition();
        Point p = state.getCompares();
        PathTransition planet2arcFinish;
        PathTransition planet1arcFinish;
        Node planet1 = planetRenderers.get(p.x).getModel();
        Node planet2 = planetRenderers.get(p.y).getModel();
        double height;
        double halfway = ((planet2.getTranslateX()-planet1.getTranslateX())/2)+planet1.getTranslateX();

        if((p.y == p.x+1)|(p.y == p.x-1)){ // TODO: This is not a working implementation
            //noinspection UnusedAssignment
            height = 72.5;
        }
        else{
            //noinspection UnusedAssignment
            height = 145;   /* Minimum height */
        }
        height = 145;

        PathTransition planet2arcStart = moveAlongCurve(planet2,planet2.getTranslateX(),planet2.getTranslateY(),halfway,height);
        PathTransition planet1arcStart = moveAlongCurve(planet1,planet1.getTranslateX(),planet1.getTranslateY(),halfway,-height);
        ParallelTransition arcS= new ParallelTransition();
        arcS.getChildren().addAll(planet2arcStart,planet1arcStart);
        transition.getChildren().add(arcS);

        PauseTransition pt =new PauseTransition(Duration.seconds(0.5));
        transition.getChildren().add(pt);

        // TODO: Make the other planets fade here and add that to transition

        if(state.isSwap()) { /* Swap the planets */
            planet2arcFinish = moveBackFinish(planet2,halfway,height,planet1.getTranslateX(),planet1.getTranslateY());
            planet1arcFinish = moveBackFinish(planet1,halfway,-height,planet2.getTranslateX(),planet2.getTranslateY());
        }
        else { /* Return the planets to their original positions */
            planet2arcFinish = moveBackFinish(planet2,halfway,height,planet2.getTranslateX(),planet2.getTranslateY());
            planet1arcFinish = moveBackFinish(planet1,halfway,-height,planet1.getTranslateX(),planet1.getTranslateY());
        }

        ParallelTransition arcFinish = new ParallelTransition();
        arcFinish.getChildren().addAll(planet2arcFinish,planet1arcFinish);
        transition.getChildren().add(arcFinish);
        transition.play();
    }

    /**
     * Creates a rectangle in the given position with a image as the background,
     * then fades the shape in and out for a certain amount of time
     *
     * @param xPos The x position where the rectangle should go
     * @param yPos The y position where the rectangle should go
     * @param sb   The background image of rectangle
     * @param time The duration of the fade transition
     */
    private void skyboxSection(double xPos, double yPos, Image sb, double time){
        Rectangle rect = new Rectangle(500,500);
        rect.setTranslateX(xPos);
        rect.setTranslateY(yPos);
        rect.setTranslateZ(200);
        rect.setFill(new ImagePattern(sb));
//        rect.setFill(Color.GRAY); /* comment in to check random fade out */

        FadeTransition transition = new FadeTransition(Duration.millis(time), rect);
        transition.setFromValue(1.0);
        transition.setToValue(0.6);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
        root.getChildren().add(rect);
    }

    /**
     *
     * A pane contain information about a planet
     *
     * @return A pane with a TextFlow object
     */
    private Pane planetInformation(){
        // TODO: Cleanup
        // TODO: Fix antialiasing issues (Possibly only on my machine?)
        planetName.setFont(new Font(40));
        planetName.setFill(Color.WHITE);
        planetName.setStyle("-fx-stroke: black;-fx-stroke-width: 1;");
//        planetName.setVisible(false);                                               /* Hidden to be set visible on click */
        planetName.setFontSmoothingType(FontSmoothingType.GRAY);                    /* Might help with font smoothing */

        planetSize.setFont(new Font(20));
        planetSize.setFill(Color.WHITE);
        planetSize.setStyle("-fx-stroke: black;-fx-stroke-width: 1;");

        TextFlow planetFlow = new TextFlow();
        planetFlow.setTextAlignment(TextAlignment.CENTER);
        planetFlow.setMaxWidth(500);
        planetFlow.getChildren().addAll(planetName,planetSize);

        Pane planetNameHolder = new Pane();                                         /* Wrapper for the text */
        planetNameHolder.setMouseTransparent(true);
        planetNameHolder.getChildren().add(planetFlow);
//        planetNameHolder.setTranslateZ(-80);
        planetNameHolder.setVisible(false);
        planetNameHolder.setCache(true);
        planetNameHolder.setCacheHint(CacheHint.SCALE_AND_ROTATE);

        return  planetNameHolder;
    }

    /**
     * @return either 2000,3000,4000 randomly as a double
     */
    private double randomNumber(){
        Random x = new Random();
        int y = x.nextInt(2)+2;
        return (double)y*1000;
    }

    private void createSkyboxSections(Image skyboxTexture){
        skyboxSection(-170,-750,skyboxTexture,randomNumber());               /* Skybox for the left hand side of the screen */
        skyboxSection(-170,-250,skyboxTexture,randomNumber());
        skyboxSection(-170,250,skyboxTexture,randomNumber());
        skyboxSection(330,-750,skyboxTexture,randomNumber());                /* Skybox for the center of the screen */
        skyboxSection(330,-250,skyboxTexture,randomNumber());
        skyboxSection(330,250,skyboxTexture,randomNumber());
        skyboxSection(830,-750,skyboxTexture,randomNumber());                /* Skybox for the right hand side of the screen*/
        skyboxSection(830,-250,skyboxTexture,randomNumber());
        skyboxSection(830,250,skyboxTexture,randomNumber());
    }

    /**
     * @return the SubScene
     */
    public SubScene getScene() {
        return scene;
    }
}
