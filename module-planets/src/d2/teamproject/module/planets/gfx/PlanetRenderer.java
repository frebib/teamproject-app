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

/**
 * Renders a {@link Planet} object
 * @author Joseph Groocock
 */
public class PlanetRenderer {
    private final Planet planet;
    private final Group model;
    private final double radius;

    private RotateTransition axisRotation;

    public PlanetRenderer(Planet planet, float leftOffset) {
        // TODO: Add informative planet text
        this.planet = planet;

        radius = Math.log(planet.getDiameter() / 800) * 16d;
        model = new Group();
        model.setTranslateX(leftOffset + radius);
        Sphere sphere = new Sphere(radius);

        float rotTime = 10 * planet.getRotationTime();
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

    /**
     * Binds a handler to the onClick event of the planet model
     * @param handler handler to receive the event
     * @return this {@link PlanetRenderer} instance
     */
    public PlanetRenderer onClick(EventHandler<MouseEvent> handler) {
        model.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
        return this;
    }

    /**
     * Binds a handler to the onMouseIn event of the planet model
     * @param handler handler to receive the event
     * @return this {@link PlanetRenderer} instance
     */
    public PlanetRenderer onMouseIn(EventHandler<MouseEvent> handler) {
        model.addEventHandler(MouseEvent.MOUSE_ENTERED, handler);
        return this;
    }

    /**
     * Binds a handler to the onMouseOut event of the planet model
     * @param handler handler to receive the event
     * @return this {@link PlanetRenderer} instance
     */
    public PlanetRenderer onMouseOut(EventHandler<MouseEvent> handler) {
        model.addEventHandler(MouseEvent.MOUSE_EXITED, handler);
        return this;
    }

    /**
     * @return the radius of the planet model
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @return the planet model
     */
    public Node getModel() {
        return model;
    }

    /**
     * @return the planet that the renderer is drawing
     */
    public Planet getPlanet() {
        return planet;
    }

    /**
     * Sets the rotation speed of the planet model
     * @param speed rotation speed to set planet model to
     */
    public void setRotationSpeed(double speed) {
        axisRotation.setRate(speed);
    }
}