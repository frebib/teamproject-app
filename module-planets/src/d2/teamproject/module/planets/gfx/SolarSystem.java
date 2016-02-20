package d2.teamproject.module.planets.gfx;

import d2.teamproject.PARTH;
import d2.teamproject.module.planets.Planet;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.Collectors;

public class SolarSystem {
    private final SubScene scene;
    private final Group root;
    private PerspectiveCamera camera;

    private List<Planet> planets;
    private List<PlanetRenderer> planetRenderers;
    private Image skybox;
    private Double intialCameraXPosition;

    public SolarSystem(List<Planet> planets, Image skyboxTexture) {
        this.planets = planets;
        intialCameraXPosition = -140.0; // Offset right slightly for Sun
        planetRenderers = planets.stream()
                                 .map(PlanetRenderer::new)
                                 .collect(Collectors.toList());
        root = new Group();
        scene = new SubScene(root, PARTH.MIN_WIDTH, PARTH.MIN_HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);

        camera = new PerspectiveCamera();
        camera.setFieldOfView(40);
        camera.setTranslateX(intialCameraXPosition);
        camera.setTranslateY(scene.getHeight() / -2);
        camera.setRotationAxis(new Point3D(0, 1, 0));

        root.getChildren().add(camera);
        root.getChildren().addAll(planetRenderers.stream()
                                     .map(PlanetRenderer::getModel)
                                     .collect(Collectors.toList()));
        // TODO: Fix antialiasing issues
        Text planetName = new Text();
        planetName.setFont(new Font(81));
        planetName.setFill(Color.WHITE);
        planetName.setStyle("-fx-stroke: black;-fx-stroke-width: 1;");
        planetName.setVisible(false);
        planetName.setFontSmoothingType(FontSmoothingType.GRAY);
        Pane planetNameHolder = new Pane();
        planetNameHolder.setMouseTransparent(true);
        planetNameHolder.getChildren().add(planetName);
        planetNameHolder.setTranslateZ(-80);
        planetNameHolder.setTranslateY(0);
        planetNameHolder.setCache(true);
        planetNameHolder.setCacheHint(CacheHint.SCALE_AND_ROTATE);

        root.getChildren().add(planetNameHolder);

        for (PlanetRenderer p:planetRenderers) {
            p.getModel().setOnMouseClicked(e ->{
                // TODO: Better click checking needs to be added
                if(p.getClicked()){
                    planetName.setVisible(false);
                    zoomOut(camera).play();
                    p.setClicked(false);
                }
                else {
                    planetNameHolder.setLayoutX(p.getModel().getLocalToSceneTransform().getTx()-130);
                    planetName.setText(p.getPlanet().getName());
                    zoomIn(camera, p.getModel().getLocalToSceneTransform().getTx()).play();
                    planetName.setVisible(true);

                    p.setClicked(true);
                }
            });
        }


        root.setOnDragDetected(e ->swap(planetRenderers.get(0).getModel(),planetRenderers.get(2).getModel()).play());
        scene.setCamera(camera);
    }

    public TranslateTransition zoomIn(PerspectiveCamera camera, double planetPosition){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2.5),camera);
        tt.setToZ(600);
        tt.setToX(-625+planetPosition);
        return tt;
    }

    public TranslateTransition zoomOut(PerspectiveCamera camera){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2),camera);
        tt.setToZ(0);
        tt.setToX(intialCameraXPosition);
        return tt;
    }

    public TranslateTransition move(Node n, double x, double y){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2),n);
        tt.setToX(x);
        tt.setToY(y);
        return tt;
    }

    public SequentialTransition swap(Node planet1, Node planet2){
        double halfway = (planet2.getTranslateX()-planet1.getTranslateX())/2;
        // TODO: Calculate height based on which planets are swapping
        double height = 150;

        TranslateTransition planet2arcStart = move(planet2,halfway,height);
        TranslateTransition planet2arcFinish = move(planet2,planet1.getTranslateX(),0);
        TranslateTransition planet1arcStart = move(planet1,halfway,-height);
        TranslateTransition planet1arcFinish = move(planet1,planet2.getTranslateX(),0);

        ParallelTransition arcStart= new ParallelTransition();
        arcStart.getChildren().addAll(planet2arcStart,planet1arcStart);
        ParallelTransition arcFinish= new ParallelTransition();
        arcFinish.getChildren().addAll(planet2arcFinish,planet1arcFinish);

        SequentialTransition sq = new SequentialTransition();
        sq.getChildren().addAll(arcStart,arcFinish);
        return sq;
    }

    public void startAnim() {
//        planetRenderers.stream().forEach(PlanetRenderer.getAnimation::start);
    }

    public void stopAnim() {
//        planetRenderers.stream().forEach(PlanetRenderer.getAnimation::stop);
    }

    public SubScene getScene() {
        return scene;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }
}
