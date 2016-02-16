package d2.teamproject.module.tubesearch;

import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import javafx.scene.layout.StackPane;

public class TubeSearchView implements BaseView {
    private TubeSearchController controller;
    private StackPane stackPane;

    public TubeSearchView(TubeSearchController module) {
        this.controller = module;
        stackPane = new StackPane();
    }

    public BaseController getController() {
        return controller;
    }

    @Override
    public StackPane getPane() {
        /* ADD CODE HERE */

        return stackPane;
    }
}