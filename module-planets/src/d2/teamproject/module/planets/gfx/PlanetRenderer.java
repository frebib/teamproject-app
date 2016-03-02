package d2.teamproject.module.planets.gfx;

import d2.teamproject.module.planets.Planet;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class PlanetRenderer {
    // TODO: Figure out the best GAP for all planets
    public static final int GAP = 40;
    private static float cumulativeDist = 0;

    private Planet planet;
    private Group model;
    private Sphere sphere;
    private final double radius;

    private RotateTransition axisRotation;

    public PlanetRenderer(Planet planet) {
        // TODO: Add informative planet text
        this.planet = planet;

        radius = Math.log(planet.getDiameter() / 800) * 16d;
        model = new Group();
        model.setTranslateX(cumulativeDist + radius);
        sphere = new Sphere(radius);
        cumulativeDist += (radius * 2) + GAP;

        float rotTime = 32 * planet.getRotationTime();
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
        });
        sphere.setMaterial(mat);

        // TODO: Add moons & rings

        model.getChildren().add(sphere);
        model.setRotationAxis(new Point3D(0, 0, 1));
        model.setRotate(planet.getTilt());
    }

    public PlanetRenderer onClick(EventHandler<MouseEvent> handler) {
        model.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
        return this;
    }

    public PlanetRenderer onMouseIn(EventHandler<MouseEvent> handler) {
        model.addEventHandler(MouseEvent.MOUSE_ENTERED, handler);
        return this;
    }

    public PlanetRenderer onMouseOut(EventHandler<MouseEvent> handler) {
        model.addEventHandler(MouseEvent.MOUSE_EXITED, handler);
        return this;
    }

    public double getRadius() {
        return radius;
    }

    public Node getModel() {
        return model;
    }

    public Planet getPlanet() {
        return planet;
    }
}