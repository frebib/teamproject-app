package d2.teamproject.module.tubesearch;

import d2.teamproject.visualisations.BaseModule;
import d2.teamproject.visualisations.BaseView;
import javafx.scene.layout.StackPane;

public class TubeSearchView extends BaseView {
    private StackPane stackPane;

    public TubeSearchView(BaseModule module) {
        super(module);
        stackPane = new StackPane();
    }

    public StackPane getPane() {
        /* ADD CODE HERE */

        return stackPane;
    }
}