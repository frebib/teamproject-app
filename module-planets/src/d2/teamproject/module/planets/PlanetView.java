package d2.teamproject.module.planets;

import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import d2.teamproject.module.planets.gfx.SolarSystem;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.Map;

public class PlanetView implements BaseView {
    private PlanetController controller;

    private BorderPane pane;
    private Group root;

    private SolarSystem sSystem;

    public PlanetView(PlanetController controller) {
        this.controller = controller;

        root = new Group();
        pane = new BorderPane(root);
    }

    public BaseController getController() {
        return controller;
    }

    @Override
    public Scene getScene(VisualisationView visView) {
        visView.setContent(pane);
        return visView.getScene();
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        Image skybox = (Image) res.get("skybox");
        sSystem = new SolarSystem(controller.getPlanets(), skybox);
        root.getChildren().add(sSystem.getScene());
    }
}