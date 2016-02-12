package d2.teamproject.visualisations.tubesearch;

import d2.teamproject.visualisations.BaseModule;
import d2.teamproject.visualisations.BaseView;
import javafx.scene.layout.StackPane;

public class TubeSearchView implements BaseView {
    private TubeSearchModule module;
    private StackPane stackPane;

    public TubeSearchView(TubeSearchModule module) {
        this.module = module;
    }

    @Override
    public BaseModule getModule() {
        return (BaseModule) module;
    }

    public StackPane getStackPane() {
        /* ADD CODE HERE */

        return stackPane;
    }
}
