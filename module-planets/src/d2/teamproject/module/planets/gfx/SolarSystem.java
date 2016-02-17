package d2.teamproject.module.planets.gfx;

import d2.teamproject.PARTH;
import d2.teamproject.module.planets.Planet;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public class SolarSystem {
    private final SubScene scene;
    private final Group root;
    private PerspectiveCamera camera;

    private List<Planet> planets;
    private List<PlanetRenderer> planetRenderers;

    public SolarSystem(List<Planet> planets) {
        this.planets = planets;
        planetRenderers = planets.stream()
                                 .map(PlanetRenderer::new)
                                 .collect(Collectors.toList());
        root = new Group();
        scene = new SubScene(root, PARTH.MIN_WIDTH, PARTH.MIN_HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.MAGENTA);

        camera = new PerspectiveCamera();
        camera.setFieldOfView(120);
        camera.setTranslateZ(-1000d);
        camera.setRotationAxis(new Point3D(0, 1, 0));
        camera.setRotate(50);

        root.getChildren().add(camera);
        root.getChildren().add(planetRenderers.get(0).getModel());
        root.getChildren().add(planetRenderers.get(1).getModel());
        root.getChildren().add(planetRenderers.get(2).getModel());
        root.getChildren().add(planetRenderers.get(3).getModel());
        root.getChildren().add(planetRenderers.get(4).getModel());
        root.getChildren().add(planetRenderers.get(5).getModel());
        root.getChildren().add(planetRenderers.get(6).getModel());
        root.getChildren().add(planetRenderers.get(7).getModel());

        scene.setCamera(camera);
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
