package d2.teamproject.module.tubesearch;

import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.Map;

public class TubeSearchView extends VisualisationView {
    private TubeSearchController controller;

    public TubeSearchView(TubeSearchController controller) {
        this.controller = controller;
    }

    public BaseController getController() {
        return controller;
    }

    @Override
    public void loadResources(Map<String, Object> res) {
        // No resources to load yet
    }
}