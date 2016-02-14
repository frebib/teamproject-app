package d2.teamproject.module.tubesearch;

import d2.teamproject.module.BaseModule;
import d2.teamproject.module.BaseView;
import javafx.scene.layout.StackPane;

public class TubeSearchView implements BaseView {
    private TubeSearchModule module;
    private StackPane stackPane;

    public TubeSearchView(TubeSearchModule module) {
        this.module = module;
        stackPane = new StackPane();
    }

    @Override
    public BaseModule getModule() {
        return module;
    }

    @Override
    public StackPane getPane() {
        /* ADD CODE HERE */

        return stackPane;
    }
}