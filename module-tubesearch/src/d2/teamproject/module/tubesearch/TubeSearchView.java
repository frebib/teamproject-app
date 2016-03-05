package d2.teamproject.module.tubesearch;

import d2.teamproject.gui.VisualisationView;
import d2.teamproject.module.BaseController;

import java.util.Map;

public class TubeSearchView extends VisualisationView {
    private TubeSearchController controller;

    public TubeSearchView(TubeSearchController controller) {
        this.controller = controller;
    }

    public BaseController getController() {
        return controller;
    }
}