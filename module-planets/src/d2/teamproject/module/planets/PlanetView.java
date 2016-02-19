package d2.teamproject.module.planets;

import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.planets.gfx.SolarSystem;
import javafx.scene.image.Image;

import java.util.Map;

public class PlanetView extends VisualisationView {
    private PlanetController controller;

    private SolarSystem sSystem;

    public PlanetView(PlanetController controller) {
        this.controller = controller;
    }

    public BaseController getController() {
        return controller;
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        Image skybox = (Image) res.get("skybox");
        sSystem = new SolarSystem(controller.getPlanets(), skybox);
        frontPane.setMouseTransparent(true);
        backPane.getChildren().add(sSystem.getScene());
    }
}