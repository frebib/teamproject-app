package d2.teamproject.module.planets.gfx;

import d2.teamproject.module.planets.Planet;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class PlanetRenderer {
    private Planet planet;
    private Group model;
    private Sphere sphere;
    static Integer previousPlanet = 100;

    private RotateTransition rot;

    public PlanetRenderer(Planet planet) {
        this.planet = planet;
        this.model = new Group();
//        this.sphere = new Sphere(planet.getDiameter());
        this.sphere = new Sphere(50);
//        sphere.setTranslateX(planet.getDistToSun() / 100000d);
        sphere.setTranslateX(previousPlanet);
        sphere.setTranslateY(720 / 2d);
        previousPlanet += 110;

        float rotTime = 20 * planet.getRotationTime();
        rot = new RotateTransition(Duration.seconds(Math.abs(rotTime)), sphere);
        rot.setAxis(Rotate.Y_AXIS);
        rot.setFromAngle(360);
        rot.setToAngle(0);
        rot.setInterpolator(Interpolator.LINEAR);
        rot.setCycleCount(RotateTransition.INDEFINITE);
        rot.playFromStart();

        PhongMaterial mat = new PhongMaterial();

        planet.getTextures().forEach((k, v) -> {
            String img = k.toLowerCase();
            String name = planet.getName().toLowerCase();
            if (img.startsWith(name + '.')) {
                System.out.println(img);
                mat.setDiffuseMap(v);
            }
            else if (img.contains("-normal.")){
                System.out.println(img);
                mat.setBumpMap(v);
            }
//            else if (img.contains("-spec.")) {
//                System.out.println(img);
//                mat.setSpecularMap(v);
//            }
            // No handling for normal maps here
//            System.out.println();
        });
        sphere.setMaterial(mat);

        // TODO: Add moons & rings

        model.getChildren().add(sphere);
        model.setRotationAxis(new Point3D(0, 0, 1));
        model.setRotate(planet.getTilt());
    }

    public Node getModel() {
        return model;
    }
}
