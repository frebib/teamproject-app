package d2.teamproject.module.tubesearch;

import d2.teamproject.module.BaseController;
import d2.teamproject.module.BaseView;
import javafx.scene.layout.StackPane;

public class TubeSearchView implements BaseView {
    private TubeSearchController module;
    private StackPane stackPane;

    public TubeSearchView(TubeSearchController module) {
        this.module = module;
        stackPane = new StackPane();
    }

    @Override
    public BaseController getModule() {
        return module;
    }

    @Override
    public StackPane getPane() {
        /* ADD CODE HERE */

        return stackPane;
    }
}