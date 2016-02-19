package d2.teamproject.module.planets.gfx;

import d2.teamproject.PARTH;
import d2.teamproject.module.planets.Planet;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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

        for (PlanetRenderer p:planetRenderers) {
            p.getModel().setOnMouseClicked(e ->{
                // TODO: Better click checking needs to be added
                if(p.getClicked()){
                    zoomOut(camera).play();
                    p.setClicked(false);
                }
                else {
                    zoomIn(camera, p.getModel().getLocalToSceneTransform().getTx()).play();
                    p.setClicked(true);
                }
            });
        }
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
