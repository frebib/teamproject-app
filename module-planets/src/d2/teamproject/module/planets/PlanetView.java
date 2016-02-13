package d2.teamproject.module.planets;

import d2.teamproject.visualisations.BaseModule;
import d2.teamproject.visualisations.BaseView;
import javafx.scene.layout.StackPane;

public class PlanetView extends BaseView {
    private StackPane stackPane;

    public PlanetView(BaseModule module) {
        super(module);
        stackPane = new StackPane();
    }

    public StackPane getPane() {
        /* ADD CODE HERE */

        return stackPane;
    }
}