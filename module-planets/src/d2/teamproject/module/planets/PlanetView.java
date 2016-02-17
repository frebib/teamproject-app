package d2.teamproject.module.planets;

import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import d2.teamproject.module.planets.gfx.SolarSystem;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PlanetView implements BaseView {
    private PlanetController controller;

    private BorderPane pane;
    private Group root;

    private SolarSystem sSystem;
    public PlanetView(PlanetController controller) {
        this.controller = controller;
        controller.setView(this);

        this.sSystem = new SolarSystem(controller.getPlanets());
        this.root = new Group();
        this.root.getChildren().add(sSystem.getScene());
        this.pane = new BorderPane(root);
    }

    public BaseController getController() {
        return controller;
    }

    @Override
    public Scene getScene(VisualisationView visView) {
        visView.setContent(pane);
        return visView.getScene();
    }
}