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
    private final Double initialCameraXPosition;

    private Planet zoomed;

    /**
     * Uses a list of planets to create the models for the scene, adds information of the planet to the scene
     * Creates multiple rectangles used to simulate the stars in the background
     * Adds event handlers to each planet to zoom on click and show information
     *
     * @param planets  A list of Planet used to create Planet Renderers used in the scene
     * @param skyboxTexture A image used for the background
     */
    public SolarSystem(List<Planet> planets, Image skyboxTexture) {

        initialCameraXPosition = -140.0;    /* Offset right slightly for Sun */
        planetRenderers = planets.stream().map(PlanetRenderer::new).collect(Collectors.toList());
        root = new Group();
        scene = new SubScene(root, PARTH.MIN_WIDTH, PARTH.MIN_HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK); /* Black background */

        camera = new PerspectiveCamera();
        camera.setNearClip(0.001);
        camera.setFieldOfView(40);
        camera.setTranslateX(initialCameraXPosition);
        camera.setTranslateY(scene.getHeight() / -2); /* Sets the camera in the middle of the window */
        camera.setRotationAxis(new Point3D(0, 1, 0));

        root.getChildren().add(camera);
        root.getChildren().addAll(planetRenderers.stream().map(PlanetRenderer::getModel).collect(Collectors.toList())); // TODO: Get rid of this planetRenderers

        // TODO: Cleanup
        Text planetName = new Text();                                               // TODO: Fix antialiasing issues (Possibly only on my machine?)
        planetName.setFont(new Font(40));
        planetName.setFill(Color.WHITE);
        planetName.setStyle("-fx-stroke: black;-fx-stroke-width: 1;");
//        planetName.setVisible(false);                                               /* Hidden to be set visible on click */
        planetName.setFontSmoothingType(FontSmoothingType.GRAY);                    /* Might help with font smoothing */

        Text planetSize = new Text();                                               // TODO: Fix antialiasing issues (Possibly only on my machine?)
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

        root.getChildren().add(planetNameHolder);

        // TODO: Cleanup
        createSkyboxSection(-250,-750,skyboxTexture,randomNumber());               /* Skybox for the left hand side of the screen */
        createSkyboxSection(-250,-250,skyboxTexture,randomNumber());
        createSkyboxSection(-250,250,skyboxTexture,randomNumber());
        createSkyboxSection(250,-750,skyboxTexture,randomNumber());                /* Skybox for the center of the screen */
        createSkyboxSection(250,-250,skyboxTexture,randomNumber());
        createSkyboxSection(250,250,skyboxTexture,randomNumber());
        createSkyboxSection(750,-750,skyboxTexture,randomNumber());                /* Skybox for the right hand side of the screen*/
        createSkyboxSection(750,-250,skyboxTexture,randomNumber());
        createSkyboxSection(750,250,skyboxTexture,randomNumber());

        for (PlanetRenderer p : planetRenderers) {
            p.onClick(e -> {
                if(p.getPlanet() == zoomed) {                                        // TODO: Better click checking needs to be added
                    zoomed = null;
//                    planetName.setVisible(false);                                    /* Hide the planets name*/
                    planetNameHolder.setVisible(false);
                    zoomOut(camera).play();                                          /* Zoom out the camera*/
                }
                else {
                zoomed = p.getPlanet();
                planetNameHolder.setLayoutX(p.getModel().getLocalToSceneTransform().getTx()-130);   /* Move the planet information in front the planet*/
                planetName.setText(p.getPlanet().getName());                                        /* Set the text to the current planets name */
                planetSize.setText(("\n"+Float.toString(p.getPlanet().getMass())));
                zoomIn(camera, p).play();                                                           /* Zoom in the camera */

                System.out.println("Planet Local x: "+p.getModel().getLocalToParentTransform().getTx());
                planetNameHolder.setTranslateX(p.getModel().getLocalToParentTransform().getTx());
                System.out.println("Text x: "+planetNameHolder.getLocalToParentTransform().getTx());
                planetNameHolder.setVisible(true);                                                      /* Show the planet information */
                }
            });
        }

        root.setOnDragDetected(e -> {
            SequentialTransition test = new SequentialTransition();
            test.getChildren().addAll(messUp(),swapBack());
            test.play();
        });
        scene.setCamera(camera);
    }

    /**
     * Moves a camera close to a planet position, giving a zoom in effect
     *
     * @param camera The camera that is to be move
     * @return  A transition to be played
     */
    private TranslateTransition zoomIn(PerspectiveCamera camera, PlanetRenderer renderer){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1.5), camera);
        double onX = renderer.getModel().getLocalToSceneTransform().getTx();
        tt.setToZ(1000 - (renderer.getRadius() * 6));
        tt.setToX(-625 + onX);
        return tt;
    }

    /**
     * Returns a camera to its original position
     *
     * @param camera The camera that is to be moved
     * @return A transition to be played
     */
    private TranslateTransition zoomOut(PerspectiveCamera camera){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), camera);
        tt.setToZ(0);
        tt.setToX(initialCameraXPosition);
        return tt;
    }

    /**
     * Moves a node (usually a planet model) to a position (x,y) for 2 seconds
     *
     * @param n A Node to translate
     * @param x The x coordinate to move the node to
     * @param y The x coordinate to move the node to
     * @return  A transition to be played
     */
    private TranslateTransition move(Node n, double x, double y){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2),n);
        tt.setToX(x);
        tt.setToY(y);
        return tt;
    }

    /**
     * @return either 2000,3000,4000 randomly as a double
     */
    private double randomNumber(){
        Random x = new Random();
        int y = x.nextInt(2)+2;
        return (double)y*1000;
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
    private void createSkyboxSection(double xPos, double yPos, Image sb, double time){
        Rectangle rect = new Rectangle(500,500);
        rect.setTranslateX(xPos);
        rect.setTranslateY(yPos);
        rect.setTranslateZ(200);
        rect.setFill(new ImagePattern(sb));
//        rect.setFill(Color.GRAY);                                   /* comment in to check random fade out */

        FadeTransition ft = new FadeTransition(Duration.millis(time), rect);
        ft.setFromValue(1.0);
        ft.setToValue(0.6);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
        root.getChildren().add(rect);
    }

    /**
     * Takes two planets and swaps them in arc movement
     * The transitions is split into for four arcs, some are played in parallel and others sequentially
     *
     * @param planet1 Planet A in the swap
     * @param planet2 Planet B in the swap
     * @return the whole transition
     */
    @Deprecated
    private SequentialTransition swap(Node planet1, Node planet2){
        double halfway = ((planet2.getTranslateX()-planet1.getTranslateX())/2)+planet1.getTranslateX();
        // TODO: Calculate height based on which planets are swapping
        double height = 200;

        TranslateTransition planet2arcStart = move(planet2,halfway,height);
        TranslateTransition planet2arcFinish = move(planet2,planet1.getTranslateX(),0);
        TranslateTransition planet1arcStart = move(planet1,halfway,-height);
        TranslateTransition planet1arcFinish = move(planet1,planet2.getTranslateX(),0);

        ParallelTransition arcS= new ParallelTransition();
        arcS.getChildren().addAll(planet2arcStart,planet1arcStart);
        ParallelTransition arcF= new ParallelTransition();
        arcF.getChildren().addAll(planet2arcFinish,planet1arcFinish);

        SequentialTransition sq = new SequentialTransition();
        sq.getChildren().addAll(arcS,arcF);
        return sq;
    }

    private SequentialTransition messUp(){
        SequentialTransition sq = new SequentialTransition();
        SequentialTransition one = swap(planetRenderers.get(3).getModel(),planetRenderers.get(5).getModel());
        SequentialTransition two = swap(planetRenderers.get(4).getModel(),planetRenderers.get(6).getModel());
        SequentialTransition three = swap(planetRenderers.get(0).getModel(),planetRenderers.get(2).getModel());
        sq.getChildren().addAll(one,two,three);
        // 0 1 2 3 4 5 6
        // 2 1 0 5 6 3 4
        return  sq;
    }

    private SequentialTransition swapBack(){
        SequentialTransition sq = new SequentialTransition();

        int[] incorrectOrder = {2,1,0,5,6,3,4}; /* This should be created  when the planets are rendered in the wrong order*/
        int[] correctOrder   = {0,1,2,3,4,5,6}; /* This should be taken in from the sorting*/

        for (int k = 0; k < incorrectOrder.length - 1; k++)
            sq.getChildren().add(swap(
                    planetRenderers.get(incorrectOrder[k]).getModel(),
                    planetRenderers.get(correctOrder[k]).getModel()
            ));
        return sq;
    }

    public void sortSwap(CompareSortState<Planet> state){
        SequentialTransition sq = new SequentialTransition();
        Point p = state.getCompares();
        TranslateTransition planet2arcFinish;
        TranslateTransition planet1arcFinish;
        Node planet1 = planetRenderers.get(p.x).getModel();
        Node planet2 = planetRenderers.get(p.y).getModel();
        double halfway = ((planet2.getTranslateX()-planet1.getTranslateX())/2)+planet1.getTranslateX();
        double height = 200; // TODO: Calculate height based on which planets are swapping

        TranslateTransition planet2arcStart = move(planet2,halfway,height);
        TranslateTransition planet1arcStart = move(planet1,halfway,-height);
        ParallelTransition arcS= new ParallelTransition();
        arcS.getChildren().addAll(planet2arcStart,planet1arcStart);
        sq.getChildren().add(arcS);

        PauseTransition pt =new PauseTransition(Duration.seconds(0.5));
        sq.getChildren().add(pt);

        // TODO: Make the other planets fade here and add that to sq

        if(state.isSwap()) { /* Swap the planets  */
            planet2arcFinish = move(planet2,planet1.getTranslateX(),0);
            planet1arcFinish = move(planet1,planet2.getTranslateX(),0);

        }
        else { /* Return the planets */
            planet2arcFinish = move(planet2,planet2.getTranslateX(),0);
            planet1arcFinish= move(planet1,planet1.getTranslateX(),0);

        }
        ParallelTransition arcF= new ParallelTransition();
        arcF.getChildren().addAll(planet2arcFinish,planet1arcFinish);
        sq.getChildren().add(arcF);
        sq.play();
    }

    /**
     * @return the SubScene
     */
    public SubScene getScene() {
        return scene;
    }
}
