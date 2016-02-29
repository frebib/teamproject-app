package d2.teamproject.module.planets;

import d2.teamproject.algorithm.sorting.CompareSortState;
import d2.teamproject.algorithm.sorting.SortState;
import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.planets.gfx.SolarSystem;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.List;
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

    public Pane getPane() {
        return contentBox;
    }
    public Parent getWindow() {
        return contentBox.getParent().getParent();
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // Load skybox image
        Image skybox = (Image) res.get("skybox");
        sSystem = new SolarSystem(controller.getPlanets(), skybox);
        contentBox.getChildren().add(sSystem.getScene());
    }

    public void updateState(SortState<Planet> state) {
        System.out.println("state = " + state);
        if (state instanceof CompareSortState)
            sSystem.sortSwap((CompareSortState<Planet>) state);
    }
}