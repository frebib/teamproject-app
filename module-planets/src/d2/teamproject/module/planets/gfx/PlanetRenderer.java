package d2.teamproject.module.planets.gfx;

import d2.teamproject.module.planets.Planet;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class PlanetRenderer {
    private Planet planet;
    private Sphere model;
    static Integer previousPlanet = 100;

    private RotateTransition rot;

    public PlanetRenderer(Planet planet) {
        this.planet = planet;
        this.model = new Sphere(planet.getDiameter());
//        model.setTranslateX(planet.getDistToSun() / 100000d);
        model.setTranslateX(previousPlanet);
        model.setTranslateY(720 / 2d);
        previousPlanet += (int)(planet.getDiameter() + 60);

//        this.rot = new RotateTransition(planet.getRotateSpeed() * SECS_PER_HOUR, model);
        rot = new RotateTransition(Duration.seconds(15), model);
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
            else if (img.contains("-bump.")){
                System.out.println(img);
                mat.setBumpMap(v);
            }
            else if (img.contains("-spec.")) {
                System.out.println(img);
                mat.setSpecularMap(v);
            }
            // No handling for normal maps here
//            System.out.println();
        });
        model.setMaterial(mat);
    }

    public Node getModel() {
        return model;
    }
}
