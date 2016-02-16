package d2.teamproject.module.tubesearch;

import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import javafx.scene.layout.Pane;

public class TubeSearchView implements BaseView {
    private TubeSearchController controller;
    private Pane pane;

    public TubeSearchView(TubeSearchController module) {
        this.controller = module;
        pane = new Pane();
    }

    public BaseController getController() {
        return controller;
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}