package d2.teamproject.module.planets.gfx;

import d2.teamproject.PARTH;
import d2.teamproject.module.planets.Planet;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.Collectors;

public class SolarSystem {
    private final SubScene scene;
    private final Group root;
    private PerspectiveCamera camera;

    private List<Planet> planets;
    private List<PlanetRenderer> planetRenderers;

    public SolarSystem(List<Planet> planets, Image skyboxTexture) {
        this.planets = planets;
        planetRenderers = planets.stream()
                                 .map(PlanetRenderer::new)
                                 .collect(Collectors.toList());
        root = new Group();
        scene = new SubScene(root, PARTH.MIN_WIDTH, PARTH.MIN_HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);

        // TODO: Add skybox texture

        camera = new PerspectiveCamera();
        camera.setFieldOfView(40);
        camera.setTranslateX(-140); // Offset right slightly for Sun
        camera.setTranslateY(scene.getHeight() / -2);
        camera.setRotationAxis(new Point3D(0, 1, 0));

        root.getChildren().add(camera);
        root.getChildren().addAll(planetRenderers.stream()
                                     .map(PlanetRenderer::getModel)
                                     .collect(Collectors.toList()));

        scene.setCamera(camera);
    }

    private RotateTransition rotateAroundAxis(Node node) {
        RotateTransition rotate = new RotateTransition(Duration.seconds(5),node);
        rotate.setAxis(Rotate.Y_AXIS);
        rotate.setFromAngle(360);
        rotate.setToAngle(0);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        return rotate;
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
