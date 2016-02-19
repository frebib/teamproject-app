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
    private static final int gap = 40;
    private static float cumulativeDist = 0;

    private Planet planet;
    private Group model;
    private Sphere sphere;
    private Boolean clicked = false;

    private RotateTransition axisRotation;

    public PlanetRenderer(Planet planet) {
        this.planet = planet;

        double radius = Math.log(planet.getDiameter() / 800) * 16d;
        model = new Group();
        model.setTranslateX(cumulativeDist + radius);
        sphere = new Sphere(radius);
        cumulativeDist += (radius * 2) + gap;

        float rotTime = 20 * planet.getRotationTime();
        axisRotation = new RotateTransition(Duration.seconds(Math.abs(rotTime)), sphere);
        axisRotation.setAxis(Rotate.Y_AXIS);
        // TODO: Some planets spin in the wrong direction
        // https://i.imgur.com/uzjbGST.jpg
        axisRotation.setFromAngle(0);
        axisRotation.setToAngle(360 * Math.signum(rotTime));
        axisRotation.setInterpolator(Interpolator.LINEAR);
        axisRotation.setCycleCount(RotateTransition.INDEFINITE);
        axisRotation.playFromStart();

        PhongMaterial mat = new PhongMaterial();

        planet.getTextures().forEach((k, v) -> {
            String img = k.toLowerCase();
            String name = planet.getName().toLowerCase();

            if (img.startsWith(name + '.'))
                mat.setDiffuseMap(v);
            else if (img.contains("-normal."))
                mat.setBumpMap(v);
//            else if (img.contains("-spec."))
//                mat.setSpecularMap(v);

            // No handling for normal maps here
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
    public Boolean getClicked() { return  clicked; }
    public void setClicked(Boolean bool) { clicked = bool; }
}
