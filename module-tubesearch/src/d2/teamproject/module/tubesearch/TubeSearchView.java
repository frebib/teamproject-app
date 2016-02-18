package d2.teamproject.module.tubesearch;

import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.Map;

public class TubeSearchView implements BaseView {
    private TubeSearchController controller;
    private Pane pane;

    public TubeSearchView(TubeSearchController controller) {
        this.controller = controller;

        pane = new Pane();
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
        // No resources to load yet
    }
}